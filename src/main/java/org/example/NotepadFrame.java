package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NotepadFrame extends JFrame {

	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 600;
	JFileChooser FILE_CHOOSER;
	int returnInput;
	JTextArea txtArea;
	JScrollPane scroll;
	JSpinner fontSizeSpinner;
	JLabel charCount;
	JLabel wordCount;
	JLabel showTextFont;
	JLabel showFontSize;
	JButton changeColorBt;

	public static int countWords(String str) {
		if (str.isEmpty()) {
			return 0;
		}
		int wordCount = 0;
		int state = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				i++;
				continue;
			}

			if (Character.isLetterOrDigit(str.charAt(i))) {
				if (state == 0) {
					wordCount++;
					state = 1;
				}
			}
			else {
				state = 0;
			}
		}

		return wordCount;
	}

	public void createScreen() {
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Notepad");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))).getImage();
		this.setIconImage(icon);

		txtArea = new JTextArea();
		scroll = new JScrollPane(txtArea);
		this.add(scroll);
	}

	public void createInfo() {

		JPanel statusPane = new JPanel();
		getContentPane().add(statusPane, BorderLayout.SOUTH);
		charCount = new JLabel();
		showTextFont = new JLabel();
		showFontSize = new JLabel();
		wordCount = new JLabel();
		statusPane.add(wordCount);
		statusPane.add(charCount);
		statusPane.add(showTextFont);
		statusPane.add(showFontSize);
		charCount.setText("| Character Counter: 0");
		wordCount.setText("Word Counter: 0");
		showTextFont.setText("| Font: " + txtArea.getFont().getFamily());
		showFontSize.setText("| Font Size: " + fontSizeSpinner.getValue());

		txtArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int textLength = txtArea.getText().length();
				wordCount.setText("Word Counter: " + countWords(txtArea.getText()));
				if(e.getKeyCode() != KeyEvent.VK_ENTER) {
					charCount.setText("| Character Counter: " + textLength++);
				}else {
					charCount.setText("| Character Counter: " + textLength--);
				}
			}
		});
	}

	public void createMenu() {

		txtArea.setFont(new Font("Arial", Font.PLAIN, 20));

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu menuFile = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem exitItem = new JMenuItem("Exit");

		JMenu menuFont = new JMenu("Font");
		JMenuItem calibriFont = new JMenuItem("Calibri");
		JMenuItem arialFont = new JMenuItem("Arial");
		JMenuItem timesNewRoFont = new JMenuItem("Times New Roman");

		menuBar.add(menuFile);
		menuFile.add(openItem);
		menuFile.add(saveItem);
		menuFile.add(exitItem);

		menuBar.add(menuFont);
		menuFont.add(calibriFont);
		menuFont.add(arialFont);
		menuFont.add(timesNewRoFont);

		fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setValue(20);

		fontSizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				txtArea.setFont(new Font(txtArea.getFont().getFamily(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
				showFontSize.setText("| Font Size: " + fontSizeSpinner.getValue());
			}
		});

		menuFont.add(fontSizeSpinner);

		ActionListener fontSelect = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == calibriFont) {
					txtArea.setFont(new Font((String)calibriFont.getText(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
				} else if(e.getSource() == arialFont) {
					txtArea.setFont(new Font((String)arialFont.getText(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
				} else if(e.getSource() == timesNewRoFont) {
					txtArea.setFont(new Font((String)timesNewRoFont.getText(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
				}
				showTextFont.setText("| Font: " + txtArea.getFont().getFamily());
			}
		};

		calibriFont.addActionListener(fontSelect);
		arialFont.addActionListener(fontSelect);
		timesNewRoFont.addActionListener(fontSelect);

		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FILE_CHOOSER = new JFileChooser(System.getProperty("user.home") + "/Desktop");
				returnInput = FILE_CHOOSER.showOpenDialog(null);

				if(returnInput == JFileChooser.APPROVE_OPTION) {
					try {
						File openFile = FILE_CHOOSER.getSelectedFile();
						FileReader fr = new FileReader(openFile);

						char[] buffer = new char[(int)openFile.length()];
						fr.read(buffer);
						String fileContent = String.valueOf(buffer);
						txtArea.setText(fileContent);
						fr.close();
					}catch(Exception err) {
						err.printStackTrace();
						JOptionPane.showMessageDialog(null, "Unable to open the file. Please try again!", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FILE_CHOOSER = new JFileChooser(System.getProperty("user.home") + "/Desktop");
				File file = new File("file.txt");
				FILE_CHOOSER.setSelectedFile(file);
				returnInput = FILE_CHOOSER.showOpenDialog(null);

				if(returnInput == JFileChooser.APPROVE_OPTION) {
					try {
						File saveFile = FILE_CHOOSER.getSelectedFile();
						FileWriter writeFile = new FileWriter(saveFile);
						writeFile.write(txtArea.getText());
						writeFile.flush();
						writeFile.close();
						JOptionPane.showMessageDialog(null, "File saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
					}catch (Exception err) {
						err.printStackTrace();
						JOptionPane.showMessageDialog(null, "Unable to save the file. Please try again!", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		changeColorBt = new JButton("Color");
		menuBar.add(changeColorBt);

		changeColorBt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(e.getSource() == changeColorBt) {
					Color color = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
					txtArea.setForeground(color);
				}
			}
		});
	}
}
