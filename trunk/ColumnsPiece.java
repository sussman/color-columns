

/**  ColumnsPiece.java:  the basic falling block of color */


import java.util.*;
import java.awt.*;


public class ColumnsPiece
{
  ColumnsGame game;
  int x, y;  // coordinate of bottom block
  Color[] colors;  // array of colored blocks, variable size
  int length;  // the length of our array
  
  public static final int DOWN = 0; // directional constants
  public static final int UP = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 3;
  
  
  /** Constructor */
  
  public ColumnsPiece(ColumnsGame game, int length)
  {
    this.game = game;
    this.length = length;

    // allocate Color[size] randomly, avoiding backcolor (?)
    colors = new Color[length];
    
    re_init();
  }
  
  
  
  public void re_init()
  {
    float foo, bar;
    Random rand = new Random();
    
    bar = rand.nextFloat();
    x = (int) (bar * game.board.maxx);
    y = 0;
    
    for (int i = 0; i < length; i++) {
      foo = rand.nextFloat();
      if (foo < .125)
	colors[i] = Color.red;  // red
      else if (foo < .25)
	colors[i] = Color.blue;  // blue
      else if (foo < .375)
	colors[i] = Color.green; // green
      else if (foo < .5)
	colors[i] = Color.lightGray; // gray
      else if (foo < .625)
	colors[i] = Color.magenta; // purple
      else if (foo < .75)
	colors[i] = Color.yellow; // yellow
      else if (foo < .875)
	colors[i] = Color.pink; // pink
      else
	colors[i] = Color.cyan; // cyan
    }      
  }
  
  /** Draw myself:
      1.  on the board
      2.  directly on the canvas, for smoother animation */
  
  public void draw()
  {
    for (int i = 0; i < length; i++) {
      if ((y-i) >= 0) {
	game.board.matrix[x][y-i] = colors[i];
	game.canvas.plot_3Dblock(x, y - i, colors[i]);
      }
    }
  }
  
  /** Undraw myself -- use game.backcolor */
  
  public void undraw()
  {
    for (int i = 0; i < length; i++) {
      if ((y-i) >= 0) {
	game.board.matrix[x][y-i] = game.backcolor;
	game.canvas.plot_block(x, y - i, game.backcolor);
      }
    }
  }
  
  /** When we die, we become part of the matrix[][] and start over at
      the top of the board.  :)*/
  
  public void die()
  {
    game.thread_safe_p = false;
    draw();

    // copy self into board.matrix[][]
    for (int count = 0; count < length; count++) {
      if ((y-count) < 0) {
	game.game_over();
	return;
      }
      game.board.matrix[x][y - count] = colors[count];
    }

    // redraw our board
    game.canvas.repaint();
    
    // look for matches
    game.board.clear_sequences();
    game.canvas.repaint();
    
    re_init();
    draw();
    game.thread_safe_p = true;
  }
  
  /** Cycle my array of colors */
  
  public void rotate(int direction)
  {
    undraw();
    
    if (direction == ColumnsPiece.DOWN) {
      Color last_color = colors[0];
      for (int i = 1;  i < length; i++) 
	colors[i-1] = colors[i];
      colors[length-1] = last_color;
    }

    if (direction == ColumnsPiece.UP) {
      Color last_color = colors[length-1];
      for (int i = length-2;  i >= 0; i--) 
	colors[i+1] = colors[i];
      colors[0] = last_color;
    }
    
    draw();
  }
  
  
  /** Keepmoving down until move() returns false. :)  */
  
  public void drop()
  {
    while(move(ColumnsPiece.DOWN)) {
      // do nothing. :)
    }
  }
  
  /** Check for collisions, then change our coordinates if OK.
      Returns success or failure. */
  
  public boolean move(int direction)
  {
    // first, remove the piece from the board
    undraw();
    
    if (direction == ColumnsPiece.LEFT) {
      int testx = x - 1;
      if ((testx < 0)) { draw(); return false;}
      for (int count = 0; count < length; count++) {
	int testy = y - count;
	if (testy >= 0) {
	  if (game.board.matrix[testx][testy] 
	      != game.backcolor) { // if collision
	    draw();
	    return false; // then never mind
	  }
	}
      } 
      // Otherwise, we actually move the piece (its bottom coordinate):
      x -= 1;
      draw();
      return true;
    }
    
    else if (direction == ColumnsPiece.RIGHT) {
      int testx = x + 1;
      if ((testx >= game.board.maxx)) { draw(); return false;}
      for (int count = 0; count < length; count++) {
	int testy = y - count;
	if (testy >= 0) {
	  if (game.board.matrix[testx][testy] 
	    != game.backcolor) { // if collision
	    draw();
	    return false; // then never mind
	  }
	}
      } 
      // Otherwise, we actually move the piece (its bottom coordinate):
      x += 1;
      draw();
      return true;
    }
    
    if (direction == ColumnsPiece.DOWN) {
      int testx = x;
      for (int count = 0; count < length; count++) {
	int testy = y - count + 1;
	if (testy >= 0) {
	  if ((testy >= game.board.maxy) ||
	      (game.board.matrix[testx][testy] != game.backcolor))
	  { 
	    // Then we MERGE INTO the MATRIX!
	    die();
	    return false;
	  }
	}
      } 
      // Otherwise, we actually move the piece (its bottom coordinate):
      y += 1;
      draw();
      return true;
    }
    
    return false;
  }
  
  

  /** Return an ascii representation of our color array */
  
  public String toString()
  {
    StringBuffer str = new StringBuffer();
    str.append("Columns Piece: coords: " + x + ", " + y + ", Color Array: ");
    for (int i = 0; i < length; i++) {
      str.append(colors[i].toString());
    }
    return str.toString();
  }
  
}



//////////////////////////////////////////////////////////
// Don't think, just hit `y':
//
// local variables:
// eval: (load-file "bkgomagic.el")
// end:
//////////////////////////////////////////////////////////
