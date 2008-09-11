
/** A separate thread which periodically drops the ColumnsPiece */


import java.io.*;


public class ColumnsClock extends Thread
{
  ColumnsGame game;   // the game I'm controlling
  public long delay;  // how long to pause, in milliseconds
  boolean drop_p;     // are we expected to drop the piece?

  /** Constructor */
  
  public ColumnsClock(ColumnsGame game, long delay, boolean drop_p)
  {
    this.game = game;
    this.delay = delay;
    this.drop_p = drop_p;
  }
    
  
  /** Main routine of thread: 

      If this clock is a "dropper", it periodically drops the piece.

      Otherwise, it just delays a fixed amount and returns.

  */
  
  public void run()
  {
      if (drop_p) {
	while(true)
	{
	  try {
	    sleep(delay);
	    if (game.thread_safe_p)
	      game.piece.move(ColumnsPiece.DOWN);
	  }
	  catch (InterruptedException e) {
	    System.err.println("Clock error: " + e);
	  }
	}
      }

      else // just delay and set a variable when I return
      {
	try {
	  sleep(delay);
	  game.wait_done_p = true;
	}
	catch (InterruptedException e) {
	  System.err.println("Clock error: " + e);
	}
      }
  }
  
}




//////////////////////////////////////////////////////////
// Don't think, just hit `y':
//
// local variables:
// eval: (load-file "bkgomagic.el")
// end:
//////////////////////////////////////////////////////////
