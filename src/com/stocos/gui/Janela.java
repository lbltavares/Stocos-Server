package com.stocos.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Janela extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Janela INSTANCE;

	public static Janela getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Janela();
		return INSTANCE;
	}

	private Janela() {
		super("Stocos - Server");
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
		JTabbedPane mainPane = new JTabbedPane();
		mainPane.addTab("Trafego", PainelTrafego.getInstance());
		setContentPane(mainPane);
	}
}
