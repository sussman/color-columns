
/** The abstract model of our gameboard, rendered by ColumnsCanvas.

    Its main work in life is to clear rows of color.

 */


import java.awt.*;


public class ColumnsBoard 
{
  ColumnsGame game; 
  public Color[][] matrix;   // dynamically sized 2D array of colors
  public boolean[][] mark_matrix;  // used to mark squares for deletion
  public int maxx, maxy;

  public boolean need_to_delete_p;
  
  public static final int MAXDEPTH = 2;  // how many colors-in-a-row to recognize
  
  public static final int Up = 0;
  public static final int UpRight = 1;
  public static final int Right = 2;
  public static final int DownRight = 3;
  public static final int Down = 4;
  public static final int DownLeft = 5;
  public static final int Left = 6;
  public static final int UpLeft = 7;
  
  
  /** Constructor:  allocate 2D array */
  
  public ColumnsBoard(ColumnsGame game, int sizex, int sizey)
  {
    this.game = game;
    this.maxx = sizex;
    this.maxy = sizey;
    matrix = new Color[maxx][maxy];
    mark_matrix = new boolean[maxx][maxy];
    
    for (int i = 0; i < maxx; i++)
      for (int j = 0; j < maxy; j++)
	matrix[i][j] = game.backcolor;
  }
  
  
  /** Test a certain matrix square for a certain color */
  
  boolean isColor(int x, int y, Color testcolor)
  {
    if ((x >= maxx) || (x < 0) || (y >= maxy) || (y < 0))
      return false;
    if (matrix[x][y].equals(testcolor))
      return true;
    else
      return false;
  }
  
  
  /** Entry routine to begin looking for matching color sequences */
  
  public void clear_sequences()
  {
    int x, y;
    Color color;
    
    while (true) {
      for (y = (maxy - 1); y >= 0; y--) {
	for (x = 0; x < maxx; x++) {
	  color = matrix[x][y];
	  if (color != game.backcolor) {
	    search(0, x, y, color, Up);
	    search(0, x, y, color, UpRight);
	    search(0, x, y, color, Right);
	    search(0, x, y, color, DownRight);
	    search(0, x, y, color, Down);
	    search(0, x, y, color, DownLeft);
	    search(0, x, y, color, Left);
	    search(0, x, y, color, UpLeft);
	  }
	}
      }
      if (! delete_blocks()) {
	need_to_delete_p = false;
	break;
      }
    } 
  }
  
  
  
  /** Look through our boolean mark_matrix[][], and delete any marked blocks;
      return whether we deleted any blocks or not.  */
  
  boolean delete_blocks()
  {
    int x, y, count;
    boolean deleted_p = false;

    if (! need_to_delete_p) return false;

    // wait a specific amount of time first, via waitclock thread:
    game.waitclock.run();          // wait for the thread to set the boolean
    while (! game.wait_done_p) {}  // block on this boolean
    game.wait_done_p = false; // then reset the boolean

    for (x = 0; x < maxx; x++) {   // traverse down each column
      for (y = 0; y < maxy; y++) {
	
	if (mark_matrix[x][y]) {    // if we find a marked piece
	  for (count = y; count > 0; count--) {  // shift-copy stuff down
	    matrix[x][count] = matrix[x][count-1];
	  }
	  matrix[x][0] = game.backcolor;
	  deleted_p = true;
	}
	
      }
    }
    // and reset our mark_matrix to all false again
    for (int i = 0; i < maxx; i++)
      for (int j = 0; j < maxy; j++)
	mark_matrix[i][j] = false;
    
    return deleted_p;
  }
  
  
  /** Recursive tree search algorithm around a single matrix square;
   If sequences are found, mark the blocks to be deleted in
   mark_matrix.*/
  
  boolean search(int depth, int x, int y, Color searchcolor, int direction)
  {
    if ((x < 0) || (x >= maxx) || (y < 0) || (y >= maxy)) return false;

    if (! (isColor(x, y, searchcolor))) {
      return false;
    }
    
    if (depth == MAXDEPTH) {
      mark_matrix[x][y] = true;
      game.up_score();
      need_to_delete_p = true;
      game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
    }
    
    switch (direction) {
    case Up: {
      if (search(depth + 1, x, y - 1, searchcolor, Up)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case UpRight: {
      if (search(depth + 1, x + 1, y - 1, searchcolor, UpRight)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case Right: {
      if (search(depth + 1, x + 1, y, searchcolor, Right)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case DownRight: {
      if (search(depth + 1, x + 1, y + 1, searchcolor, DownRight)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_3Dblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case Down: {
      if (search(depth + 1, x, y + 1, searchcolor, Down)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case DownLeft: {
      if (search(depth + 1, x - 1, y + 1, searchcolor, DownLeft)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case Left: {
      if (search(depth + 1, x - 1, y, searchcolor, Left)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    case UpLeft: {
      if (search(depth + 1, x - 1, y - 1, searchcolor, UpLeft)) {
	mark_matrix[x][y] = true;
	game.canvas.plot_outlineblock(x, y, Color.black);
	return true;
      }
      else
	return false;
    }
    }
    return false;    
  }
  
  
}



//////////////////////////////////////////////////////////
// Don't think, just hit `y':
//
// local variables:
// eval: (load-file "bkgomagic.el")
// end:
//////////////////////////////////////////////////////////
