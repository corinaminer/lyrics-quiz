import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class SongView extends JPanel implements ActionListener, KeyListener {
  private static final String GIVE_UP_BUTTON_TEXT = "Give up";
  private static final String NEW_SONG_BUTTON_TEXT = "New song";
  private static final String CHANGE_SOURCE_BUTTON_TEXT = "Change source";

  @Nonnull private final GUI _gui;
  @Nonnull private final JTextField _inputField;
  @Nonnull private final JLabel[] _wordLabels;

  @Nonnull private final Song _song;
  @Nonnull private final Set<String> _wordsGuessed = new HashSet<>();

  private boolean _gaveUp = false;

  SongView(Song song, GUI gui) {
    _song = song;
    _gui = gui;

    // Create input field
    _inputField = new JTextField(30);
    _inputField.addKeyListener(this);

    // Create buttons and JPanel to hold them
    JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
    Stream.of(GIVE_UP_BUTTON_TEXT, NEW_SONG_BUTTON_TEXT, CHANGE_SOURCE_BUTTON_TEXT)
        .forEach(
            buttonText -> {
              JButton button = new JButton(buttonText);
              button.addActionListener(this);
              buttonsPanel.add(button);
            });

    JPanel interactionBar = new JPanel(new GridLayout(1, 2));
    interactionBar.add(_inputField);
    interactionBar.add(buttonsPanel);

    // Create JLabels for each word in the song
    _wordLabels = new JLabel[_song.length()];
    int numCols = 14;
    int numRows = _wordLabels.length / numCols;
    if (_wordLabels.length % numCols != 0) numRows++;
    JPanel wordsPanel = new JPanel(new GridLayout(1, numCols));
    JPanel[] cols = new JPanel[numCols];
    for (int i = 0; i < numCols; i++) {
      cols[i] = new JPanel(new GridLayout(numRows, 1));
      wordsPanel.add(cols[i]);
    }
    for (int i = 0; i < _wordLabels.length; i++) {
      _wordLabels[i] = new JLabel(" " + _song.getOriginalWord(i) + " ");
      _wordLabels[i].setOpaque(true);
      Color color = new Color(i % 55, i * 13 % 45, i * 17 % 40);
      _wordLabels[i].setBackground(color);
      _wordLabels[i].setForeground(color);
      cols[i / numRows].add(_wordLabels[i]);
    }

    setLayout(new BorderLayout());
    add(interactionBar, BorderLayout.NORTH);
    add(wordsPanel, BorderLayout.SOUTH);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String buttonText = ((JButton) e.getSource()).getText();
    switch (buttonText) {
      case GIVE_UP_BUTTON_TEXT:
        _gaveUp = true;
        Arrays.stream(_wordLabels)
            .filter(w -> w.getForeground() != Color.WHITE)
            .forEach(w -> w.setForeground(Color.YELLOW));
        break;
      case NEW_SONG_BUTTON_TEXT:
        _gui.selectNewSong();
        break;
      case CHANGE_SOURCE_BUTTON_TEXT:
        _gui.changeSongsSourceDirectory();
        break;
      default:
        JOptionPane.showMessageDialog(this, "WHAT was THAT ~`.`~");
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (_gaveUp) {
      return; // Ignore all input field activity if the user gave up
    }
    String word = Util.clearPunc(_inputField.getText().toLowerCase());
    if (_song.contains(word) && _wordsGuessed.add(word)) {
      _song.locations(word).forEach(i -> _wordLabels[i].setForeground(Color.WHITE));
      _inputField.setText("");
      if (won()) {
        JOptionPane.showMessageDialog(this, "Good job and you win");
      }
    }
  }

  private boolean won() {
    return Arrays.stream(_wordLabels).allMatch(w -> w.getForeground() == Color.WHITE);
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}
}
