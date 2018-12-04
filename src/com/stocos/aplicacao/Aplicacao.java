package com.stocos.aplicacao;

import java.awt.EventQueue;

import com.stocos.gui.Janela;

public class Aplicacao {
	public static void main(String[] args) {
		// new Thread(() -> Server.getInstance().start()).start();
		EventQueue.invokeLater(() -> Janela.getInstance().setVisible(true));
	}
}
