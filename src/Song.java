import java.util.*;

class Song {

  private final List<String> _lyrics;
  private final Map<String, Set<Integer>> _map;

  Song(List<String> lyrics) {
    _lyrics = lyrics;
    _map = new HashMap<>();
    for (int i = 0; i < lyrics.size(); i++) {
      String word = Util.clearPunc(lyrics.get(i).toLowerCase());
      if (_map.containsKey(word)) {
        _map.get(word).add(i);
      } else {
        TreeSet<Integer> tree = new TreeSet<>();
        tree.add(i);
        _map.put(word, tree);
      }
    }
  }

  boolean contains(String word) {
    return _map.containsKey(Util.clearPunc(word.toLowerCase()));
  }

  Set<Integer> locations(String word) {
    if (!_map.containsKey(word)) {
      return new TreeSet<>();
    }
    return _map.get(word);
  }

  void removeTree(String key) {
    _map.remove(key);
  }

  String getOriginalWord(int i) {
    return _lyrics.get(i);
  }

  int length() {
    return _lyrics.size();
  }
}
