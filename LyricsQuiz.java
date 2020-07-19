import java.util.List;
import java.util.Random;

public class LyricsQuiz {

  public static void main(String[] args) {
    List<Song> songs = Util.getSongs("lyrics.txt");
    Random r = new Random();
    new GUI(songs.get(r.nextInt(songs.size())));
  }
}
