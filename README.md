# StoCOS - Descrição:
 StoCOS é o trabalho prático para a disciplina Trabalho Interdisciplinar de Software II do Bacharelado em Engenharia de Software da Pontifícia Universidade Católica de Minas Gerais - PUC Minas. 
 Esse repositório trata da camada de controle do projeto. O frontend pode ser encontrado [aqui].

# Composição:

- ## Alunos:
  - Bernardo Fonseca Andrade de Carvalho
  - Gabriel Henrique Souza Haddad Campos
  - Lucas Branco Laborne Tavares
  - Maria Verônica Santos Soares
  - Talita Arantes Melo

- ## Professores:
  - Hugo Bastos de Paula
  - Tadeu dos Reis Faria

[aqui]: https://github.com/Haddadson/StoCOS


# Como utilizar o Servidor:
 Para adicionar, remover, modificar e deletar Produtos, Lotes ou Redes de Cosméticos, é necessário
fazer requisições GET ou POST para o endereço do servidor (ex: localhost:4567).

## Requisições GET
 As requisições GET servem para deletar ou obter itens, e os parâmetros são passados através da
query.
### Exemplo:
Requisição                                         | Descrição
---------------------------------------------------|---------------------------------------
GET<br>localhost:4567/produto/getByAtributo?rede=Avon | Obtém todos os produtos da rede Avon.
GET<br>localhost:4567/redecosmeticos/getAll           | Obtém todas as redes de cosméticos.
GET<br>localhost:4567/produto/delete?id=123abc        | Deleta o produto de id=123abc

## Requisições POST
 As requisições POST servem para adicionar ou modificar itens. O corpo da requisição deve ser em
formato JSON, contendo as informações necessárias:
### Exemplo:
Requisição | Corpo | Descrição
-|----|-
<br><br>POST<br>localhost:4567/produto/add| {<br>    <span>&emsp;</span>"nome":"Loreal Kids",<br><span>&emsp;</span>"marca":"Loreal",<br><span>&emsp;</span>"categoria":"SHAMPOO",<br><span>&emsp;</span>"volume":14.5<br>} | Adiciona um Shampoo Loreal Kids,<br> da marca Loreal, da rede Lojas Rede,<br>com 15.6 de volume.
<br><br>POST<br>localhost:4567/lote/add|{<br><span>&emsp;</span>"status":1,<br><span>&emsp;</span>"id-rede":"123abc",<br><span>&emsp;</span>"id-produto":"837kahf",<br><span>&emsp;</span>"quantidade": 14,<br><span>&emsp;</span>"data-validade": "2018-12-16T14:33",<br><span>&emsp;</span>"data-entrega":"2018-11-10",<br><span>&emsp;</span>"data-agendamento":"2018-11-10"<br>}|Adiciona um lote ao banco de dados.


## Formatos JSON:

Cada entidade possui uma representação em JSON. Para adicionar ou modificar itens, deve ser passado
o JSON completo (com todos os campos, mas não necessariamente preenchidos - ex: “marca”:””) do
item que deseja-se adicionar ou modificar:
<b>Quando um JSON é OBTIDO do servidor, ele também virá com um atributo “id”</b>, contendo um id
único, gerado quando ele é adicionado no banco.
Segue os formatos JSON de cada item:

Entidade|Formato|Observação
----|-------|-------
Produto|{<br><span>&emsp;</span>"nome":"String",<br><span>&emsp;</span>"marca":"String",<br><span>&emsp;</span>"categoria":"String",<br><span>&emsp;</span>"volume":Double<br>}|Os atributos “Nome” e “Capacidade” são obrigatórios.
Lote|{<br><span>&emsp;</span>"status":Int,<br><span>&emsp;</span>"id-rede":"String",<br><span>&emsp;</span>"id-produto":"String",<br><span>&emsp;</span>"quantidade": Int,<br><span>&emsp;</span>"data-validade": "String",<br><span>&emsp;</span>"data-entrega":"String",<br><span>&emsp;</span>"data-agendamento":"String"<br>}|Todos os atributos são obrigatórios.
Rede de Cosméticos|{<br><span>&emsp;</span>"nome":"String",<br><span>&emsp;</span>"capacidade":Double,<br><span>&emsp;</span>"email":"String",<br><span>&emsp;</span>"telefone":"String",<br><span>&emsp;</span>"endereco":"String"<br>}|Os atributos "Nome" e "Capacidade" são obrigatórios

## REFERÊNCIA GERAL:
Todos os métodos abaixo servem para todos os itens (Redes de Cosméticos, Lotes e Produtos):
### Caminhos:
- redecosmeticos/...
- produto/...
- lote/...

TIPO|MÉTODO|QUERY|CORPO|DESCRIÇÃO
-|-|-|-|-
GET|/getAll|||Obtém uma array json com todos os itens
GET|/getById|id=123abc||Obtém o item com o id 123abc
GET|/getByAtributo|atributo=valor||Obtém uma array json com todos os itens com aquele atributo=valor
GET|/delete|id=123abc||Deleta o item com id=123abc
POST|/add||JSON do item (sem o id)|Adiciona um item no banco. Retorna true se a operação foi um sucesso, false caso contrário.
POST|/update||JSON do item (com o id)|Modifica o item com o id passado no json, retorna true se a operação foi um sucesso, false caso contrário.

## REFERÊNCIA ESPECÍFICA:
As requisições abaixo são específicas para algumas entidades

ENTIDADE|TIPO|MÉTODO|QUERY|CORPO|DESCRIÇÃO
-|-|-|-|-|-
Produto|GET|/produto/getByIdRede|id=123abc||Obtem todos os produtos de uma rede específica (busca por ID)
Produto|GET|/produto/getByNomeRede|nome=LojasRede||Obtem todos os produtos de uma rede específica (busca por Nome)
Rede de Cosméticos|GET|/redeCosmeticos/getOcupacao|idrede=123abc||Obtem a ocupação atual da rede especificada pelo ID.

