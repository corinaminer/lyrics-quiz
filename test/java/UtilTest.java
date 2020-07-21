import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

  private static final Path TEST_RESOURCES_PATH = Paths.get("", "test", "resources");

  @Test
  void testGetSongs_fileNotFound() {
    File notADirectory = TEST_RESOURCES_PATH.resolve("not a file").toFile();
    assertThrows(IllegalArgumentException.class, () -> Util.getSongs(notADirectory));
  }

  @Test
  void testGetSongs_notADirectory() {
    File notADirectory = TEST_RESOURCES_PATH.resolve("not_a_directory").toFile();
    assertThrows(IllegalArgumentException.class, () -> Util.getSongs(notADirectory));
  }

  @Test
  void testGetSongs_emptyDirectory() {
    File emptyDir = TEST_RESOURCES_PATH.resolve("empty").toFile();
    assert emptyDir.mkdir(); // should successfully create since it doesn't exist
    assertEquals(ImmutableList.of(), Util.getSongs(emptyDir));
    assert emptyDir.delete(); // should successfully delete since it's empty
  }

  @Test
  void testGetSongs() {
    File songsDir = TEST_RESOURCES_PATH.resolve("songs").toFile();
    assertEquals(ImmutableList.of(new Song(ImmutableList.of())), Util.getSongs(songsDir));
  }
}
