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

	private void play() {
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
		
		Sound.playSound("gameover.wav");
	}

	private int userRowLocation() {
		for (int i = 0; i < 5; i++) {

			if (grid.getImage(new Location(i, 0)).equals("user.gif"))
				return i;
		}
		return 0;
	}

	private void handleKeyPress() {
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

	private void populateRightEdge() {
		Location loc = new Location((int) (Math.random() * 5), 9);

		if (Math.random() < 0.4) {
			grid.setImage(loc, "avoid.gif");
		} else {
			grid.setImage(loc, "get.gif");
		}
	}

	private void scrollLeft() {
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

	private void handleCollision(Location loc) {
		if (grid.getImage(loc).equals("get.gif")) {
			Sound.playSound("get.wav");
			timesGet++;
		} else if (grid.getImage(loc).equals("avoid.gif")) {
			grid.setImage(loc, "");
			grid.setImage(new Location(this.userRowLocation(), 0), "avoid.gif");
			Sound.playSound("avoid.wav");
			timesAvoid++;
			grid.pause(3000);
			resetScreen();
		}

		grid.setImage(loc, null);
	}

	private int getScore() {
		return timesGet;
	}

	private void updateTitle() {
		if (timesAvoid == 3)
			grid.setTitle("Game Over - Try Again");
		else
			grid.setTitle("Scrolling Game | Current Score: " + getScore() + "| Lives: " + (3 - timesAvoid));
	}

	private boolean isGameOver() {
		return timesAvoid == 3;
	}

	public static void main(String[] args) {
		new Game().play();
	}
	
	private void resetScreen() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 5; y++) {

				if (grid.getImage(new Location(y, x)) != null) {
					grid.setImage(new Location(y, x), "");
				}
			}
		}
		if (timesAvoid != 3)
			grid.setImage(new Location(0, 0), "user.gif");
		else
			grid.setImage(new Location(0, 0), "gameover.gif");
	}
}
