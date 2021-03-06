package br.com.emulator.javaboy.core.network;

import br.com.emulator.javaboy.core.Dmgcpu;

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


/** Subclasses of this class implement the serial communcation (Game Link) interface
 */

public abstract class GameLink {
	
    protected boolean serverRunning = false;

	public abstract void send(byte b);
	public abstract void shutDown();
	public abstract void setDmgcpu(Dmgcpu dmgcpu);
	
}