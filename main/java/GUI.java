import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

@ParametersAreNonnullByDefault
public class GUI implements ActionListener {
  private static final String SELECT_SONGS_DIR_BUTTON_TEXT = "Select songs directory";

  @Nonnull private final Random _r = new Random();
  @Nonnull private JFileChooser _fileChooser;
  @Nullable private List<Song> _currentDirSongs;

  @Nonnull private final JFrame _frame;
  @Nonnull private final JPanel _noSongPanel;
  @Nullable private SongView _songView;

  GUI() {
    _frame = new JFrame();
    _frame.setResizable(true);
    _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    File currentPath = Paths.get("").toAbsolutePath().toFile();
    _fileChooser = new JFileChooser(currentPath);
    _fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    _noSongPanel = new JPanel(new BorderLayout());
    _noSongPanel.add(new JLabel("No songs directory selected."), BorderLayout.NORTH);
    JButton selectSongsDir = new JButton(SELECT_SONGS_DIR_BUTTON_TEXT);
    selectSongsDir.addActionListener(this);
    _noSongPanel.add(selectSongsDir, BorderLayout.SOUTH);
  }

  void showNoSongsDirSelectedView() {
    if (_songView != null) {
      _frame.remove(_songView);
    }
    _frame.add(_noSongPanel);
    _frame.pack();
    _frame.setVisible(true);
  }

  /**
   * Causes file selector popup to open, allowing user to choose a songs source directory. If the
   * user does so, inits a song from the new directory.
   */
  void changeSongsSourceDirectory() {
    File songsDir = getSongsSourceDirectory();
    if (songsDir == null) {
      // User hit cancel; ignore that indecisive moron
      return;
    }
    List<Song> songs = Util.getSongs(songsDir);
    if (!songs.isEmpty()) {
      _currentDirSongs = Util.getSongs(songsDir);
      selectNewSong();
    } else {
      JOptionPane.showMessageDialog(_frame, "No song files in the selected directory.");
    }
  }

  /**
   * Pops up a file selection menu. Returns selected {@link File} (guaranteed to be a directory), or
   * {@code null} if no file was selected (i.e. user hit Cancel, or an error occurred).
   */
  private File getSongsSourceDirectory() {
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

  void selectNewSong() {
    checkState(_currentDirSongs != null && !_currentDirSongs.isEmpty(), "Cannot select song");
    initSongView(_currentDirSongs.get(_r.nextInt(_currentDirSongs.size())));
  }

  private void initSongView(Song s) {
    _frame.remove(_noSongPanel);
    if (_songView != null) {
      _frame.remove(_songView);
    }
    _songView = new SongView(s, this);
    _frame.add(_songView);
    _frame.pack();
    _frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String buttonText = ((JButton) e.getSource()).getText();
    if (buttonText.equals(SELECT_SONGS_DIR_BUTTON_TEXT)) {
      changeSongsSourceDirectory();
    } else JOptionPane.showMessageDialog(_frame, "WHAT was THAT ~`.`~");
  }
}
