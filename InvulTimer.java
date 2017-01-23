import java.util.Timer;
import java.util.TimerTask;

public class InvulTimer {
	private static int timerTick;
	
	private static Timer timer;
	
	public InvulTimer() {
		timer = new Timer();
		createTimer();
	}
	
	private static void createTimer() {
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			public void run() {
				timerTick++;
			}
			
		}, 0, 1000);
	}
	
	public static int getTimerTick() {
		return timerTick;
	}
	
	public static void resetTimer() {
		timer.cancel();
		timerTick = 0;
	}
}
