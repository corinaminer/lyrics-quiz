import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class GUI implements ActionListener, KeyListener {

  private final JFrame _frame;
  private final JTextField _inputField;
  private final WordLabel[] _words;
  private final Song _song;

  GUI(Song s) {
    _song = s;

    _frame = new JFrame();
    _frame.setResizable(true);
    _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create interaction bar with input field and "give up" button
    _inputField = new JTextField(30);
    _inputField.addKeyListener(this);
    JButton giveUp = new JButton("Give up");
    JButton reset = new JButton("Reset");
    giveUp.addActionListener(this);
    reset.addActionListener(this);
    JPanel buttons = new JPanel(new GridLayout(1, 2));
    buttons.add(giveUp);
    buttons.add(reset);
    JPanel interactionBar = new JPanel(new GridLayout(1, 2));
    interactionBar.add(_inputField);
    interactionBar.add(buttons);

    // Create WordLabels for each word in the song
    _words = new WordLabel[_song.length()];
    int numCols = 14;
    int numRows = _words.length / numCols;
    if (_words.length % numCols != 0) numRows++;
    JPanel wordsPanel = new JPanel(new GridLayout(1, numCols));
    JPanel[] cols = new JPanel[numCols];
    for (int i = 0; i < numCols; i++) {
      cols[i] = new JPanel(new GridLayout(numRows, 1));
      wordsPanel.add(cols[i]);
    }
    for (int i = 0; i < _words.length; i++) {
      WordLabel wordLabel =
          new WordLabel(_song.getOriginalWord(i), new Color(i % 55, i * 13 % 45, i * 17 % 40));
      _words[i] = wordLabel;
      cols[i / numRows].add(wordLabel);
    }

    _frame.add(interactionBar, BorderLayout.NORTH);
    _frame.add(wordsPanel, BorderLayout.SOUTH);
    _frame.pack();
    _frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String buttonText = ((JButton) e.getSource()).getText();
    if (buttonText.equals("Give up")) {
      Arrays.stream(_words)
          .filter(w -> w.getForeground() != Color.WHITE)
          .forEach(w -> w.setForeground(Color.YELLOW));
    } else if (buttonText.equals("Reset")) {
      reset();
    } else JOptionPane.showMessageDialog(_frame, "WHAT was THAT ~`.`~");
  }

  private void reset() {
    _song.reset();
    Arrays.stream(_words).forEach(WordLabel::reset);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    String word = Util.clearPunc(_inputField.getText().toLowerCase());
    if (_song.guessWord(word)) {
      showWord(word);
      _inputField.setText("");
      if (won()) {
        JOptionPane.showMessageDialog(_frame, "Good job and you win");
      }
    }
  }

  private void showWord(String word) {
    _song.locations(word).forEach(i -> _words[i].setForeground(Color.WHITE));
  }

  private boolean won() {
    return Arrays.stream(_words).allMatch(w -> w.getForeground() == Color.WHITE);
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}
}
