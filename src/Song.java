import java.util.*;

class Song {

  private final List<String> _lyrics;
  private final Map<String, Set<Integer>> _map;
  private final Set<String> _wordsGuessed;

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
    _wordsGuessed = new HashSet<>();
  }

  boolean guessWord(String word) {
    String hygienic = Util.clearPunc(word.toLowerCase());
    return _map.containsKey(hygienic) && _wordsGuessed.add(hygienic);
  }

  Set<Integer> locations(String word) {
    if (!_map.containsKey(word)) {
      return new TreeSet<>();
    }
    return _map.get(word);
  }

  String getOriginalWord(int i) {
    return _lyrics.get(i);
  }

  int length() {
    return _lyrics.size();
  }

  void reset() {
    _wordsGuessed.clear();
  }
}
