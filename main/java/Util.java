import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
   * Returns all songs stored in file fname as an ArrayList of Song objects.
   *
   * @param fname File to draw songs from
   * @return all songs stored in file fname as an ArrayList of Song objects
   */
  @Nonnull
  static List<Song> getSongs(String fname) {
    Scanner sc = getScanner(fname);
    ImmutableList.Builder<Song> songs = ImmutableList.builder();
    while (sc.hasNextLine()) {
      songs.add(getNextSong(sc));
    }
    return songs.build();
  }

  /** Pulls lyrics from scanner until it hits a blank line or EOF. Returns new {@link Song}. */
  private static Song getNextSong(Scanner sc) {
    ImmutableList.Builder<String> lyrics = ImmutableList.builder();
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      if (line.equals("")) break;
      lyrics.addAll(Arrays.asList(line.trim().split("\\s+")));
    }
    return new Song(lyrics.build());
  }

  /** Returns a scanner for the given filename. Exits if file DNE. */
  private static Scanner getScanner(String fname) {
    try {
      return new Scanner(new File(fname));
    } catch (Exception e) {
      System.out.println("File " + fname + " not found. Bye");
      System.exit(0);
    }
    return null;
  }
}
