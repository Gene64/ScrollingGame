import java.io.File;

public class Game {
	private Grid grid;
	private int userRow;
	private int msElapsed;
	private int timesGet;
	private int timesAvoid;

	public Game() {
		grid = new Grid(5, 10);
		userRow = 0;
		msElapsed = 0;
		timesGet = 0;
		timesAvoid = 0;
		updateTitle();
		grid.setImage(new Location(userRow, 0), "user.gif");
	}

	public void play() {
		while (!isGameOver()) {
			grid.pause(75);
			handleKeyPress();
			if (msElapsed % 300 == 0) {
				scrollLeft();
				populateRightEdge();
			}
			updateTitle();
			msElapsed += 100;
		}
	}

	public int userRowLocation() {
		for (int i = 0; i < 5; i++) {

			if (grid.getImage(new Location(i, 0)).equals("user.gif"))
				return i;
		}
		return 0;
	}

	public void handleKeyPress() {
		int key = grid.checkLastKeyPressed();
		Location originalLoc = new Location(userRowLocation(), 0);

		if (key == 38) {
			if (originalLoc.getRow() == 0)
				return;

			Location newLoc = new Location(originalLoc.getRow() - 1, 0);

			if (grid.getImage(newLoc) != null)
				handleCollision(newLoc);

			grid.setImage(newLoc, "user.gif");
			grid.setImage(originalLoc, "");

		} else if (key == 40) {
			if (originalLoc.getRow() == 4)
				return;

			Location newLoc = new Location(originalLoc.getRow() + 1, 0);

			if (grid.getImage(newLoc) != null)
				handleCollision(newLoc);

			grid.setImage(newLoc, "user.gif");
			grid.setImage(originalLoc, "");
		}
	}

	public void populateRightEdge() {
		Location loc = new Location((int) (Math.random() * 5), 9);

		if (Math.random() < 0.4) {
			grid.setImage(loc, "avoid.gif");
		} else {
			grid.setImage(loc, "get.gif");
		}
	}

	public void scrollLeft() {
		if (grid.getImage(new Location(userRowLocation(), 1)) != null)
			handleCollision(new Location(userRowLocation(), 1));

		// Columns
		for (int x = 1; x < 10; x++) {
			// Rows
			for (int y = 0; y < 5; y++) {
				Location currentLoc = new Location(y, x);
				Location moveLeftLoc = new Location(y, x - 1);

				if (grid.getImage(currentLoc) != null) {
					grid.setImage(moveLeftLoc, grid.getImage(currentLoc));
					grid.setImage(currentLoc, "");
				}
			}
		}
	}

	public void handleCollision(Location loc) {
		if (grid.getImage(loc).equals("get.gif")) {
			timesGet++;
			
			Sound.playSound(System.getProperty("user.dir") + File.separator + "get.wav");
			
		} else if (grid.getImage(loc).equals("avoid.gif")) {
			timesAvoid++;
		}

		grid.setImage(loc, null);
	}

	public int getScore() {
		return timesGet;
	}

	public void updateTitle() {
		if (timesAvoid == 3)
			grid.setTitle("Game Over - Try Again");
		else
			grid.setTitle("Scrolling Game | Current Score: " + getScore() + "| Lives: " + (3 - timesAvoid));
	}

	public boolean isGameOver() {
		return timesAvoid == 3;
	}

	public static void main(String[] args) {
		new Game().play();
	}
}
