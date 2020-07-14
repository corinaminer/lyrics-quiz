import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Util {
	/**
	 * Returns a version of s with only the letters.
	 * 
	 * @param s
	 *            string to clear punctuation from
	 * @return a version of s with only the letters
	 */
	public static String clearPunc(String s) {
		String s2 = "";
		for (char c : s.toCharArray()) {
			if ("abcdefghijklmnopqrstuvwxyz".contains("" + c)) {
				s2 += c;
			}
		}
		return s2;
	}

	/**
	 * Returns all songs stored in file fname as an ArrayList of Song objects.
	 * 
	 * @param fname
	 *            File to draw songs from
	 * @return all songs stored in file fname as an ArrayList of Song objects
	 */
	public static ArrayList<Song> getSongs(String fname) {
		Scanner sc = getScanner(fname);
		ArrayList<Song> songs = new ArrayList<Song>();
		while (sc.hasNextLine()) {
			songs.add(getNextSong(sc));
		}
		return songs;
	}

	/**
	 * Pulls lyrics from scanner until it hits a blank line or EOF.
	 * 
	 * @param sc
	 * @return New Song created from lyrics.
	 */
	private static Song getNextSong(Scanner sc) {
		ArrayList<String> lyrics = new ArrayList<String>();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.equals(""))
				break;
			for (String word : line.trim().split("\\s+")) {
				lyrics.add(word);
			}
		}
		return new Song(lyrics);
	}


	/**
	 * Returns a scanner for the given filename. Exits if file DNE.
	 * 
	 * @param fname
	 * @return scanner for fname
	 */
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
