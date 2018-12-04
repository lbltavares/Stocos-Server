package com.stocos.indicadores;

import java.time.LocalDate;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.stocos.lote.LoteDao;
import com.stocos.produto.ProdutoDao;
import com.stocos.redecosmeticos.RedeCosmeticosDao;

public class Indicadores {

	public static String calcularVolumeTotalDoEstoque() {
		List<JSONObject> lotes = LoteDao.getInstance().getAllAsJson();
		List<JSONObject> produtos = ProdutoDao.getInstance().getAllAsJson();
		double total = 0;
		for (JSONObject lote : lotes) {
			String idProduto = lote.getString("id-produto");
			for (JSONObject produto : produtos) {
				if (produto.getString("id").equals(idProduto)) {
					total += lote.getInt("quantidade") * produto.getDouble("volume");
				}
			}
		}
		return total + "";
	}

	public static String calcularCapacidadeTotal() {
		List<JSONObject> redes = RedeCosmeticosDao.getInstance().getAllAsJson();
		double result = 0;
		for (JSONObject rede : redes) {
			result += rede.getDouble("capacidade");
		}
		return "" + result;
	}

	public static String calcularRedesComMaisLotes() {
		List<JSONObject> lotes = LoteDao.getInstance().getAllAsJson();
		List<JSONObject> redes = RedeCosmeticosDao.getInstance().getAllAsJson();
		JSONArray resultado = new JSONArray();
		int maximo = redes.size() > 5 ? 5 : redes.size();
		redes.sort((r1, r2) -> {
			int qntRede1 = 0, qntRede2 = 0;
			// Calcular qnt de rede1:
			for (JSONObject lote : lotes) {
				if (lote.getString("id-rede").equals(r1.get("id"))) {
					qntRede1++;
				}
			}
			r1.put("qnt-lotes", qntRede1);
			// Calcular qnt de rede2:
			for (JSONObject lote : lotes) {
				if (lote.getString("id-rede").equals(r2.get("id"))) {
					qntRede2++;
				}
			}
			r2.put("qnt-lotes", qntRede2);
			return qntRede2 - qntRede1;
		});
		for (int i = 0; i < maximo; i++) {
			resultado.put(redes.get(i));
		}
		return resultado.toString();
	}

	public static String calcularRedesComMenosLotes() {
		List<JSONObject> lotes = LoteDao.getInstance().getAllAsJson();
		List<JSONObject> redes = RedeCosmeticosDao.getInstance().getAllAsJson();
		JSONArray resultado = new JSONArray();
		int maximo = redes.size() > 5 ? 5 : redes.size();
		redes.sort((r1, r2) -> {
			int qntRede1 = 0, qntRede2 = 0;
			// Calcular qnt de rede1:
			for (JSONObject lote : lotes) {
				if (lote.getString("id-rede").equals(r1.get("id"))) {
					qntRede1++;
				}
			}
			r1.put("qnt-lotes", qntRede1);
			// Calcular qnt de rede2:
			for (JSONObject lote : lotes) {
				if (lote.getString("id-rede").equals(r2.get("id"))) {
					qntRede2++;
				}
			}
			r2.put("qnt-lotes", qntRede2);
			return qntRede1 - qntRede2;
		});
		for (int i = 0; i < maximo; i++) {
			resultado.put(redes.get(i));
		}
		return resultado.toString();
	}

	public static String calcularProdutosMaisFrequentes() {
		List<JSONObject> lotes = LoteDao.getInstance().getAllAsJson();
		List<JSONObject> produtos = ProdutoDao.getInstance().getAllAsJson();
		JSONArray resultado = new JSONArray();
		int maximo = produtos.size() > 5 ? 5 : produtos.size();
		produtos.sort((p1, p2) -> {
			int freqP1 = 0, freqP2 = 0;
			for (JSONObject lote : lotes) {
				if (lote.getString("id-produto").equals(p1.get("id"))) {
					freqP1 += lote.getInt("quantidade");
				}
			}
			p1.put("frequencia", freqP1);
			for (JSONObject lote : lotes) {
				if (lote.getString("id-produto").equals(p2.get("id"))) {
					freqP2 += lote.getInt("quantidade");
				}
			}
			p2.put("frequencia", freqP2);
			return freqP2 - freqP1;
		});
		for (int i = 0; i < maximo; i++) {
			resultado.put(produtos.get(i));
		}
		return resultado.toString();
	}

	public static String calcularAgendamentosNoMes() {
		List<JSONObject> lotes = LoteDao.getInstance().getAllAsJson();
		LocalDate now = LocalDate.now();
		int total = 0;
		for (JSONObject lote : lotes) {
			LocalDate ldtLote = LocalDate.parse(lote.getString("data-agendamento"));
			if (now.getMonth().equals(ldtLote.getMonth()) && now.getYear() == ldtLote.getYear()) {
				total++;
			}
		}
		return "" + total;
	}

	public static String getIndicadores(Request req, Response res) {
		JSONObject result = new JSONObject();
		result.put("redes-mais-lotes", calcularRedesComMaisLotes());
		result.put("redes-menos-lotes", calcularRedesComMenosLotes());
		result.put("volume-total", calcularVolumeTotalDoEstoque());
		result.put("capacidade-total", calcularCapacidadeTotal());
		result.put("produtos-mais-frequentes", calcularProdutosMaisFrequentes());
		result.put("agendamentos-no-mes", calcularAgendamentosNoMes());
		return result.toString();
	}

}
