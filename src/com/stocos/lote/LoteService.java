package com.stocos.lote;

import java.util.UUID;

import org.json.JSONObject;
import org.simpleframework.http.Query;

import com.stocos.dao.DefaultDaoImpl;
import com.stocos.produto.Produto;
import com.stocos.produto.ProdutoDao;
import com.stocos.redecosmeticos.RedeCosmeticos;
import com.stocos.redecosmeticos.RedeCosmeticosDao;
import com.stocos.redecosmeticos.RedeCosmeticosService;
import com.stocos.servico.DefaultServicoImpl;

public class LoteService extends DefaultServicoImpl<Lote> {

	public LoteService() {
		super(LoteDao.getInstance());
	}

	// Calcula se e possivel adicionar o lote:
	private boolean podeAdicionar(Lote lote) throws Exception {
		// Obtem a rede do lote a ser adicionado
		RedeCosmeticosDao redeDao = RedeCosmeticosDao.getInstance();
		RedeCosmeticos rede = redeDao.getById(lote.getIdRede()).getValue();
		if (rede == null)
			throw new Exception("Rede nao encontrada!");

		// Obtem o produto atrelado ao lote a ser adicionado
		ProdutoDao pDao = ProdutoDao.getInstance();
		Produto produto = pDao.getById(lote.getIdProduto()).getValue();
		if (produto == null)
			throw new Exception("Produto nao encontrado!");

		double total = produto.getVolume() * lote.getQuantidade();

		RedeCosmeticosService redeService = new RedeCosmeticosService();
		double ocupacao = new JSONObject(redeService.getOcupacao(lote.getIdRede().toString())).getDouble("resultado");

		System.out.println("Tentando adicionar um lote de volume " + total + " na rede " + rede.getNome()
				+ " (capacidade: " + rede.getCapacidade() + " - Ocupacao: " + ocupacao + ")");

		return rede.getCapacidade() > ocupacao + total;
	}

	@Override
	public String add(JSONObject json) throws Exception {
		Lote lote = getDao().fromJson(json);
		if (lote.getStatus() != 1)
			throw new Exception("Erro ao adicionar lote! O status inicial deve ser 1." + lote.getStatus());
		else if (podeAdicionar(lote))
			return new JSONObject().put("status", getDao().create(lote)).toString();
		else
			throw new Exception("Erro ao adicionar lote: Capacidade insuficiente!");
	}

	@Override
	public String update(JSONObject json) throws Exception {
		UUID id = UUID.fromString(json.getString(DefaultDaoImpl.CAMPO_UUID));
		Lote lote = getDao().getById(id).getValue();
		Lote loteModificado = getDao().fromJson(json);
		if (lote.getStatus() <= loteModificado.getStatus()) {
			throw new Exception("Voce deve avancar o status do lote. Status atual: " + lote.getStatus());
		} else if (loteModificado.getStatus() == 2) { // ENTREGUE
			return "" + getDao().update(id, loteModificado);
		} else if (loteModificado.getStatus() == 3) { // AGENDADO PARA RETIRADA

		} else if (loteModificado.getStatus() == 4) { // RETIRADO

		}
		return new JSONObject().put("status", "O status informado e invalido. Envie um numero de 1 a 4").toString();
	}

	@Override
	public String delete(Query query) throws Exception {
		return "";
	}

}
