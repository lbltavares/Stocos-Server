package com.stocos.gui;

public class Janela {
	private static Janela INSTANCE;

	public static Janela getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Janela();
		return INSTANCE;
	}
	
	private Janela() {
		
	}
}
