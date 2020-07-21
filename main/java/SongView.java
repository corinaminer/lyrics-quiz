import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class SongView extends JPanel implements ActionListener, KeyListener {
  private static final String GIVE_UP_BUTTON_TEXT = "Give up";

  @Nonnull private final JTextField _inputField;
  @Nonnull private final JLabel[] _words;
  @Nonnull private final Song _song;

  SongView(Song song) {
    _song = song;

    // Create interaction bar with input field and "give up" button
    _inputField = new JTextField(30);
    _inputField.addKeyListener(this);
    JButton giveUp = new JButton(GIVE_UP_BUTTON_TEXT);
    giveUp.addActionListener(this);
    JPanel interactionBar = new JPanel(new GridLayout(1, 2));
    interactionBar.add(_inputField);
    interactionBar.add(giveUp);

    // Create JLabels for each word in the song
    _words = new JLabel[_song.length()];
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
      _words[i] = new JLabel(" " + _song.getOriginalWord(i) + " ");
      _words[i].setOpaque(true);
      Color color = new Color(i % 55, i * 13 % 45, i * 17 % 40);
      _words[i].setBackground(color);
      _words[i].setForeground(color);
      cols[i / numRows].add(_words[i]);
    }

    setLayout(new BorderLayout());
    add(interactionBar, BorderLayout.NORTH);
    add(wordsPanel, BorderLayout.SOUTH);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String buttonText = ((JButton) e.getSource()).getText();
    if (buttonText.equals(GIVE_UP_BUTTON_TEXT)) {
      Arrays.stream(_words)
          .filter(w -> w.getForeground() != Color.WHITE)
          .forEach(w -> w.setForeground(Color.YELLOW));
    } else JOptionPane.showMessageDialog(this, "WHAT was THAT ~`.`~");
  }

  @Override
  public void keyReleased(KeyEvent e) {
    String word = Util.clearPunc(_inputField.getText().toLowerCase());
    if (_song.contains(word)) {
      showWord(word);
      _inputField.setText("");
      if (won()) {
        JOptionPane.showMessageDialog(this, "Good job and you win");
      }
    }
  }

  private void showWord(String word) {
    _song.locations(word).forEach(i -> _words[i].setForeground(Color.WHITE));
    _song.removeTree(word);
  }

  private boolean won() {
    return Arrays.stream(_words).allMatch(w -> w.getForeground() == Color.WHITE);
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}
}
