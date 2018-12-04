package com.stocos.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.stocos.servidor.Server;
import com.stocos.servidor.ServerListener;

public class ToolBarControl extends JToolBar implements ServerListener {
	private static final long serialVersionUID = 1L;

	private static ToolBarControl INSTANCE;

	public static ToolBarControl getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ToolBarControl();
		return INSTANCE;
	}

	private static Color stoppedColor = new Color(255, 80, 80);
	private static Color startedColor = new Color(10, 255, 10);

	private JLabel statusLbl, statusTextLbl;
	private JSpinner porta;

	public ToolBarControl() {
		Server.getInstance().addServerListener(this);
		Border etched = new EtchedBorder();
		Border empty = new EmptyBorder(5, 5, 5, 5);
		setBorder(new CompoundBorder(etched, empty));
		setFloatable(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		initComponents();
	}

	private void initComponents() {
		initStatus();
		initPortaField();
		initButtons();
	}

	private void initStatus() {
		statusLbl = new JLabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;

				Point2D center = new Point2D.Float(getWidth() / 2, getHeight() / 2);
				float radius = 25;
				Point2D focus = new Point2D.Float(getWidth() / 2, getHeight() / 2);
				float[] dist = { 0.0f, 1.0f };
				Color[] colors = { getBackground(), ToolBarControl.getInstance().getBackground() };
				RadialGradientPaint grad = new RadialGradientPaint(center, radius, focus, dist, colors,
						CycleMethod.NO_CYCLE);

				g2d.setPaint(grad);
				g2d.fillOval(0, 0, getWidth(), getHeight());
				g.dispose();
			}
		};
		statusLbl.setOpaque(true);
		statusLbl.setBackground(stoppedColor);
		Dimension d = new Dimension(30, 30);
		statusLbl.setPreferredSize(d);
		statusLbl.setMinimumSize(d);
		statusLbl.setMaximumSize(d);
		statusLbl.setSize(d);
		statusTextLbl = new JLabel("Pronto");
		statusTextLbl.setBorder(new EmptyBorder(5, 5, 5, 5));
		statusTextLbl.setOpaque(true);
		statusTextLbl.setBackground(new Color(190, 190, 190));
	}

	private void initPortaField() {
		porta = new JSpinner(new SpinnerNumberModel(Server.getInstance().porta(), 1, 99999, 1));
		porta.setMaximumSize(new Dimension(40, 30));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(porta, "#");
		porta.setEditor(editor);
	}

	private void initButtons() {
		ButtonGroup group = new ButtonGroup();
		JToggleButton tbtn = new JToggleButton("Iniciar");
		tbtn.setBorder(new EmptyBorder(5, 5, 5, 5));
		tbtn.addActionListener(e -> {
			Server.getInstance().porta((int) porta.getValue());
			Server.getInstance().start();
		});
		group.add(tbtn);
		add(tbtn);
		tbtn = new JToggleButton("Pausar");
		tbtn.setBorder(new EmptyBorder(5, 5, 5, 5));
		tbtn.addActionListener(e -> {
			Server.getInstance().stop();
		});
		tbtn.setSelected(true);
		group.add(tbtn);
		add(tbtn);
		JLabel portaLbl = new JLabel("Porta:");
		portaLbl.setBorder(new EmptyBorder(0, 20, 0, 4));
		add(portaLbl);
		add(porta);
		add(Box.createHorizontalGlue());
		add(statusTextLbl);
		add(Box.createHorizontalGlue());
		add(statusLbl);
	}

	@Override
	public void onServerStart() {
		statusLbl.setBackground(startedColor);
		statusTextLbl.setText("Servidor iniciado em localhost:" + Server.getInstance().porta());
		porta.setEnabled(false);
	}

	@Override
	public void onServerStop() {
		statusLbl.setBackground(stoppedColor);
		statusTextLbl.setText("Servidor parado.");
		porta.setEnabled(true);
	}

	@Override
	public void onServerRequest(Request req) {

	}

	@Override
	public void onServerResponse(Request req, Response res, String resBody) {

	}

	@Override
	public void onPortChange(int novaPorta) {

	}
}
