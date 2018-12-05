package com.stocos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import com.stocos.dao.DefaultDaoImpl;
import com.stocos.lote.LoteDao;
import com.stocos.produto.ProdutoDao;
import com.stocos.redecosmeticos.RedeCosmeticosDao;

public class PainelSolicitacoes extends JPanel {
	private static final long serialVersionUID = 1L;
	private static PainelSolicitacoes INSTANCE;

	public static PainelSolicitacoes getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PainelSolicitacoes();
		return INSTANCE;
	}

	private JTable table;

	public PainelSolicitacoes() {
		super(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		initTable();
		initControlPanel();
	}

	private void initTable() {
		DefaultTableModel model = new DefaultTableModel(new String[] { "Solicitacao", "Tipo", "Item" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return JLabel.class;
				case 1:
					return JLabel.class;
				default:
					return String.class;
				}
			}

			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(table.getRowHeight() + 10);
		table.getColumnModel().getColumn(0).setMaxWidth(130);
		table.getColumnModel().getColumn(0).setMinWidth(130);
		table.getColumnModel().getColumn(1).setMaxWidth(130);
		table.getColumnModel().getColumn(1).setMinWidth(130);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public <O> void criarSolicitacao(String sol, JSONObject json, DefaultDaoImpl<O> dao) {
		if (dao instanceof ProdutoDao) {
			adicionarItem(new Object[] { sol, "Produto", json.toString() });
		} else if (dao instanceof RedeCosmeticosDao) {
			adicionarItem(new Object[] { sol, "Rede de Cosmeticos", json.toString() });
		} else if (dao instanceof LoteDao) {
			adicionarItem(new Object[] { sol, "Lote", json.toString() });
		}
	}

	private void initControlPanel() {
		JToolBar controlPanel = new JToolBar();
		controlPanel.setFloatable(false);
		controlPanel.add(Box.createHorizontalGlue());
		JButton btn = new JButton("Rejeitar");
		btn.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10, 10, 10, 10)));
		btn.addActionListener(e -> {
			int rows[] = table.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.removeRow(rows[i]);
			}
		});
		controlPanel.add(btn);
		controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		btn = new JButton("Aceitar");
		btn.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10, 10, 10, 10)));
		btn.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row != -1) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				String entidade = (String) model.getValueAt(row, 1);
				String dados = (String) model.getValueAt(row, 2);
				JSONObject json = new JSONObject(dados);
				if (entidade.equalsIgnoreCase("Produto")) {
					if (ProdutoDao.getInstance().create(ProdutoDao.getInstance().fromJson(json))) {
						JOptionPane.showMessageDialog(this,
								"O Produto " + json.getString("nome") + " foi adicionado com sucesso!");
					} else {
						JOptionPane.showMessageDialog(this,
								"O Produto " + json.getString("nome") + " ja existe no catalogo!");
					}
				} else if (entidade.equalsIgnoreCase("Lote")) {
					//
				} else if (entidade.equalsIgnoreCase("Rede de Cosmeticos")) {
					//
				}
				model.removeRow(row);
			}
		});
		controlPanel.add(btn);
		add(controlPanel, BorderLayout.SOUTH);
	}

	public void resetarTabela() {
		((DefaultTableModel) table.getModel()).setRowCount(0);
	}

	public void adicionarItem(Object[] item) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(item);
	}
}
