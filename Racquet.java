import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Racquet
{
   public static final int Y = 330;
   //public static final int WIDTH = 60;
   public static final int HEIGHT = 10;
   public int width = 60;
   int x = 0;
   int xa = 0;
   private MiniTennis game;
   
   public Racquet(MiniTennis game)
   {
      this.game = game;
   }
   
   public void move()
   {
      if(x + xa > 0 && x + xa < game.getWidth() - width)
      {
         x += xa;
      }
   }
   
   public void resize(int altWidth)
   {
      if(x + altWidth > game.getWidth())
      {
         x = game.getWidth() - altWidth;
      }
      this.width = altWidth;
   }
   
   public void activateBar()
   {
      x = 0;
      this.width = game.getWidth();
   }
   
   //@Override   
   public void paint(Graphics2D g)
   {
      //super.paint(g);
      g.fillRect( x, 330, width, HEIGHT);
   }
   
   public void keyReleased(KeyEvent e)
   {
      xa = 0;
   }
   
   public void keyPressed(KeyEvent e)
   {
      if(e.getKeyCode() == KeyEvent.VK_LEFT) //move left VK_A
         xa = -game.racquetSpeed;
      if(e.getKeyCode() == KeyEvent.VK_RIGHT) // move right VK_D
         xa = game.racquetSpeed;
   }
   
   public Rectangle getBounds()
   {
      return new Rectangle(x, Y, width, HEIGHT);
   }
   
   public int getTopY()
   {
      return Y;
   }
}