
/** The basic Columns game-wrapper class */


import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class ColumnsGame extends Frame
{
  public ColumnsBoard board;   // abstract model of the gameboard 
  public ColumnsCanvas canvas; // draws the gameboard
  public ColumnsPiece piece;   // the piece which is falling at any given time
  public ColumnsClock dropclock;   // thread to drop the piece automatically
  public ColumnsClock waitclock;   // thread to wait X milliseconds only

  public int doublescore, score;
  Label scorelabel;

  public Color backcolor = new Color(249, 237, 255); 
  public boolean thread_safe_p = false;  // ok to have the clock drop a piece?
  public boolean wait_done_p = false;  // is our wait over?
  public boolean applet_p;

  /* Note: remember that when parsing a keystroke "down" event, if
     piece.move(ColumnsPiece.DOWN) returns false, be sure to call
     piece.die() and then create a new piece!  */
  
  public ColumnsGame(boolean applet_p)
  {
    this.applet_p = applet_p;

    board = new ColumnsBoard(this, 10, 20);
    canvas = new ColumnsCanvas(this);
    piece = new ColumnsPiece(this, 3);
    dropclock = new ColumnsClock(this, 750, true);
    waitclock = new ColumnsClock(this, 500, false);
    
    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	quit();
      }
    });

    scorelabel = new Label("Score: " + score);
    Panel scorepanel = new Panel();
    scorepanel.add(scorelabel);

    setTitle("Java Columns");
    setLayout(new BorderLayout());
    add(canvas, BorderLayout.CENTER);
    add(scorepanel, BorderLayout.SOUTH);
    pack();
    setVisible(true);
    setSize(200, 400);
    
    thread_safe_p = true;
    dropclock.start();
  }
  
  public void quit()
  {
    if (applet_p)
      this.dispose();
    else
      System.exit(0);
  }


  public void reset_game()
  {
    board = new ColumnsBoard(this, 10, 20);
    piece = new ColumnsPiece(this, 3);
    // change value of dropclock's delay?

    canvas.repaint();

    score = 0;
    doublescore = 0;
    thread_safe_p = true;
    dropclock.resume();
  }


  /** The tricky compensation here: Every matched color-sequence is
      counted *twice* by our search algorithm; hence our score is
      exactly half the number of hits our search reports! */

  public void up_score()
  {
    doublescore++;
    score = doublescore / 2;
    scorelabel.setText("Score: " + score);
  }
  

  /** What do to when game is over.  */

  public void game_over()
  {
    dropclock.suspend();

    Graphics g = canvas.getGraphics();
    g.setColor(Color.black);
    // set font better here.
    g.drawString("GAME OVER, MAN", 5, (canvas.y_incr * board.maxy / 2));

    waitclock.run();          // wait for the thread to set the boolean
    while (! wait_done_p) {}  // block on this boolean
    wait_done_p = false; // then reset the boolean

    reset_game();
  }

  public static void main(String args[]) 
  {
    ColumnsGame the_game = new ColumnsGame(false);    
  }
  
}



//////////////////////////////////////////////////////////
// Don't think, just hit `y':
//
// local variables:
// eval: (load-file "bkgomagic.el")
// end:
//////////////////////////////////////////////////////////
