import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.google.common.base.Preconditions.checkArgument;

@ParametersAreNonnullByDefault
class Util {
  /**
   * Returns a version of s with only the letters.
   *
   * @param s string to clear punctuation from
   * @return a version of s with only the letters
   */
  @Nonnull
  static String clearPunc(String s) {
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (Character.isLetter(c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Returns all songs stored in the provided directory as {@link Song} objects.
   *
   * @param songsDir Directory to draw songs from
   * @return All songs stored in directory {@code songsDir}
   */
  static List<Song> getSongs(@Nonnull File songsDir) {
    checkArgument(songsDir.isDirectory(), "Received non-directory file %s", songsDir.toString());
    File[] files = songsDir.listFiles();
    if (files == null) {
      System.out.println(
          String.format("Failed to list files in directory %s", songsDir.toString()));
      return ImmutableList.of();
    }

    ImmutableList.Builder<Song> songs = ImmutableList.builder();
    for (File f : files) {
      if (!f.canRead()) {
        System.out.println(String.format("Warning: Do not have permissions to read file %s", f.toString()));
        continue;
      }
      if (!f.isDirectory()) {
        try {
          songs.add(getNextSong(new Scanner(f)));
        } catch (FileNotFoundException e) {
          System.out.println(String.format("Unexpectedly could not find file %s", f.toString()));
        }
      }
    }
    return songs.build();
  }

  /** Pulls lyrics from scanner until it hits EOF. Returns new {@link Song}. */
  @Nonnull
  private static Song getNextSong(Scanner sc) {
    ImmutableList.Builder<String> lyrics = ImmutableList.builder();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (!line.isEmpty()) {
        lyrics.addAll(Arrays.asList(line.trim().split("\\s+")));
      }
    }
    return new Song(lyrics.build());
  }
}
