package com.stocos.servico;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.http.Query;

import com.stocos.dao.DefaultDaoImpl;

public class DefaultServicoImpl<O> implements Servico<UUID, O> {

	private DefaultDaoImpl<O> dao;

	public DefaultServicoImpl(DefaultDaoImpl<O> dao) {
		this.dao = dao;
	}

	protected DefaultDaoImpl<O> getDao() {
		return dao;
	}

	@Override
	public String getAll() throws Exception {
		List<JSONObject> jsonList = dao.getAllAsJson();
		JSONArray arr = new JSONArray();
		jsonList.forEach(arr::put);
		return arr.toString();
	}

	@Override
	public String getById(Query query) throws Exception {
		String uuid = query.get(DefaultDaoImpl.CAMPO_UUID);
		JSONArray arr = new JSONArray();
		arr.put(dao.getByIdAsJson(uuid));
		return arr.toString();
	}

	@Override
	public String getByAtributo(Entry<String, String> entry) throws Exception {
		List<JSONObject> jsonList = dao.getByAtributoAsJson(entry.getKey(), entry.getValue());
		JSONArray arr = new JSONArray();
		jsonList.forEach(arr::put);
		return arr.toString();
	}

	@Override
	public String add(JSONObject json) throws Exception {
		return new JSONObject().put("status", dao.create(dao.fromJson(json))).toString();
	}

	@Override
	public String update(JSONObject json) throws Exception {
		UUID uuid = UUID.fromString(json.getString(DefaultDaoImpl.CAMPO_UUID));
		return new JSONObject().put("status", dao.update(uuid, dao.fromJson(json))).toString();
	}

	@Override
	public String delete(Query query) throws Exception {
		String id = query.get(DefaultDaoImpl.CAMPO_UUID);
		return new JSONObject().put("status", dao.delete(UUID.fromString(id))).toString();
	}
}
