import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI implements ActionListener, KeyListener {

	JFrame frame;
	JTextField field;
	JLabel[] words;
	Song song;


	public GUI(Song s) {
		song = s;

		frame = new JFrame();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		words = new JLabel[song.length()];

		int numCols = 14;
		int numRows = words.length / numCols;
		if (words.length % numCols != 0)
			numRows++;
		JPanel wordsPanel = new JPanel(new GridLayout(1, numCols));
		JPanel[] cols = new JPanel[numCols];
		for (int i = 0; i < numCols; i++) {
			cols[i] = new JPanel(new GridLayout(numRows, 1));
			wordsPanel.add(cols[i]);
		}
		for (int i = 0; i < words.length; i++) {
			words[i] = new JLabel(" " + song.getOriginalWord(i) + " ");
			words[i].setOpaque(true);
			words[i].setBackground(new Color(i % 55, i * 13 % 45, i * 17 % 40));
			words[i].setForeground(new Color(i % 55, i * 13 % 45, i * 17 % 40));
			cols[i / numRows].add(words[i]);
		}
		frame.add(wordsPanel, BorderLayout.NORTH);

		field = new JTextField(30);
		field.addKeyListener(this);
		JButton giveUp = new JButton("Give up");
		giveUp.addActionListener(this);
		JPanel bottom = new JPanel(new GridLayout(1, 3));
		bottom.add(field);
		bottom.add(giveUp);
		frame.add(bottom, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((JButton) e.getSource()).getText().equals("Give up")) {
			for (int i = 0; i < words.length; i++)
				if (words[i].getForeground() != Color.WHITE)
					words[i].setForeground(Color.YELLOW);
		} else
			JOptionPane.showMessageDialog(frame, "No fucking idea what signal just got sent");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			String word = Util.clearPunc(field.getText().toLowerCase());
			if (song.contains(word)) {
				showWord(word);
				if (won()) {
					JOptionPane.showMessageDialog(frame, "Good job and you win");
				}
			}
			if (word.length() > 4 && word.substring(word.length() - 3).equals("ing")) {
				showWord(word.substring(0, word.length() - 1));
			}
			field.setText("");
		}
	}

	private void showWord(String word) {
		for (int i : song.locations(word))
			words[i].setForeground(Color.WHITE);
		song.removeTree(word);
	}

	private boolean won() {
		for (JLabel l : words)
			if (l.getForeground()!=Color.WHITE)
				return false;
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		return;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		return;
	}

}
