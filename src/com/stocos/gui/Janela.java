package com.stocos.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.stocos.servidor.Server;

public class Janela extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	private static Janela INSTANCE;

	public static Janela getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Janela();
		return INSTANCE;
	}

	private JPanel mainPanel;

	private Janela() {
		super("Stocos - Server");
		addWindowListener(this);
		initLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		pack();
		setSize(590, 450);
		setLocationRelativeTo(null);
	}

	private static void initLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equalsIgnoreCase(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignore) {
		}
	}

	private void initComponents() {
		mainPanel = new JPanel(new BorderLayout());
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Trafego", PainelTrafego.getInstance());
		mainPanel.add(tabPane, BorderLayout.CENTER);
		mainPanel.add(ToolBarControl.getInstance(), BorderLayout.SOUTH);
		setContentPane(mainPanel);
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		Server.getInstance().stop();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}
}
