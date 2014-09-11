package br.com.emulator.javaboy;


import br.com.emulator.javaboy.core.Dmgcpu;
import br.com.emulator.javaboy.core.cart.Cartridge;
import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.BufferedLayer;

public class GameApplication extends Application{

	public GameApplication(int w, int h) {
		super(w, h);
	}

	private String path = "/opt/gameboy/game.gb";
	
	private Cartridge cartridge;

	private Dmgcpu dmgcpu;

	private BufferedLayer layer;

	@Override
	public void load() {

		layer = new BufferedLayer(0,0);
		
		cartridge = new Cartridge(path);
		loading = 10;
		
		dmgcpu = new Dmgcpu(cartridge);
		loading = 50;
		dmgcpu.start();

		loading = 100;
	}

	@Override
	public void draw(Graphic g) {

		layer.copy(dmgcpu.graphicsChip.getBackBuffer());		
		layer.draw(g);
	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event) {

		if(event.isKeyDown(KeyEvent.TSK_F1)){
			cartridge.saveBatteryRam();
		}
		if(event.isKeyDown(KeyEvent.TSK_F2)){
			cartridge.loadBatteryRam();
		}
		
		if(event.isKeyDown(KeyEvent.TSK_UP_ARROW)){
			if (!dmgcpu.ioHandler.padUp) {
				dmgcpu.ioHandler.padUp = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_UP_ARROW)){
			dmgcpu.ioHandler.padUp = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.isKeyDown(KeyEvent.TSK_DOWN_ARROW)) {
			if (!dmgcpu.ioHandler.padDown) {
				dmgcpu.ioHandler.padDown = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_DOWN_ARROW)){
			dmgcpu.ioHandler.padDown = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.isKeyDown(KeyEvent.TSK_LEFT_ARROW)) {
			if (!dmgcpu.ioHandler.padLeft) {
				dmgcpu.ioHandler.padLeft = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_LEFT_ARROW)){
			dmgcpu.ioHandler.padLeft = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}		
		else if (event.isKeyDown(KeyEvent.TSK_RIGHT_ARROW)) {
			if (!dmgcpu.ioHandler.padRight) {
				dmgcpu.ioHandler.padRight = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_RIGHT_ARROW)){
			dmgcpu.ioHandler.padRight = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}		
		else if (event.isKeyDown(KeyEvent.TSK_Z)) {
			if (!dmgcpu.ioHandler.padA) {
				dmgcpu.ioHandler.padA = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_Z)){
			dmgcpu.ioHandler.padA = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		} 
		else if (event.isKeyDown(KeyEvent.TSK_X)) {
			if (!dmgcpu.ioHandler.padB) {
				dmgcpu.ioHandler.padB = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_X)){
			dmgcpu.ioHandler.padB = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.isKeyDown(KeyEvent.TSK_ENTER)) {
			if (!dmgcpu.ioHandler.padStart) {
				dmgcpu.ioHandler.padStart = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_ENTER)){
			dmgcpu.ioHandler.padStart = false;
			dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
		}
		else if (event.isKeyDown(KeyEvent.TSK_SPACE)) {
			if (!dmgcpu.ioHandler.padSelect) {
				dmgcpu.ioHandler.padSelect = true;
				dmgcpu.triggerInterruptIfEnabled(dmgcpu.INT_P10);
			}
		}else if(event.isKeyUp(KeyEvent.TSK_SPACE)){
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

}
