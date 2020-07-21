import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

@ParametersAreNonnullByDefault
public class GUI implements ActionListener, KeyListener {
  private static final String GIVE_UP_BUTTON_TEXT = "Give up";
  private static final String SELECT_SONGS_DIR_BUTTON_TEXT = "Select songs directory";

  @Nonnull private JFileChooser _fileChooser;

  @Nonnull private final JFrame _frame;
  @Nullable private JTextField _inputField;
  @Nullable private JLabel[] _words;
  @Nullable private Song _song;

  @Nonnull private final JPanel _noSongPanel;
  @Nullable private JPanel _songPanel;

  GUI() {
    _frame = new JFrame();
    _frame.setResizable(true);
    _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    File currentPath = Paths.get("").toAbsolutePath().toFile();
    _fileChooser = new JFileChooser();
    _fileChooser.setCurrentDirectory(currentPath);
    _fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    _noSongPanel = new JPanel(new BorderLayout());
    _noSongPanel.add(new JLabel("No songs directory selected."), BorderLayout.NORTH);
    JButton selectSongsDir = new JButton(SELECT_SONGS_DIR_BUTTON_TEXT);
    selectSongsDir.addActionListener(this);
    _noSongPanel.add(selectSongsDir, BorderLayout.SOUTH);
  }

  void showNoSongsDirSelectedView() {
    if (_songPanel != null) {
      _frame.remove(_songPanel);
    }
    _frame.add(_noSongPanel);
    _frame.pack();
    _frame.setVisible(true);
  }

  /**
   * Pops up a file selection menu. Returns selected {@link File} (guaranteed to be a directory), or
   * {@code null} if no file was selected (i.e. user hit Cancel, or an error occurred).
   */
  File getSongsSourceDirectory() {
    int result = _fileChooser.showOpenDialog(_frame);
    switch (result) {
      case JFileChooser.CANCEL_OPTION:
        return null;
      case JFileChooser.APPROVE_OPTION:
        File selected = _fileChooser.getSelectedFile();
        checkState(selected != null && selected.isDirectory(), "Failed to select");
        return selected;
      default:
        JOptionPane.showMessageDialog(_frame, "(!) An error occurred, please try again :/");
        return null;
    }
  }

  void setup(Song s) {
    _song = s;

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

    _songPanel = new JPanel(new BorderLayout());
    _songPanel.add(interactionBar, BorderLayout.NORTH);
    _songPanel.add(wordsPanel, BorderLayout.SOUTH);

    _frame.remove(_noSongPanel);
    _frame.add(_songPanel);
    _frame.pack();
    _frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String buttonText = ((JButton) e.getSource()).getText();
    if (buttonText.equals(GIVE_UP_BUTTON_TEXT)) {
      Arrays.stream(_words)
          .filter(w -> w.getForeground() != Color.WHITE)
          .forEach(w -> w.setForeground(Color.YELLOW));
    } else if (buttonText.equals(SELECT_SONGS_DIR_BUTTON_TEXT)) {
      File songsDir = getSongsSourceDirectory();
      if (songsDir == null) {
        // User hit cancel; ignore that indecisive moron
        return;
      }
      List<Song> songs = Util.getSongs(songsDir);
      if (songs.isEmpty()) {
        JOptionPane.showMessageDialog(_frame, "No song files in the selected directory.");
        return;
      }
      Random r = new Random();
      setup(songs.get(r.nextInt(songs.size())));
    } else JOptionPane.showMessageDialog(_frame, "WHAT was THAT ~`.`~");
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (_inputField == null) {
      return;
    }
    String word = Util.clearPunc(_inputField.getText().toLowerCase());
    if (_song.contains(word)) {
      showWord(word);
      _inputField.setText("");
      if (won()) {
        JOptionPane.showMessageDialog(_frame, "Good job and you win");
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
