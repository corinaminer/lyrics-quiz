import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

@ParametersAreNonnullByDefault
public class SourceSelector {
  @Nonnull private JFileChooser _fileChooser;
  @Nonnull private final JFrame _frame;

  SourceSelector(JFrame frame) {
    _frame = frame;

    File currentPath = Paths.get("").toAbsolutePath().toFile();
    _fileChooser = new JFileChooser(currentPath);
    _fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  }

  /**
   * Causes file selector popup to open, allowing user to choose a songs source directory. Returns a
   * list of the songs in the selected directory, or null if no directory was selected or the
   * selected directory has no songs.
   */
  @Nullable
  List<Song> selectDirectoryAndGetSongs() {
    File songsDir = getSongsSourceDirectory();
    if (songsDir == null) {
      // User hit cancel; ignore that indecisive moron
      return null;
    }
    List<Song> songs = Util.getSongs(songsDir);
    if (songs.isEmpty()) {
      JOptionPane.showMessageDialog(_frame, "No song files in the selected directory.");
      return null;
    }
    return songs;
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
}
