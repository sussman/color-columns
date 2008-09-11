/** Subclass of java.awt.canvas which draws a ColumnsBoard */

import java.io.*;
import java.awt.*;
import java.awt.event.*;


public class ColumnsCanvas extends Canvas
{
  ColumnsGame game;
  int x_incr, y_incr;
  Graphics g;
  
  
  /** Constructor */
  
  public ColumnsCanvas(ColumnsGame game)
  {
    this.super();
    this.game = game;

    Rectangle r = getBounds();
    x_incr = r.width / game.board.maxx;
    y_incr = r.height / game.board.maxy;
    
    setBackground(game.backcolor);
    
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
	int key = e.getKeyCode();
	handle_keystroke(key);
      }
    });
    
  }
  
  void handle_keystroke(int key)
  {
    game.thread_safe_p = false;  // stop our clock while processing

    if (key == KeyEvent.VK_UP) {
      game.piece.rotate(ColumnsPiece.UP);
    }
    if (key == KeyEvent.VK_LEFT) {
      game.piece.move(ColumnsPiece.LEFT);
    }
    if (key == KeyEvent.VK_RIGHT) {
      game.piece.move(ColumnsPiece.RIGHT);
    }
    if (key == KeyEvent.VK_DOWN) {
      game.piece.rotate(ColumnsPiece.DOWN);
    }
    if (key == KeyEvent.VK_SPACE) {
      game.piece.drop();
    }
    game.thread_safe_p = true; // restart our clock again
  }
  
  
  /** Standard overridden canvas routine:
      redraw whole canvas, based on ColumnsBoard model.
      
      I assume this is called automatically whenever the Container is
      resized!  */
  
  
  public void paint(Graphics g)
  {
    game.thread_safe_p = false;

    super.paint(g);
    
    Rectangle r = getBounds();
    
    int x = game.board.maxx;
    int y = game.board.maxy;

    // calculate grid-size variables from board size
    x_incr = r.width / x;
    y_incr = r.height / y;

    g.setColor(Color.black);
    g.drawLine(0, (y_incr * y), (x_incr * x), (y_incr * y));
    
    for (int j = 0; j < game.board.maxy; j++) {
      for (int i = 0; i < game.board.maxx; i++) {
	if (game.board.matrix[i][j] != game.backcolor)
	  plot_3Dblock(i, j, game.board.matrix[i][j]);
      }
    }
    
    game.piece.draw();

    game.thread_safe_p = true;
  }
  
  
  /** Plot an individual block WITHOUT redrawing whole board!  

      Note that x, y input are in *board* coordinates, not pixels.

      In the future, this might render an image instead!  */
  
  public void plot_3Dblock(int x, int y, Color color)
  {
    if (y < 0) return;
    
    g = this.getGraphics();
    g.setColor(color);
    g.fill3DRect((x * x_incr), (y * y_incr), x_incr, y_incr, true);
  }
  
  public void plot_outlineblock(int x, int y, Color color)
  {
    g = this.getGraphics();
    g.setColor(color);
    g.setColor(Color.black);
    g.drawRect((x * x_incr), (y * y_incr), x_incr, y_incr);
  }

  public void plot_block(int x, int y, Color color)
  {
    if (y < 0) return;

    g = this.getGraphics();
    g.setColor(color);
    g.fillRect((x * x_incr), (y * y_incr), x_incr, y_incr);
  }
  
}



//////////////////////////////////////////////////////////
// Don't think, just hit `y':
//
// local variables:
// eval: (load-file "bkgomagic.el")
// end:
//////////////////////////////////////////////////////////
