public class Game {
	private Grid grid;
	private int userRow;
	private int msElapsed;
	private int timesGet;
	private int timesAvoid;
	private int level = 1;
	private boolean invulnerable;

	public Game() {
		grid = new Grid(5, 10);
		userRow = 0;
		msElapsed = 0;
		timesGet = 0;
		timesAvoid = 0;
		updateTitle();
		grid.setImage(new Location(userRow, 0), "user.png");
	}

	private void play() {
		while (!isGameOver()) {
			Grid.pause(75);
			handleKeyPress();
			if (msElapsed % 300 == 0) {
				scrollLeft();
				populateRightEdge();
			}
			updateTitle();
			msElapsed += 100;
		}
		grid.setImage(new Location(0, 0), "gameover.png");
		Sound.playSound("gameover.wav");
	}

	private int userRowLocation() {
		for (int i = 0; i < 5; i++) {

			if (grid.getImage(new Location(i, 0)).equals("user.png"))
				return i;
		}
		return 0;
	}

	private void handleKeyPress() {
		
		int key = grid.checkLastKeyPressed();
		
		Location originalLoc = new Location(userRowLocation(), 0);

		if (key == 38 && originalLoc.getRow() != 0) {
			Location newLoc = new Location(originalLoc.getRow() - 1, 0);

			if (grid.getImage(newLoc) != null)
				handleCollision(newLoc);

			grid.setImage(newLoc, "user.png");
			grid.setImage(originalLoc, "");

		} else if (key == 40 && originalLoc.getRow() != 4) {

			Location newLoc = new Location(originalLoc.getRow() + 1, 0);

			if (grid.getImage(newLoc) != null)
				handleCollision(newLoc);

			grid.setImage(newLoc, "user.png");
			grid.setImage(originalLoc, "");
		}
	}

	private void populateRightEdge() {
		Location loc = new Location((int) (Math.random() * 5), 9);
		
		double random = Math.random();

		if (random < 0.4) {
			grid.setImage(loc, "avoid.png");
		} else if (random < 0.98) {
			grid.setImage(loc, "get.png");
		} else if (random < 0.99){
			grid.setImage(loc, "life.png");
		} else {
			grid.setImage(loc, "invul.png");
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

				if (grid.getImage(currentLoc) != null) {
					grid.setImage(new Location(y, x - 1), grid.getImage(currentLoc));
					grid.setImage(currentLoc, "");
				}
			}
		}
	}

	private void handleCollision(Location loc) {
		
		if (grid.getImage(loc).equals("get.png") || (grid.getImage(loc).equals("avoid.png") && invulnerable)) {
			Sound.playSound("get.wav");
			timesGet++;
			updateLevel();
		} else if (grid.getImage(loc).equals("avoid.png")) {
			grid.setImage(loc, "");
			grid.setImage(new Location(this.userRowLocation(), 0), "avoid.png");
			Sound.playSound("avoid.wav");
			timesAvoid++;
			Grid.pause(3000);
			resetScreen();
		} else if (grid.getImage(loc).equals("life.png")) {
			Sound.playSound("life.wav");
			timesAvoid--;
			updateLevel();
		} else if (grid.getImage(loc).equals("invul.png")){
			Sound.playSound("invul.wav");
			invulnerable = true;
		}

		grid.setImage(loc, null);
	}

	private int getScore() {
		return timesGet;
	}
	
	private void updateLevel() {
		if (level == 1 && timesGet >= 50)
			level++;
	}

	private void updateTitle() {
		if (timesAvoid == 3)
			grid.setTitle("Game Over - Try Again");
		else
			grid.setTitle("Can You Get to 100? | Current Score: " + getScore() + " | Lives: " + (3 - timesAvoid));
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
		grid.setImage(new Location(0, 0), "user.png");
	}
}
