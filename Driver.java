import java.util.ArrayList;
import java.util.Random;

public class Driver {

	public static void main(String[] args) {
		System.out.println(37 / 6);
		ArrayList<Song> songs = Util.getSongs("lyrics.txt");
		Random r = new Random();
		GUI gui = new GUI(songs.get(r.nextInt(songs.size())));
	}

}
