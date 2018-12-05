package com.stocos.produto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.http.Query;

import com.stocos.dao.DefaultDaoImpl;
import com.stocos.lote.LoteDao;
import com.stocos.redecosmeticos.RedeCosmeticos;
import com.stocos.redecosmeticos.RedeCosmeticosDao;
import com.stocos.servico.DefaultServicoImpl;

public class ProdutoService extends DefaultServicoImpl<Produto> {

	public ProdutoService() {
		super(ProdutoDao.getInstance());
	}

	public String getTodosOsProdutosDaRede(Query query) {
		String idRede = query.get("id-rede");
		JSONArray arr = new JSONArray();
		Map<String, JSONObject> produtos = new HashMap<>();
		LoteDao.getInstance().getByAtributoAsJson("id-rede", idRede).forEach(l -> {
			String id = l.getString("id-produto");
			int qnt = l.getInt("quantidade");
			JSONObject p = ProdutoDao.getInstance().getByIdAsJson(id);
			p.put("qnt", qnt);
			if (produtos.containsKey(p.getString("nome"))) {
				produtos.get("nome").accumulate("qnt", qnt);
			} else {
				produtos.put(p.getString("nome"), p);
			}
		});
		produtos.forEach((nome, json) -> arr.put(json));
		return arr.toString();
	}

	public String getByIdRede(Query query) throws Exception {
		String id = query.get(DefaultDaoImpl.CAMPO_UUID);
		List<String> jsonList = LoteDao.getInstance().getByAtributoAsString("id-rede", id);
		Set<String> jsonSet = new HashSet<>(jsonList);
		JSONArray jsonArr = new JSONArray();
		jsonSet.stream().map(JSONObject::new).forEach(jsonArr::put);
		return jsonArr.toString();
	}

	public String getByNomeRede(Query query) throws Exception {
		String nome = query.get("nome");
		RedeCosmeticosDao redeDao = RedeCosmeticosDao.getInstance();
		Entry<UUID, RedeCosmeticos> rede = redeDao.getByNome(nome);
		query.put(DefaultDaoImpl.CAMPO_UUID, rede.getKey().toString());
		return getByIdRede(query);
	}

}
