package br.com.emulator.javaboy.core.printer;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;

public class GameBoyPrinterWindow extends Frame {
	
	private static final long serialVersionUID = -1650189025502780521L;
	
	private int width;
	private int height;
	
	private Image i;
	private int scale = 2;

	GameBoyPrinterWindow(String title, int width, int height) {
		super(title);
		
		this.width = width;
		this.height = height;
		
		setSize(width * scale, height * scale);
		setResizable(false);
	}

	public void setImage(Image i) {
		this.i = i;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		g.setColor(new Color(255, 0, 255));
		g.drawImage(i, 0, 0, width * 2, height * 2, null);
	}

};
