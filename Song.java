import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Song {

	private ArrayList<String> lyrics;
	private HashMap<String, TreeSet<Integer>> map;

	public Song(ArrayList<String> lyrics) {
		this.lyrics = lyrics;
		map = new HashMap<String, TreeSet<Integer>>();
		for (int i = 0; i < lyrics.size(); i++) {
			String word = Util.clearPunc(lyrics.get(i).toLowerCase());
			if (map.containsKey(word)) {
				map.get(word).add(i);
			} else {
				TreeSet<Integer> tree = new TreeSet<Integer>();
				tree.add(i);
				map.put(word, tree);
			}
		}
	}

	public boolean contains(String word) {
		return map.containsKey(Util.clearPunc(word.toLowerCase()));
	}

	public TreeSet<Integer> locations(String word) {
		if (!map.containsKey(word)) {
			return new TreeSet<Integer>();
		}
		return map.get(word);
	}

	public void removeTree(String key) {
		map.remove(key);
	}

	public String getOriginalWord(int i) {
		return lyrics.get(i);
	}

	public int length() {
		return lyrics.size();
	}



}
