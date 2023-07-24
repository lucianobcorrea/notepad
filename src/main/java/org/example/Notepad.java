package org.example;

public class Notepad {

	public static void main(String[] args) {
		
		NotepadFrame notepad = new NotepadFrame();
		notepad.createScreen();
		notepad.createMenu();
		notepad.createInfo();
		notepad.setVisible(true);
	}

}
