import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
class Song {

  @Nonnull private final List<String> _lyrics;
  @Nonnull private final Map<String, Set<Integer>> _map;

  Song(List<String> lyrics) {
    _lyrics = ImmutableList.copyOf(lyrics);
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

  @Nonnull
  Set<Integer> locations(String word) {
    if (!_map.containsKey(word)) {
      return ImmutableSet.of();
    }
    return _map.get(word);
  }

  void removeTree(String key) {
    _map.remove(key);
  }

  @Nonnull
  String getOriginalWord(int i) {
    return _lyrics.get(i);
  }

  int length() {
    return _lyrics.size();
  }
}
