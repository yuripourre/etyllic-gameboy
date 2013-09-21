package br.com.emulator.javaboy;

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

Version 0.9

Applet Mode Changes (when running on a web page)
- Fixed ROMS with save RAM not loading when on a web page - Done
- Applets can be sized other than 1x - Done
- Applets show strip showing current ROM and other info displayed at start - Done
- Applets have options menu providing control change, size change, frameskip change, sound toggle - Done
- Applets have a parameter to turn sound on/off in the applet tag - Done

Application Mode Changes (when running stand-alone)
- Half-fixed keyboard controls sometimes not starting in application version - Done
- Fixed random keypressed causing an exception when no ROM loaded - Done

General changes
- ROMS can optionally be loaded from ZIP/JAR and GZip compressed files (code contributed by Stealth Software)

Emulation Changes
- Much more accurate emulation of sound channel 4 (noise) - Done
- Flipped double height sprites are now handled properly - Done

Version 0.91

Applet Mode Changes
- Switch menu from click to double-click to avoid problem with setting focus - Done
- Added Save to Web feature - Done
- Added reset option to menu - Done
- Fixed bad update to border when applet window covered (only on microsoft vm) - Done

Emulation Changes
- Fixed printing of HDMA data to console slowing down games - Done

Version 0.92

Emulation Changes
- Fixed LCDC interrupt LCY flag.  Fixes crash in 'Max' and graphical corruption on
  intro to 'Rayman', 'Donkey Kong Country GBC', and probably others. !!! Check Max Again !!!
- Fixed problem when grabbing the next instruction when executing right next to the 
  end of memory.  Fixes crahes on 'G&W Gallery 3', 'Millipede/Centipede' and others
- Fixed P10-P13 interrupt handling.  Fixes controls in Double Dragon 3 menus, 
  Lawnmower Man, and others.
- Added hack to unscramble status bars on many games (Aladdin, Pokemon Pinball)
  that change bank address just before the window starts
- Changed sprite hiding behaviour.  Now sprites are turned on if they're visible anywhere
  in the frame.  Doesn't properly support sprite raster effects, but stops them from
  disappearing. (Elevator Action, Mortal Kombat 4)
- Fixed debug breakpoint detection (Micro Machines 2, Monster Race 2, others)
- Changed VBlank line to fix white screen on startup (Home Alone, Dragon Tales)  (check!)
- Added extra condition to LCD interrupts - that the display should be enabled.  Max works again.
- Keep on at Mahjong.  Probably display disabled so interrupt never occurs.
- Note: broken robocop 2, exact instruction timings needed.  poo.  Only worked becuase of bad vblank line.
- Check mario golf problem.  Did it work before?
- Fixed comparison with LCY register, Austin Powers - Oh Behave! now works, and GTA status bar isn't scrambled.
- Found out that on the GBC, the BG enable doesn't do anything(?).  Fixes Dragon Ball Z.
- Fixed crash when Super Mario Bros DX tries to access the printer
- Found odd bug where tiles wouldn't validate properly until they were drawn.  Happens on the window layer.  SMBDX shows it up on the Enter/Print menu
- SF2 broken, but workings when I increase CPU speed.  That breaks music in Pinball Fantasies and Gradius 2 though.  Needs accurate CPU timings.
- Fix online save RAM bugs

New Features
- Added support for MBC3 mapper chip.  MBC3 games now work (Pokemon Blue/Crystal mainly.  Gold/silver still doesn't work)
- Added the MBC3 real time clock.  Pokemon Gold/Silver now work, as well as Harvest Moon GB.
- Added emulation of the Game Boy Printer (only in application mode for now)
 */

import br.com.etyllica.Etyllica;

/** This is the main controlling class which contains the main() method
 *  to run JavaBoy as an application, and also the necessary applet methods.
 *  It also implements a full command based debugger using the console.
 */

public class JavaBoy extends Etyllica {

	private final static int WIDTH = 160*2;
	private final static int HEIGHT = 144*2;

	public JavaBoy() {
		super(WIDTH, HEIGHT);
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		setMainApplication(new GameApplication(w, h));
	}

}
