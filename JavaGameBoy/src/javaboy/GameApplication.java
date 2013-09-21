package javaboy;


import java.awt.Color;

import javaboy.core.Cartridge;
import javaboy.core.Dmgcpu;
import br.com.etyllica.core.application.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyboardEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.event.Tecla;
import br.com.etyllica.core.video.Graphic;
import br.com.etyllica.layer.BufferedLayer;

public class GameApplication extends Application{

	public GameApplication(int w, int h) {
		super(w, h);
	}

	private String cartucho = "/opt/gameboy/game.gb";

	private Cartridge cartridge;

	private Dmgcpu dmgcpu;

	private BufferedLayer cam;

	@Override
	public void load() {

		cartridge = new Cartridge(cartucho);
		dmgcpu = new Dmgcpu(cartridge);
		dmgcpu.reset();

		cam = new BufferedLayer(0,0);

		Thread t = new Thread(){
			public void run(){
				
				dmgcpu.execute(-1);
			}
		};

		t.start();

		//clearBeforeDraw = false;
		//updateAtFixedRate(1);


		loading = 100;
	}

	public void timeUpdate(){
		dmgcpu.execute(1000);
	}

	@Override
	public void draw(Graphic g) {
		
		/*if(!dmgcpu.graphicsChip.frameDone){
			
			//dmgcpu.graphicsChip.draw(0,0);
			cam.igualaImagem(dmgcpu.graphicsChip.backBuffer);
			//cam.draw(g);
			System.out.println("Force Wait");
		}else{
			//System.out.println("Force Draw");
			
			cam.igualaImagem(dmgcpu.graphicsChip.backBuffer);
			//dmgcpu.graphicsChip.draw(0,0);
		}*/
		
		cam.igualaImagem(dmgcpu.graphicsChip.backBuffer);
		cam.draw(g);
		
	}

	@Override
	public GUIEvent updateKeyboard(KeyboardEvent event) {

		if(event.getPressed(Tecla.TSK_UP_ARROW)){
			if (!dmgcpu.ioHandler.padUp) {
				dmgcpu.ioHandler.padUp = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_UP_ARROW)){
			dmgcpu.ioHandler.padUp = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.getPressed(Tecla.TSK_DOWN_ARROW)) {
			if (!dmgcpu.ioHandler.padDown) {
				dmgcpu.ioHandler.padDown = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_DOWN_ARROW)){
			dmgcpu.ioHandler.padDown = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.getPressed(Tecla.TSK_LEFT_ARROW)) {
			if (!dmgcpu.ioHandler.padLeft) {
				dmgcpu.ioHandler.padLeft = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_LEFT_ARROW)){
			dmgcpu.ioHandler.padLeft = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}		
		else if (event.getPressed(Tecla.TSK_RIGHT_ARROW)) {
			if (!dmgcpu.ioHandler.padRight) {
				dmgcpu.ioHandler.padRight = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_RIGHT_ARROW)){
			dmgcpu.ioHandler.padRight = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}		
		else if (event.getPressed(Tecla.TSK_Z)) {
			if (!dmgcpu.ioHandler.padA) {
				dmgcpu.ioHandler.padA = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_Z)){
			dmgcpu.ioHandler.padA = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		} 
		else if (event.getPressed(Tecla.TSK_X)) {
			if (!dmgcpu.ioHandler.padB) {
				dmgcpu.ioHandler.padB = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_X)){
			dmgcpu.ioHandler.padB = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.getPressed(Tecla.TSK_ENTER)) {
			if (!dmgcpu.ioHandler.padStart) {
				dmgcpu.ioHandler.padStart = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_ENTER)){
			dmgcpu.ioHandler.padStart = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.getPressed(Tecla.TSK_SPACE)) {
			if (!dmgcpu.ioHandler.padSelect) {
				dmgcpu.ioHandler.padSelect = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.getReleased(Tecla.TSK_SPACE)){
			dmgcpu.ioHandler.padSelect = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}

		/*switch (key) {
		case KeyEvent.VK_F1    : if (dmgcpu.graphicsChip.frameSkip != 1)
			dmgcpu.graphicsChip.frameSkip--;
		//showStatus("Frameskip now " + dmgcpu.graphicsChip.frameSkip);
		break;
		case KeyEvent.VK_F2    : if (dmgcpu.graphicsChip.frameSkip != 10)
			dmgcpu.graphicsChip.frameSkip++;
		//showStatus("Frameskip now " + dmgcpu.graphicsChip.frameSkip);
		break;
		case KeyEvent.VK_F5    : dmgcpu.terminateProcess();
		activateDebugger();
		System.out.println("- Break into debugger");
		break;
		}*/

		return null;
	}

	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		return null;
	}


	//public int gerencia() {

	//dmgcpu.reset();
	//dmgcpu.execute(-1);

	//dmgcpu.graphicsChip.draw(0, 0);

	//}




}
