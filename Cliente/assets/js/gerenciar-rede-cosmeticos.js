$(document).ready(() => {
    $('.divinfo').hide();
    montarListaDeRedes();

    // Filtra os itens da tabela
    $("#filtrarRedes").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#listaderedes a").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

// Busca as redes de cosméticos no servidor e atualiza o input dropdown
function montarListaDeRedes() {
    $.get('http://localhost:4567/redecosmeticos/getall', (data) => {
        if (data) {
            if (data.length > 0) {
                $('#listaderedes').empty();
                for (var i = 0; i < data.length; i++) {
                    var rede = data[i];
                    $('#listaderedes').append('<a class="list-group-item list-group-item-action" data-id-rede="'
                        + rede.id
                        + '" id="list-home-list" data-toggle="list" href="#list-home" onclick="atualizarInfos(this);" role="tab" aria-controls="home">'
                        + rede.nome
                        + '</a>');
                }
            }
        }
    });
}

// Atualiza as informações da tabela e de capacidade, de acordo com a rede selecionada
function atualizarInfos(rede) {
    let id = $(rede).data('id-rede');
    $('.nenhumarede').hide();
    $('.divinfo').show();
    atualizarCapacidade(id);
    atualizarTabela($(rede).html(), id);
}

// Atualizar capacidade, volumeDisponivel e volumeOcupado
function atualizarCapacidade(rede) {

    $.get('http://localhost:4567/redeCosmeticos/getOcupacao?idrede=' + rede, (data) => {
        if (data && !data.status) {
            $('#info-rede').data('ocupacao', data.resultado);
            atualizarInformacoesTela(rede);
        }
    });

    $.ajax({
        type: 'GET',
        url: 'http://localhost:4567/redeCosmeticos/getByAtributo?id=' + rede,
        success: function (retorno) { $('#info-rede').data('capacidade', retorno[0].capacidade); atualizarInformacoesTela(rede); },
        contentType: "text/plain",
        dataType: 'json'
    });

}

function atualizarInformacoesTela(rede) {

    let ocupacao = $('#info-rede').data('ocupacao');
    let capacidade = $('#info-rede').data('capacidade');

    $('#capacidade').html(capacidade);
    $('#ocupacao').html(ocupacao);
    $('#volumeDisponivel').html(capacidade - ocupacao);
    var dados = {
        capacidade: capacidade,
        volumeOcupado: ocupacao,
    }
    atualizarGraficos(dados);
    atualizarGraficoCategoria(rede);
}

function atualizarGraficos(dados) {
    var ocupado = (dados['volumeOcupado'] / dados['capacidade']) * 100.0;
    var ctx = document.getElementById("capacidadeCanvas").getContext('2d');
    var MeSeChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            labels: [
                "Volume (%)",
            ],
            datasets: [
                {
                    label: "Ocupado (%)",
                    data: [ocupado],
                    backgroundColor: ["#669911"],
                    hoverBackgroundColor: ["#66A2EB"]
                }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                xAxes: [{
                    ticks: {
                        min: 0,
                        max: 100,
                    }
                }],
                yAxes: [{
                    stacked: true
                }]
            }
        }
    });
}

function atualizarTabela(rede, id) {
    $('#corpo-tabela').empty();
    $.get('http://localhost:4567/produto/getTodosOsProdutosDaRede?id-rede=' + id, (produtos) => {
        if (produtos) {
            if (produtos.length == 0) {
                $('#corpo-tabela').append('<tr><th scope="row"></th><td>Não há produtos cadastrados</td><td></td><td></td><td></td><td></td></tr>');
            } else {
                for (let i = 0; i < produtos.length; i++) {
                    let p = produtos[i];
                    $('#corpo-tabela').append(
                        '<tr><th scope="row">'
                        + p.id.substring(1, 10) +
                        '</th><td>' + p.nome +
                        '</td><td>' + p.marca +
                        '</td><td>' + p.categoria +
                        '</td><td>' + p.volume +
                        '</td><td>' + p.qnt +
                        '</td></tr>');
                }
            }
        } else {
            $('#corpo-tabela').append('<tr><th scope="row"></th><td>Não há produtos cadastrados</td><td></td><td></td><td></td><td></td></tr>');
        }
    });
}

