/**
 * Collaboration: - Looked up how to play sound - Looked up how to use a timer
 */

public class Game {
	private Grid grid;
	private int userRow;
	private int msElapsed;
	private int timesGet;
	private int timesAvoid;
	private float speed = 1.0f;
	private boolean invul;

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
			Grid.pause((int) (100 * speed));
			handleKeyPress();
			if (msElapsed % 300 == 0) {
				scrollLeft();
				populateRightEdge();
			}
			updateTitle();
			msElapsed += 100;

			if (invul && InvulTimer.getTimerTick() == 10) {
				grid.setImage(new Location(userRowLocation(), 0), "user.png");
				invul = false;
				InvulTimer.resetTimer();
			}
		}
		setGameOverScreen();
		Sound.playSound("gameover");
	}

	private void setGameOverScreen() {
		if (timesAvoid == 3) {
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 5; y++) {
					grid.setImage(new Location(y, x), "avoid.png");
				}
			}
			grid.setImage(new Location(2, 4), "gameover.png");
		} else {
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 5; y++) {
					grid.setImage(new Location(y, x), "get.png");
				}
			}
			grid.setImage(new Location(2, 4), "useri.png");
		}
	}

	private int userRowLocation() {
		for (int i = 0; i < 5; i++) {

			if (grid.getImage(new Location(i, 0)) != null && grid.getImage(new Location(i, 0)).equals(userImage()))
				return i;
		}
		return 0;
	}

	private void handleKeyPress() {

		int key = grid.checkLastKeyPressed();

		Location originalLoc = new Location(userRowLocation(), 0);
		
		String imgMovingInto;

		if (key == 38 && originalLoc.getRow() != 0) {
			
			Location newLoc = new Location(originalLoc.getRow() - 1, 0);
			imgMovingInto = grid.getImage(newLoc);

			if (grid.getImage(newLoc) != null)
				handleCollision(imgMovingInto, 1);

			grid.setImage(newLoc, userImage());
			grid.setImage(originalLoc, "");

		} else if (key == 40 && originalLoc.getRow() != 4) {

			Location newLoc = new Location(originalLoc.getRow() + 1, 0);
			imgMovingInto = grid.getImage(newLoc);

			if (grid.getImage(newLoc) != null)
				handleCollision(imgMovingInto, 1);

			grid.setImage(newLoc, userImage());
			grid.setImage(originalLoc, "");
		}
	}

	private void populateRightEdge() {
		Location loc = new Location((int) (Math.random() * 5), 9);

		double random = Math.random();

		if (random < 0.4) {
			grid.setImage(loc, "avoid.png");
		} else if (random < 0.96) {
			grid.setImage(loc, "get.png");
		} else if (random < 0.97) {
			grid.setImage(loc, "life.png");
		} else if (!invul) {
			grid.setImage(loc, "invul.png");
		} else {
			grid.setImage(loc, "get.png");
		}
	}

	private void scrollLeft() {
		String imgComingAtUser = grid.getImage(new Location(userRowLocation(), 1));
		
		// Columns
		for (int x = 1; x < 10; x++) {
			// Rows
			for (int y = 0; y < 5; y++) {
				Location currentLoc = new Location(y, x);
				Location nextLoc = new Location(y, x - 1);
				if (grid.getImage(currentLoc) != null
						&& (grid.getImage(nextLoc) == null || !grid.getImage(nextLoc).equals(userImage()))) {
					grid.setImage(nextLoc, grid.getImage(currentLoc));
					grid.setImage(currentLoc, "");
					
					
				}
			}
		}
		if (imgComingAtUser != null)
			handleCollision(imgComingAtUser, 0);
	}

	private void handleCollision(String img, int handle) {
		if (img.equals("get.png") || (img.equals("avoid.png") && invul)) {
			Sound.playSound("get");

			if (++timesGet == 100) {
				updateTitle();
				Sound.playSound("win");
				setGameOverScreen();
				return;
			}
			increaseSpeed();

		} else if (img.equals("avoid.png")) {

			if (handle == 0) {
				grid.setImage(new Location(this.userRowLocation(), 0), "avoid.png");
			} else {
				grid.setImage(new Location(this.userRowLocation(), 0), "");
			}

			Sound.playSound("avoid");
			timesAvoid++;
			Grid.pause(3000);
			resetScreen();
		} else if (img.equals("life.png")) {
			Sound.playSound("life");
			timesAvoid--;
		} else if (img.equals("invul.png")) {
			Sound.playSound("invul");
			grid.setImage(new Location(this.userRowLocation(), 0), "useri.png");
			invul = true;
			new InvulTimer();
		}
	}

	private int getScore() {
		return timesGet;
	}

	private void increaseSpeed() {
		speed -= 0.01f;
	}

	private void updateTitle() {
		if (timesAvoid == 3)
			grid.setTitle("Game Over! | Your Score was " + timesGet + " | Want To Try Again?");
		else if (timesGet == 100)
			grid.setTitle("Game Won! | You've Reached to 100 Points! | Want To Play Again?");
		else
			grid.setTitle("Can You Get to 100? | Current Score: " + getScore() + " | Lives: " + (3 - timesAvoid));
	}

	private boolean isGameOver() {
		return timesAvoid == 3 || timesGet >= 100;
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

	private String userImage() {
		if (invul)
			return "useri.png";
		return "user.png";
	}

	public static void main(String[] args) {
		new Game().play();
	}
}
