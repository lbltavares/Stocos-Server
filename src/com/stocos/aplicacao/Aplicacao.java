package com.stocos.aplicacao;

import java.awt.EventQueue;

import com.stocos.gui.Janela;

public class Aplicacao {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> Janela.getInstance().setVisible(true));
	}
}
