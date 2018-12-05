$(document).ready(() => {
    $('#filtrarProdutosDiv').hide();
    $('#filtrarAgendamentosDiv').hide();
    montarListaProdutos();

    // Tenta realizar o cadastro
    $('#cadastrar-agendamento').click(() => {
        let lote = {};
        lote['status'] = 1;
        lote['id-produto'] = $('#produto').data('id-produto');
        lote['quantidade'] = $('#quantidade').val();
        lote['data-validade'] = $('#data-validade').val() + 'T00:00';
        lote['data-entrega'] = $('#data-entrega').val();
        lote['data-agendamento'] = '2018-12-05';
        let nomeRede = $('#nome-da-rede').val();
        $.get('http://localhost:4567/redeCosmeticos/getAll', (data) => {
            if (data) {
                for (let r = 0; r < data.length; r++) {
                    let rede = data[r];
                    if (rede.nome == nomeRede) {
                        lote['id-rede'] = rede.id;
                        $.ajax({
                            type: 'POST',
                            url: 'http://localhost:4567/lote/add',
                            data: JSON.stringify(lote),
                            success: function (data) {
                                if (data) {
                                    if (data.status == true) {
                                        alert('O agendamento foi confirmado com sucesso!');
                                    }
                                }
                            },
                            contentType: "text/plain",
                            dataType: 'json'
                        });
                        break;
                    }
                }
            }
        });
    });

    // Filtra os produtos da lista
    $("#filtrarProdutos").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#listaProdutos div").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    // Filtra os agendamentos da lista
    $("#filtrarAgendamentos").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#listaAgendamentos div").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

});


// Monta a lista de produtos cadastradas
function montarListaProdutos() {
    $('#listaProdutos').html('<div class="list-group-item text-secondary">Conectando...</div>');
    var lista = $("#listaProdutos");

    $.get("http://localhost:4567/produto/getAll", (data) => {
        if (data) {
            if (data.length > 0) {
                $('#listaProdutos').empty();
                for (var i = 0; i < data.length; i++) {
                    var produto = data[i];
                    $('#listaProdutos').append('<div class="list-group-item list-group-item-action"' +
                        'data-id-produto="' + produto.id + '" data-nome-produto="' + produto.nome + '"' +
                        ' id="list-home-list" data-toggle="list" href="#list-home" onclick="atualizarInfos(this);"' +
                        ' role="tab" aria-controls="home">' + produto.nome + '</div>');
                }
                $('#filtrarProdutosDiv').show();
            } else {
                $('#listaProdutos').html('<div class="list-group-item text-secondary">Nenhum produto cadastrado.</div>');
            }
        } else {
            $('#listaProdutos').html('<div class="list-group-item text-danger">Não foi possível obter os dados.</div>');
        }
    }).done(function (data) {

    }).fail(function () {
        $('#listaProdutos').html('<div class="list-group-item text-secondary">Erro ao comunicar com o servidor.</div>');
    }).always(function () {

    });
}

// Monta a lista de agendamentos cadastradas
function montarListaAgendamentos() {
    $('#listaAgendamentos').html('<div class="list-group-item text-secondary">Conectando...</div>');
    var lista = $("#listaAgendamentos");
    var redesCadastradas = [];

    $.get("http://localhost:4567/produto/getAll", (data) => {
        if (data) {
            if (data.length > 0) {
                $('#listaAgendamentos').empty();
                for (var i = 0; i < data.length; i++) {
                    var agendamento = data[i];
                    $('#listaAgendamentos').append('<div class="list-group-item list-group-item-action">'
                        + agendamento.dataAgendamento + ' ' + agendamento.idProduto + '</div>');
                }
                $('#filtrarAgendamentosDiv').show();
            } else {
                $('#listaAgendamentos').html('<div class="list-group-item text-secondary">Nenhuma rede Cadastrada.</div>');
            }
        } else {
            $('#listaAgendamentos').html('<div class="list-group-item text-danger">Não foi possível obter os dados.</div>');
        }
    }).done(function (data) {

    }).fail(function () {
        $('#listaAgendamentos').html('<div class="list-group-item text-secondary">Erro ao comunicar com o servidor.</div>');
    }).always(function () {

    });
}

//Atualiza as informações do formulário ao escolher produto
function atualizarInfos(produtoClicado) {
    $("#produto").val('');
    let nomeProduto = $(produtoClicado).data("nome-produto");
    let idProduto = $(produtoClicado).data("id-produto");
    $("#produto").val(nomeProduto);
    $("#produto").data("id-produto", idProduto);

}
