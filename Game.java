import java.util.Random;

public class Game
{
  private Grid grid;
  private int userRow;
  private int msElapsed;
  private int timesGet;
  private int timesAvoid;
  
  public Game()
  {
    grid = new Grid(5, 10);
    userRow = 0;
    msElapsed = 0;
    timesGet = 0;
    timesAvoid = 0;
    updateTitle();
    grid.setImage(new Location(userRow, 0), "user.gif");
  }
  
  public void play()
  {
    while (!isGameOver())
    {
      grid.pause(100);
      handleKeyPress();
      if (msElapsed % 300 == 0)
      {
        scrollLeft();
        populateRightEdge();
      }
      updateTitle();
      msElapsed += 100;
    }
  }
  
  public int userRowLocation() {
	  for (int i = 0; i < 5; i++) {
		  Location loc = new Location(i, 0);
		  
		  if (grid.getImage(loc).equals("user.gif"))
			  return i;
	  }
	  return 0;
  }
  
  public void handleKeyPress()
  {
	  int key = grid.checkLastKeyPressed();
	  Location originalLoc = new Location(userRowLocation(), 0);
	  
	  if (key == 38) {
		  if (originalLoc.getRow() == 0)
			  return;
		  
		  Location newLoc = new Location(originalLoc.getRow() - 1, 0);
		  grid.setImage(newLoc, "user.gif");
		  grid.setImage(originalLoc, "");
		  
	  } else if (key == 40) {
		  if (originalLoc.getRow() == 4)
			  return;
		  
		  Location newLoc = new Location(originalLoc.getRow() + 1, 0);
		  grid.setImage(newLoc, "user.gif");
		  grid.setImage(originalLoc, "");
	  }
  }
  
  public void populateRightEdge()
  {
	Location loc = new Location((int)(Math.random() * 5), 9);
	  
	if (Math.random() < 0.5) {
		grid.setImage(loc, "avoid.gif");
	} else {
		grid.setImage(loc, "get.gif");
	}
  }
  
  public void scrollLeft()
  {
	  String currentImg;
	  for (int x = 1; x < 9; x++) {
		  for (int y = 0; y < 4; y++) {
			  Location loc = new Location(y, x);
			  currentImg = grid.getImage(loc);
			  Location loc1 = new Location(y, x - 1);
			  //if (!currentImg.equals("user.gif"))
				//  grid.setImage(loc1, currentImg);
		  }
	  }
  }
  
  public void handleCollision(Location loc)
  {
  }
  
  public int getScore()
  {
    return 0;
  }
  
  public void updateTitle()
  {
    grid.setTitle("Scrolling Game | Current Score: " + getScore());
  }
  
  public boolean isGameOver()
  {
    return false;
  }
  
  public static void main(String[] args)
  {
	  Game game = new Game();
	  game.play();
  }
}