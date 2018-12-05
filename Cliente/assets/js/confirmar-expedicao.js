$(document).ready(() => {
  obterValoresIniciais();
  montarListaExpedicoes();

    $('#dadosExpedicao').hide();

    // Filtra os itens da tabela
    $("#filtrar").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#listaExpedicoes a").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

$(document).on('click', '#confirmarExpedicao', function (e) {
    let identificador =  $('#expedicao-selecionada').data('id-expedicao');
    let agendamentos = localStorage.getItem("agendamentos");
    if (!nuloOuVazio(agendamentos)) {
      let agendamentosJSON = JSON.parse(agendamentos);
      for (var i = 0; i < agendamentosJSON.produtosExpedicao.length; i++) {
        if(agendamentosJSON.produtosExpedicao[i].idExpedicao == identificador){
          if(agendamentosJSON.produtosExpedicao[i].statusAgendamento == "agendado"){
            agendamentosJSON.produtosExpedicao[i].statusAgendamento = "confirmado";
            localStorage.setItem('agendamentos', JSON.stringify(agendamentosJSON));
            alert("Expedição confirmada!!");
          } else {
            alert("Não é possível confirmar a expedição neste status.");
          }
        }
      }
    }
    location.reload();
});

$(document).on('click', '#cancelarExpedicao', function (e) {
    let identificador =  $('#expedicao-selecionada').data('id-expedicao');
    let agendamentos = localStorage.getItem("agendamentos");
    if (!nuloOuVazio(agendamentos)) {
      let agendamentosJSON = JSON.parse(agendamentos);
      for (var i = 0; i < agendamentosJSON.produtosExpedicao.length; i++) {
        if(agendamentosJSON.produtosExpedicao[i].idExpedicao == identificador){
          if(agendamentosJSON.produtosExpedicao[i].statusAgendamento == "agendado"){
            agendamentosJSON.produtosExpedicao[i].statusAgendamento = "cancelado";
            localStorage.setItem('agendamentos', JSON.stringify(agendamentosJSON));
            alert("Expedição cancelada!!");
          } else {
            alert("Não é possível cancelar a expedição neste status.");
          }
        }
      }
    }
    location.reload();
});


$(document).on('click', '.list-group-item', function (e) {
    $('#dadosExpedicao').show();
    let idExpedicao = $(this).attr("data-id-expedicao");
    let agendamentos = localStorage.getItem("agendamentos");
    let informacoesProduto;
    if (!nuloOuVazio(agendamentos)) {
      let agendamentosJSON = JSON.parse(agendamentos);
      let produtosExpedicao = agendamentosJSON.produtosExpedicao;
      for (var i = 0; i < produtosExpedicao.length; i++) {
        if(produtosExpedicao[i].idExpedicao == idExpedicao){
          informacoesProduto = produtosExpedicao[i];
        }
      }
    }
    $("#status-icon").html('<div class="dot"></div>');
    let icone =  $("#status-icon").children()[0];
    if(informacoesProduto.statusAgendamento == "confirmado"){
      $(icone).addClass("bg-success");
    } else if (informacoesProduto.statusAgendamento == "cancelado"){
      $(icone).addClass("bg-danger");
    } else {
      $(icone).addClass("bg-warning");
    }
    $('#nomeComprador').html("<h5><div>Comprador: " + informacoesProduto.nomeComprador + "</div></h5>");
    $('#expedicao-selecionada').html("Id: "+ informacoesProduto.idExpedicao);
    $('#expedicao-selecionada').attr('data-id-expedicao', informacoesProduto.idExpedicao);
    $('#emailComprador').html("<div>E-mail: " + informacoesProduto.emailComprador + "</div>");
    $('#telefoneComprador').html("<div>Telefone: " + informacoesProduto.telefoneComprador + "</div>");
    $('#dataExpedicao').html("<div>Data da Expedição prevista: " + informacoesProduto.dataAgendamento + "</div>");
    $('#statusExpedicao').html("<div>Status da Expedição: " + informacoesProduto.statusAgendamento + "</div>");
    $('#listaProdutos').html('');
    for(let i = 0; i < informacoesProduto.listaProdutos.length; i++){
      let produto = informacoesProduto.listaProdutos;
      $('#listaProdutos').append('<div>Nome: ' + produto[i].nomeProduto +' | Quantidade: ' + produto[i].quantidadeProduto);
    }

});

function montarListaExpedicoes(){
  let agendamentos = localStorage.getItem("agendamentos");
  if (!nuloOuVazio(agendamentos)) {
    let agendamentosJSON = JSON.parse(agendamentos);
    let produtosExpedicao = agendamentosJSON.produtosExpedicao;
    if(produtosExpedicao.length > 0 ) {
      for (var i = 0; i < produtosExpedicao.length; i++) {
        let data = produtosExpedicao[i];
        let iconeClass = "";
        if(data.statusAgendamento == "confirmado"){
          iconeClass = "bg-success";
        } else if (data.statusAgendamento == "cancelado"){
          iconeClass = "bg-danger";
        } else {
          iconeClass = "bg-warning";
        }
        $('#listaExpedicoes').append('<a class="list-group-item list-group-item-action" ' +
            'id="list-home-list data-nome-comprador="' + data.nomeComprador + '" data-telefone-comprador="' + data.telefoneComprador
            + '" data-email-comprador="' + data.emailComprador +'" data-toggle="list" href="#list-home"'
            + 'role="tab" aria-controls="home" data-id-expedicao="' + data.idExpedicao + '"'
            + '> Id: ' + data.idExpedicao + ' - Comprador: '
            + data.nomeComprador + '<div class="float-right dot '+ iconeClass +' "></div>'
            + '<br> Data de Agendamento - '
            + data.dataAgendamento
            + '<br> Telefone - '
            + data.telefoneComprador
            +'</a>');
      }
    } else {
      $('#listaExpedicoes').html('<div class="list-group-item text-secondary">Nenhuma expedição agendada.</div>');
      $('#dadosExpedicao').empty();
    }
  }
}

// Monta a lista de redes cadastradas
function montarLista() {
    $('#listaExpedicoes').html('<div class="list-group-item text-secondary">Conectando...</div>');
    var lista = $("#listaExpedicoes");
    var redesCadastradas = [];
    $.get("http://localhost:4567/lote/getall", (data) => {
        $('#listaExpedicoes').empty();
        montarListaExpedicoes();
        if (data) {
            if (data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var lote = data[i];
                    if (lote['status'] == 1) {
                        var id_rede = lote['id-rede'];
                        criarDiv(id_rede, lote);
                    }
                }
                $('#filtrarDiv').show();
            } else {
                $('#listaExpedicoes').html('<div class="list-group-item text-secondary">Nenhuma expedição agendada.</div>');
                $('#dadosExpedicao').empty();
            }
        } else {
            $('#listaExpedicoes').html('<div class="list-group-item text-danger">Não foi possível obter os dados.</div>');
            $('#dadosExpedicao').empty();
        }
    }).done(function (data) {

    }).fail(function () {
        $('#listaExpedicoes').html('<div class="list-group-item text-secondary">Erro ao comunicar com o servidor.</div>');
        $('#dadosExpedicao').empty();
    }).always(function () {

    });
}

function obterValoresIniciais() {
  let json = {
    "produtosExpedicao" : [{
      dataAgendamento: "2018-12-25T15:10",
      emailComprador: "gabriel.haddad@teste",
      enderecoComprador: "Rua teste",
      idExpedicao: 1,
      listaProdutos: [{
        idProduto: "39a98c4b-4d36-47dc-817c-955d8eb2c4f3",
        nomeProduto: "Perfume Boticario Malbec",
        quantidadeProduto: "5"
      }],
      nomeComprador: "Gabriel",
      statusAgendamento: "agendado",
      telefoneComprador: "(31)99999-2130",
    },
    {
      dataAgendamento: "2018-12-25T15:10",
      emailComprador: "teste@teste",
      enderecoComprador: "Rua teste2",
      idExpedicao: 2,
      listaProdutos: [{
        idProduto: "282132dd-9d14-45a2-b30d-500503d5538a",
        nomeProduto: "Rimmel XYZ",
        quantidadeProduto: "10"
      },
      {
        idProduto: "1aee962d-2b44-4b7b-88d4-378ef5e5e54b",
        nomeProduto: "Sabonete Natura",
        quantidadeProduto: "15"
      }],
      nomeComprador: "Ronaldo ",
      statusAgendamento: "confirmado",
      telefoneComprador: "(31)99999-2130",
    },
    {
      dataAgendamento: "2018-12-02T15:10",
      emailComprador: "tis@teste",
      enderecoComprador: "Rua tis",
      idExpedicao: 3,
      listaProdutos: [{
        idProduto: "6df91758-d76a-4c4b-9124-c430a73bdfea",
        nomeProduto: "Sabonete Avon",
        quantidadeProduto: "25"
      }],
      nomeComprador: "Maria",
      statusAgendamento: "cancelado",
      telefoneComprador: "(31)99999-9999",
    }]
  }
  localStorage.setItem('agendamentos', JSON.stringify(json));
}
