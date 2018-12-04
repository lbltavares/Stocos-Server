package com.stocos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

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

	private static Color stoppedColor = new Color(160, 0, 0);
	private static Color startedColor = new Color(0, 160, 0);

	private static String stoppedMsg = "Servidor Pausado";
	private static String startedMsg = "Servidor Iniciado";

	private JTable table;
	private JLabel statusLbl, statusTextLbl;
	private JToolBar toolBar;

	public PainelTrafego() {
		super(new BorderLayout());
		Server.getInstance().addServerListener(this);
		initComponents();
	}

	private void initComponents() {
		initTable();
		initToolBar();
	}

	private void initStatusLabel() {
		statusLbl = new JLabel();
		statusLbl.setOpaque(true);
		statusLbl.setBackground(stoppedColor);
		Dimension d = new Dimension(30, 30);
		statusLbl.setPreferredSize(d);
		statusLbl.setMinimumSize(d);
		statusLbl.setMaximumSize(d);
		statusLbl.setSize(d);
		statusTextLbl = new JLabel(stoppedMsg);
		statusTextLbl.setOpaque(true);
		statusTextLbl.setBackground(new Color(190, 190, 190));
	}

	private void initTable() {
		table = new JTable(new DefaultTableModel(new String[] { "Tipo", "Dados" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		});
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void addPortaPanel() {
		JTextField field = new JTextField(5);
		field.setMaximumSize(new Dimension(90, 30));
		field.setText("" + Server.getInstance().porta());
		JButton btn = new JButton("Mudar Porta");
		btn.addActionListener(e -> {
			try {
				int novaPorta = Integer.parseInt(field.getText());
				Server.getInstance().porta(novaPorta);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Porta invalida!\nDigite apenas numeros (ex: 4567)");
			}
		});
		toolBar.add(field);
		toolBar.add(btn);
	}

	private void initToolBar() {
		initStatusLabel();
		toolBar = new JToolBar();
		toolBar.setBorder(new EtchedBorder());
		toolBar.setFloatable(false);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

		ButtonGroup group = new ButtonGroup();
		JToggleButton btn = new JToggleButton("Iniciar");
		btn.addActionListener(e -> {
			Server.getInstance().start();
		});
		group.add(btn);
		toolBar.add(btn);

		btn = new JToggleButton("Pausar");
		btn.addActionListener(e -> {
			Server.getInstance().stop();
		});
		btn.setSelected(true);
		group.add(btn);

		toolBar.add(btn);
		addPortaPanel();
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(statusTextLbl);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(statusLbl);
		add(toolBar, BorderLayout.SOUTH);
	}

	public void resetarTabela() {
		((DefaultTableModel) table.getModel()).setRowCount(0);
	}

	public void adicionarItem(Object[] item) {

	}

	@Override
	public void onServerStart() {
		statusLbl.setBackground(startedColor);
		statusTextLbl.setText(startedMsg);
	}

	@Override
	public void onServerStop() {
		statusLbl.setBackground(stoppedColor);
		statusTextLbl.setText(stoppedMsg);
	}

	@Override
	public void onServerRequest(Request req) {

	}

	@Override
	public void onServerResponse(Request req, Response res, String resBody) {
		
	}

	@Override
	public void onPortChange(int novaPorta) {
		JOptionPane.showMessageDialog(this, "A porta foi mudada para " + novaPorta);
	}

}
