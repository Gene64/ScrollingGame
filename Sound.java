import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
	public static void playSound(final String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					URL fileURL = new URL("file:///" + url);
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(fileURL);
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}