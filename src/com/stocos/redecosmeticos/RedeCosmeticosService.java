package com.stocos.redecosmeticos;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;

import com.stocos.dao.DefaultDaoImpl;
import com.stocos.lote.Lote;
import com.stocos.lote.LoteDao;
import com.stocos.produto.ProdutoDao;
import com.stocos.servico.DefaultServicoImpl;

public class RedeCosmeticosService extends DefaultServicoImpl<RedeCosmeticos> {

	public RedeCosmeticosService() {
		super(RedeCosmeticosDao.getInstance());
	}

	@Override
	public String update(JSONObject json) throws Exception {
		UUID uuid = UUID.fromString(json.getString(DefaultDaoImpl.CAMPO_UUID));
		Entry<UUID, RedeCosmeticos> entry = getDao().getById(uuid);
		if (entry != null) {
			JSONObject ocup = new JSONObject(getOcupacao(json.getString(DefaultDaoImpl.CAMPO_UUID)));
			double ocupacao = ocup.getDouble("resultado");
			if (entry.getValue().getCapacidade() < ocupacao)
				throw new Exception("A capacidade da rede deve ser maior do que a ocupacao!");

			return new JSONObject().put("status", getDao().update(uuid, getDao().fromJson(json))).toString();
		} else {
			return new JSONObject().put("status", "Rede nao encontrada").toString();
		}
	}

	public String getOcupacao(String idRede) {
		ProdutoDao pDao = ProdutoDao.getInstance();
		LoteDao loteDao = LoteDao.getInstance();
		RedeCosmeticosDao.getInstance().getById(UUID.fromString(idRede));
		double ocupacao = 0;
		Map<UUID, Lote> lotes = loteDao.getByAtributo("id-rede", idRede);
		for (Lote l : lotes.values()) {
			ocupacao += pDao.getById(l.getIdProduto()).getValue().getVolume() * l.getQuantidade();
		}
		return new JSONObject().put("resultado", ocupacao).toString();
	}
}
