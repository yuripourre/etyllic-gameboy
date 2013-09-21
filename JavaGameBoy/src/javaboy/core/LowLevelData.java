package javaboy.core;

public class LowLevelData {

	private static final String hexChars = "0123456789ABCDEF";
	
	/** Returns the unsigned value (0 - 255) of a signed byte */
	public short unsign(byte b) {
		if (b < 0) {
			return (short) (256 + b);
		} else {
			return b;
		}
	}
	
	/** Returns the unsigned value (0 - 255) of a signed 8-bit value stored in a short */
	public short unsign(short b) {
		if (b < 0) {
			return (short) (256 + b);
		} else {
			return b;
		}
	}
	
	/** Returns a string representation of an 8-bit number in hexadecimal */
	public String hexByte(int b) {
		String s = new Character(hexChars.charAt(b >> 4)).toString();
		s = s + new Character(hexChars.charAt(b & 0x0F)).toString();

		return s;
	}

	/** Returns a string representation of an 16-bit number in hexadecimal */
	public String hexWord(int w) {
		return new String(hexByte((w & 0x0000FF00) >>  8) + hexByte(w & 0x000000FF));
	}
	public void debugLog(String s){
		System.out.println("Debug: " + s);
	}
	
}
