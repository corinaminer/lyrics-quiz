import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
class Song {
  @Nonnull private final List<String> _lyrics;
  @Nonnull private final Map<String, Set<Integer>> _map;

  Song(List<String> lyrics) {
    _lyrics = ImmutableList.copyOf(lyrics);
    Map<String, ImmutableSet.Builder<Integer>> lyricsMap = new HashMap<>();
    for (int i = 0; i < lyrics.size(); i++) {
      String word = Util.clearPunc(lyrics.get(i).toLowerCase());
      lyricsMap.computeIfAbsent(word, k -> ImmutableSet.builder()).add(i);
    }
    _map =
        lyricsMap.entrySet().stream()
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> e.getValue().build()));
  }

  boolean contains(String word) {
    return _map.containsKey(Util.clearPunc(word.toLowerCase()));
  }

  @Nonnull
  Set<Integer> locations(String word) {
    return _map.getOrDefault(word, ImmutableSet.of());
  }

  @Nonnull
  String getOriginalWord(int i) {
    return _lyrics.get(i);
  }

  int length() {
    return _lyrics.size();
  }

  /* equals and hashcode implemented for testing purposes only, and ignore _map since it is derived from lyrics */

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Song)) {
      return false;
    }
    Song o = (Song) obj;
    return _lyrics.equals(o._lyrics);
  }

  @Override
  public int hashCode() {
    return _lyrics.hashCode();
  }
}
