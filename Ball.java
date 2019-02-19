import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Ball
{
   public static final int DIAMETER = 30;
   private int x = 0;
   private int y = 0;
   private int xa = 1;
   private int ya = 1;
   private MiniTennis game;
   public int endBar = -1;
   
   public Ball(MiniTennis game)
   {
      this.game = game;
   }
   
   public void move()
   {
      boolean changeDirection = true;
      
      if(x + xa < 0) //If ball collides with LEFTMOST border, bounce off the wall.
      {
         xa = game.ballSpeed;
      }
      else if(x + xa > game.getWidth() - DIAMETER) //If ball collides with RIGHTMOST border, bounce off the wall.
      {
         xa = -game.ballSpeed;
      }
      else if(y + ya < 0) //If ball collides with TOP border, bounce off the wall.
      {
         ya = game.ballSpeed;
      }
      else if(y + ya > game.getHeight() - DIAMETER) //If ball collides with BOTTOM border, GAMEOVER!
      {
         game.GameOver();
      }
      else if(collision()) //If ball collides with the Player's racquet, then handle game events.
      {
         ya = -game.ballSpeed;
         y = game.racquet.getTopY() - DIAMETER;
         
         game.racquetSpeed++;
         game.ballSpeed++;
         
         game.score++;
         game.getPaid(50 + game.score*5);
         System.out.println(game.getBalance());
         
         switch(game.score)
         {
            //Resize powerup is accesible after one hit...
            case 1:
               game.resizeActive = true;
               break;
            //Cases are expandable for later changes
            default:
               break;        
         }
         
         //If the player has enough money for a new PowerUp...
         if(game.getBalance() > game.SLOW_COST)
         {
            game.slowActive = true;
         }
         if(game.getBalance() > game.BAR_COST)
         {
            game.barActive = true;
         }
         
         //If BAR PowerUp is ended (endBar), resize racquet back to larger size...
         if(game.score == endBar)
         {
            game.resizeRacquet(100);
            endBar = -1;
         }
      }
      //If none of these event have occured, the ball has not changed direction...
      else
      {
         changeDirection = false;
      }
         
      if(changeDirection)
      {
         //Sound.BALL.play();
      }
      
      //Update the ball's coordinates with delta values (x, y) + (xa, ya)...
      x += xa;
      y += ya;
   }
   
   public boolean collision()
   {
      return game.racquet.getBounds().intersects(getBounds());
   }
   
   //@Override
   public void paint(Graphics2D g)
   {
      //super.paint(g);
      g.setColor(Color.BLUE);
      g.fillOval(x, y, 30, 30);
   }
   
   public Rectangle getBounds()
   {
      return new Rectangle(x, y, DIAMETER, DIAMETER);
   }
}
