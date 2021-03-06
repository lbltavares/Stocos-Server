package com.stocos.controller;

import java.io.PrintStream;

import org.json.JSONObject;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.stocos.indicadores.Indicadores;
import com.stocos.lote.LoteService;
import com.stocos.produto.ProdutoService;
import com.stocos.redecosmeticos.RedeCosmeticosService;
import com.stocos.servico.DefaultServicoImpl;

public class ControllerImpl extends AbstractController {

	private ProdutoService produtoService = new ProdutoService();
	private RedeCosmeticosService redeService = new RedeCosmeticosService();
	private LoteService loteService = new LoteService();

	public ControllerImpl(Request req, Response res, PrintStream body) {
		super(req, res, body);
	}

	@Override
	public void handle() {

		// Rotas padrao:
		rotasPadrao("produto", produtoService);
		rotasPadrao("redecosmeticos", redeService);
		rotasPadrao("lote", loteService);

		// Rotas especificas:
		get("/produto/getByIdRede", (req, res) -> produtoService.getByIdRede(req.getQuery()));
		get("/produto/getByNomeRede", (req, res) -> produtoService.getByNomeRede(req.getQuery()));
		get("/produto/getTodosOsProdutosDaRede", (req, res) -> produtoService.getTodosOsProdutosDaRede(req.getQuery()));
		get("/redecosmeticos/getOcupacao", (req, res) -> redeService.getOcupacao(req.getQuery().get("idrede")));

		// Indicadores:
		get("/indicadores", Indicadores::getIndicadores);
	}

	private <O> void rotasPadrao(String item, DefaultServicoImpl<O> sv) {
		get("/" + item + "/getall", (req, res) -> sv.getAll());
		get("/" + item + "/getById", (req, res) -> sv.getById(req.getQuery()));
		get("/" + item + "/getByAtributo", (req, res) -> sv.getByAtributo(req.getQuery().entrySet().iterator().next()));
		get("/" + item + "/delete", (req, res) -> sv.delete(req.getQuery()));
		post("/" + item + "/add", (req, res) -> sv.add(new JSONObject(req.getContent())));
		post("/" + item + "/update", (req, res) -> sv.update(new JSONObject(req.getContent())));
	}
}
