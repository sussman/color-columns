
/** The basic Columns game-wrapper class */


import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ColumnsApplet extends Applet
{
  
  public void start()
  {
    ColumnsGame the_game = new ColumnsGame(true);
  }
  
}



//////////////////////////////////////////////////////////
// Don't think, just hit `y':
//
// local variables:
// eval: (load-file "bkgomagic.el")
// end:
//////////////////////////////////////////////////////////
