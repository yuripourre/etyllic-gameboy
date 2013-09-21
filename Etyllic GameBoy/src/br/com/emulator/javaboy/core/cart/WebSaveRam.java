package br.com.emulator.javaboy.core.cart;

import java.awt.Frame;
import java.awt.Label;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import br.com.emulator.javaboy.core.Dmgcpu;
import br.com.emulator.javaboy.core.LowLevelData;
import br.com.emulator.javaboy.core.cart.exception.NoSaveDataException;
import br.com.emulator.javaboy.dialog.DialogListener;
import br.com.emulator.javaboy.dialog.ModalDialog;

class WebSaveRAM extends LowLevelData implements Runnable, DialogListener {
	Cartridge cart;
	boolean save;
	URL url;
	Dmgcpu cpu;
	String username;

	public WebSaveRAM(URL url, boolean save, Cartridge cart, Dmgcpu cpu, String username) {
		this.url = url;
		this.save = save;
		this.cart = cart;
		this.cpu = cpu;
		this.username = username;

		if (!cart.canSave()) {

			ModalDialog d = new ModalDialog(null, "Sorry", "This game does not", "have a save facility.");

		} else {

			if (save) {
				ModalDialog d = new ModalDialog(null, "Confirm", "Are you sure you want to save?", this);
			} else {
				ModalDialog d = new ModalDialog(null, "Confirm", "Are you sure you want to load?", this);
			}
		}
	}

	public void yesPressed() {
		Thread t = new Thread(this);
		t.start();
	}

	public void noPressed() {
		// Object deleted now
	}

	public void run() {
		Frame f = new Frame("Please Wait...");
		f.setSize(200, 120);

		try {
			if (save) {
				f.add(new Label("Please wait, saving"), "North");
				f.add(new Label("game data to web server..."), "Center");   
				f.show();
				saveRam();
				new ModalDialog(null, "Sucess!", "Game data", "Saved ok.");
			} else {
				f.add(new Label("Please wait, loading"), "North");
				f.add(new Label("game data from web server..."), "Center");   
				f.show();
				loadRam();
				new ModalDialog(null, "Success!", "Game data", "loaded ok.");
			}
		} catch (NoSaveDataException e) {
			System.out.println("Error! " + e);
			new ModalDialog(null, "Error!", "No save data can be found on the server!", e.toString());
		} catch (Exception e) {
			System.out.println("Error! " + e);
			new ModalDialog(null, "Error!", "Load/Save error!  Report to site administrator.", e.toString());
		}
		f.hide();
	}

	public void saveRam() throws Exception {
		//   if (username == null) throw new Exception("No username provided");

		String params = "";
		String strUrl = url.toString();
		int questionPos = strUrl.indexOf("?");
		if (questionPos != -1) {
			params = "&" + strUrl.substring(questionPos + 1, strUrl.length());
		}

		System.out.println("Params: (" + url + ") " + params);

		url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?user=" + URLEncoder.encode(username));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.connect();

		DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

		StringBuffer saveData = new StringBuffer("");
		byte[] ram = cart.getBatteryRam();

		for (int r = 0; r < cart.getBatteryRamSize(); r++) {
			saveData.append(hexByte(unsign(ram[r])));
		}
		//   saveData = URLEncoder.encode("Hel\0lo");

		String content = "romname=" + URLEncoder.encode(cart.getRomFilename()) + "&gamename=" + URLEncoder.encode(cart.getCartName()) + "&user=" + URLEncoder.encode(username) + "&datalength=" + (cart.getBatteryRamSize() * 2) + "&data0=" + saveData + params;

		System.out.println(content);

		printout.writeBytes(content);
		printout.flush ();
		printout.close ();

		conn.disconnect();

		DataInputStream input = new DataInputStream (conn.getInputStream());
		String str;
		while (null != ((str = input.readLine()))) {
			System.out.println(str);
		}

		System.out.println("OK!");
	}

	public void loadRam() throws Exception {
		//   if (username == null) throw new Exception("No username provided");

		String params = "";
		String strUrl = url.toString();
		int questionPos = strUrl.indexOf("?");
		if (questionPos != -1) {
			params = "&" + strUrl.substring(questionPos + 1, strUrl.length());
		}

		System.out.println("Params: (" + url + ") " + params);

		url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?user=" + URLEncoder.encode(username) + params);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.connect();

		DataOutputStream printout = new DataOutputStream(conn.getOutputStream());


		String content = "gamename=" + URLEncoder.encode(cart.getCartName()) + "&romname=" + URLEncoder.encode(cart.getRomFilename());

		//   System.out.println(content);

		printout.writeBytes(content);
		printout.flush ();
		printout.close ();

		conn.disconnect();

		DataInputStream input = new DataInputStream (conn.getInputStream());
		String str;
		str = input.readLine();

		// No save
		if (str.equals("NOSAVERAM")) {
			throw new NoSaveDataException("");
		}

		// General error
		if (str.startsWith("ERROR")) {
			throw new Exception(str);
		}


		int pos = 0;
		try {
			for (int r = 0; r < cart.getBatteryRamSize(); r++) {
				String sub = str.substring(r * 2, r * 2 + 2);
				int val = Integer.valueOf(sub, 16).intValue();
				cart.ram[r] = (byte) val;
			}
		} catch (Exception e) {
			throw e;
		}
		cpu.reset();
	}
}