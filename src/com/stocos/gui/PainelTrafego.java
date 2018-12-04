package com.stocos.gui;

import java.awt.BorderLayout;
import java.time.LocalTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.stocos.servidor.Server;
import com.stocos.servidor.ServerListener;

public class PainelTrafego extends JPanel implements ServerListener {
	private static final long serialVersionUID = 1L;
	private static PainelTrafego INSTANCE;

	public static PainelTrafego getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PainelTrafego();
		return INSTANCE;
	}

	private JTable table;
	private JTextField searchField;
	private JButton clearBtn;

	public PainelTrafego() {
		super(new BorderLayout());
		Server.getInstance().addServerListener(this);
		initComponents();
	}

	private void initComponents() {
		initTable();
		initSearchPanel();
	}

	private void initTable() {
		DefaultTableModel model = new DefaultTableModel(new String[] { "Tipo", "Horario", "Descricao" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return ImageIcon.class;
				case 1:
					return JLabel.class;
				case 2:
					return String.class;
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
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(1).setMaxWidth(160);
		table.getColumnModel().getColumn(1).setMinWidth(160);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void initSearchPanel() {
		JPanel searchPanel = new JPanel(new BorderLayout());
		searchPanel.setBorder(new EmptyBorder(0, 6, 0, 6));
		searchPanel.add(new JLabel("Filtrar: "), BorderLayout.WEST);
		searchField = new JTextField();
		searchField.addCaretListener(e -> {
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) table.getModel()));
			sorter.setRowFilter(RowFilter.regexFilter(searchField.getText()));
			table.setRowSorter(sorter);
		});
		searchPanel.add(searchField, BorderLayout.CENTER);
		clearBtn = new JButton(new ImageIcon("res/eraser.png"));
		clearBtn.addActionListener(e -> resetarTabela());
		searchPanel.add(clearBtn, BorderLayout.EAST);
		add(searchPanel, BorderLayout.SOUTH);
	}

	public void resetarTabela() {
		((DefaultTableModel) table.getModel()).setRowCount(0);
	}

	public void adicionarItem(Object[] item) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(item);
	}

	@Override
	public void onServerStart() {

	}

	@Override
	public void onServerStop() {

	}

	@Override
	public void onServerRequest(Request req) {
		try {
			adicionarItem(new Object[] { //
					new ImageIcon("res/down-arrow.png"), //
					LocalTime.now(), //
					req.getMethod() + " " + req.getPath() + " " + req.getQuery() //
			});

		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}

	@Override
	public void onServerResponse(Request req, Response res, String resBody) {
		adicionarItem(new Object[] { //
				new ImageIcon("res/up-arrow.png"), //
				LocalTime.now(), //
				res.getCode() + " " + res.getDescription() + " " + resBody //
		});
	}

	@Override
	public void onPortChange(int novaPorta) {

	}

}
