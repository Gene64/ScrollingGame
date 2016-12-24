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
  
  public void handleKeyPress()
  {
	  int key = grid.checkLastKeyPressed();
	  
	  if (key == 38) {
		  // user moves up
	  } else if (key == 40) {
		  // user moves down
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