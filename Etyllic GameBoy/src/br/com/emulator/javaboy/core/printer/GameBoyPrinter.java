package br.com.emulator.javaboy.core.printer;

/*

JavaBoy

COPYRIGHT (C) 2001 Neil Millstone and The Victoria University of Manchester
                                                                         ;;;
This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 2 of the License, or (at your option)
any later version.        

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
more details.


You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 59 Temple
Place - Suite 330, Boston, MA 02111-1307, USA.

 */

import java.awt.Image;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import br.com.emulator.javaboy.core.Dmgcpu;
import br.com.emulator.javaboy.core.LowLevelData;
import br.com.emulator.javaboy.core.network.GameLink;


/** This is a completely frivolous emulation of the Game Boy Printer */

public class GameBoyPrinter extends GameLink {
	
	private final int BUFFER_SIZE = 32768;
	private final int IMAGE_WIDTH = 160;
	private final int IMAGE_HEIGHT = 320;

	private final int[] palette = {0xFFFFFFFF, 0xFF808080, 0xFF404040, 0xFF000000};

	private GameBoyPrinterWindow window;
	private Dmgcpu cpu;
	private short[] buffer = new short[BUFFER_SIZE];
	private int bufferFillPos;
	private int bufferEmptyPos;

	private int tileX, tileY;

	private int dataSize;

	private MemoryImageSource source;
	private Image image;
	private int[] imageData = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
	
	

	public GameBoyPrinter() {
		window = new GameBoyPrinterWindow("Game Boy Printer", IMAGE_WIDTH, IMAGE_HEIGHT);

		//window.show();

		bufferFillPos = 0;
		bufferEmptyPos = 0;

		dataSize = 640;

		for (int r = 0; r < IMAGE_WIDTH * IMAGE_HEIGHT; r++) {
			imageData[r] = 0xFF000000;
		}
		source = new MemoryImageSource(IMAGE_WIDTH, IMAGE_HEIGHT, new DirectColorModel(32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000),
				imageData, 0, IMAGE_WIDTH);
		source.setAnimated(true);
		source.newPixels();
		image = window.createImage(source);

		window.setImage(image);

		tileX = 0;
		tileY = 1;
	}

	public void setDmgcpu(Dmgcpu dmgcpu) {
		cpu = dmgcpu;
	}

	public void send(byte b) {
		System.out.print(b + " ");
		cpu.ioHandler.getRegisters()[0x01] = 0x00;			// Acknowledge the byte by sending a zero
		cpu.ioHandler.getRegisters()[0x02] &= 0x7F;		// Turn of the send bit
		cpu.triggerInterruptIfEnabled(cpu.INT_SER);

		buffer[bufferFillPos++] = LowLevelData.unsign(b);
		if (bufferFillPos == BUFFER_SIZE) {
			bufferFillPos = 0;
		}

		checkPackets();
	}

	int bytesAvailable() {
		if (bufferFillPos > bufferEmptyPos) {
			return bufferFillPos - bufferEmptyPos;
		} else {
			return (BUFFER_SIZE - bufferEmptyPos) + bufferFillPos;
		}
	}

	short getByte(int pos) {
		return buffer[(bufferEmptyPos + pos) % BUFFER_SIZE];
	}

	void consumeByte(int num) {
		System.out.println("Bytes consumed: " + num);
		bufferEmptyPos = (bufferEmptyPos + num) % BUFFER_SIZE;
	}

	void scrollImage() {
		// Scroll the image up by 8 pixels.  Really slow.
		for (int y = 0; y < IMAGE_HEIGHT - 8; y++) {
			for (int x = 0; x < IMAGE_WIDTH; x++) {
				imageData[y * IMAGE_WIDTH + x] = imageData[(y + 8) * IMAGE_WIDTH + x];
			}
		}
	}

	void checkPackets() { // 0xBD 0xE6


		if ((getByte(0) == 136)) {


			if ((bytesAvailable() >= 3) && (getByte(1) == 51)) {
				switch (getByte(2)) {
				case 1:
				case 2:
				case 15: {	// These are headers we can ignore (I hope)
					if (bytesAvailable() == 10) {
						consumeByte(10);
					}
					break;
				}

				case 4: { // This is a length header
					if (bytesAvailable() == 6) {
						dataSize = (getByte(5) << 8) + getByte(4);
						System.out.println("Data size set to " + dataSize);
						consumeByte(6);
					}
					break;
				}
				}
			}
		} else {
			// This bit is image data

			// Wait for a complete tile (16 bytes) before trying to process it
			if ((bytesAvailable() == 16) && (dataSize > 0))  {
				for (int offs = 0; offs < 8; offs++) {
					int b1 = getByte(offs * 2);
					int b2 = getByte(offs * 2 + 1);

					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 0] = palette[((b1 & 0x80) >> 6) + ((b2 & 0x80) >> 7)];
					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 1] = palette[((b1 & 0x40) >> 5) + ((b2 & 0x40) >> 6)];
					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 2] = palette[((b1 & 0x20) >> 4) + ((b2 & 0x20) >> 5)];
					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 3] = palette[((b1 & 0x10) >> 3) + ((b2 & 0x10) >> 4)];

					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 4] = palette[((b1 & 0x08) >> 2) + ((b2 & 0x08) >> 3)];
					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 5] = palette[((b1 & 0x04) >> 1) + ((b2 & 0x04) >> 2)];
					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 6] = palette[((b1 & 0x02) >> 0) + ((b2 & 0x02) >> 1)];
					imageData[((tileY * 8 + offs) * IMAGE_WIDTH) + (tileX * 8) + 7] = palette[((b1 & 0x01) << 1) + ((b2 & 0x01) >> 0)];

				}

				dataSize -= 16;

				tileX++;
				if (tileX == IMAGE_WIDTH >> 3) {
					tileX = 0;
					tileY++;

					if (tileY * 8 == IMAGE_HEIGHT - 8) {
						scrollImage();
						tileY--;
					}
				}

				consumeByte(16);
				source.newPixels();
				window.repaint();
			} else if (dataSize == 0) {
				consumeByte(1);
			}
		}

	}

	public void shutDown() {
		window.hide();
	}

};

