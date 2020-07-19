import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Util {
  /**
   * Returns a version of s with only the letters.
   *
   * @param s string to clear punctuation from
   * @return a version of s with only the letters
   */
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
  static List<Song> getSongs(String fname) {
    Scanner sc = getScanner(fname);
    List<Song> songs = new ArrayList<>();
    while (sc.hasNextLine()) {
      songs.add(getNextSong(sc));
    }
    return songs;
  }

  /** Pulls lyrics from scanner until it hits a blank line or EOF. Returns new {@link Song}. */
  private static Song getNextSong(Scanner sc) {
    List<String> lyrics = new ArrayList<>();
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      if (line.equals("")) break;
      lyrics.addAll(Arrays.asList(line.trim().split("\\s+")));
    }
    return new Song(lyrics);
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