// Adiciona Produtos
$("#botao-modal-adicionar-produtos").click(() => {
    $("#nome").val("Insira o nome do produto");
    $("#volume").val("0.0");
    $("#quantidade").val("0");
    $('#marca').val($('#listaderedes a.active').html());
});

// Adiciona um produto - Botao interno DO MODAL
$('#adicionarProdutos').click(() => {
    let p = {};
    p.nome = $('#nome').val();
    p.marca = $('#marca').val();
    p.categoria = $('#categoria').val();
    p.volume = $('#volume').val();
    $.ajax({
        type: 'POST',
        url: 'http://localhost:4567/produto/add',
        data: JSON.stringify(p),
        success: function (data) {
            if (data.status == true) {
                // O produto foi adicionado
                alert('Sua solicitação para adicionar o Produto no catálogo foi enviada!');
            } else {
                alert('O produto informado já existe no catálogo!');
            }
        },
        error: function (err) {

        },
        contentType: "text/plain; charset=utf-8",
        dataType: 'json'
    });
});

// Remove um produto
$('#removerProduto').click(() => {
    let rede = $('#listaderedes a.active').html();
    let idProduto = $('#idremover').val();
    let quantidade = $('#quantidaderemover').val();

    $.get('http://localhost:4567/produto/remover?nomerede=' + rede +
        '&idproduto=' + idProduto + '&quantidade=' + quantidade, (data) => {
            if (data) {
                atualizarInfos(rede);
            }
        });
});

// Muda a capacidade total
$('#mudarCapacidadeBtn').click(() => {
    let rede = $('#listaderedes a.active').html();
    let novaCapacidade = $('#novaCapacidade').val();
    $.get('http://localhost:4567/redeCosmeticos/getByAtributo?nome=' + rede, (data) => {
        if (data) {
            let rede = data[0];
            rede.capacidade = novaCapacidade;
            $.ajax({
                type: 'POST',
                url: 'http://localhost:4567/redeCosmeticos/update',
                data: JSON.stringify(rede),
                success: function (res) {
                    if (res) {
                        if (res.status == true) {
                            alert("Capacidade alterada com sucesso!");
                            location.reload();
                        } else {
                            location.reload();
                        }
                    }
                },
                contentType: "text/plain",
                dataType: 'json'
            });
        }
    });
});

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

// Filtra um item na tabela
function filtrar() {
    var input, filter, table, tr, td, i;
    input = $('#filtrar');
    filter = input.val().toUpperCase();
    table = $('#tabela');
    tr = $('tr');
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName('td')[0];
        if (td) {
            if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = '';
            } else {
                tr[i].style.display = 'none';
            }
        }
    }
}





function atualizarGraficoCategoria(id) {
    $.get('http://localhost:4567/produto/getTodosOsProdutosDaRede?nome=' + id, (data) => {
        if (data.length == 0) {
            $('#categoriasCanvas').hide();
        } else {
            $('#categoriasCanvas').show();

            var dados = [];
            for (let i = 0; i < data.length; i++) {
                if (!dados[data[i].categoria]) {
                    dados[data[i].categoria] = data[i].quantidade;
                } else {
                    dados[data[i].categoria] += data[i].quantidade;
                }
            }
            let categorias = [];
            let quantidades = [];
            let cores = [
                "#FF6384",
                "#36A2EB",
                "#FFCE56"
            ];
            for (let i in dados) {
                categorias.push(i);
                quantidades.push(dados[i]);
                cores.push(getRandomColor());
            }

            var ctx = document.getElementById('categoriasCanvas').getContext('2d');
            var myDoughnutChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    datasets: [{
                        data: quantidades,
                        backgroundColor: cores,
                        hoverBackgroundColor: cores,
                    }],

                    labels: categorias,
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                }
            });
        }
    });
}