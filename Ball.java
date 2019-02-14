import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Ball
{
   public static final int DIAMETER = 30;
   int x = 0;
   int y = 0;
   int xa = 1;
   int ya = 1;
   private MiniTennis game;
   int endBar = -1;
   
   public Ball(MiniTennis game)
   {
      this.game = game;
   }
   
   public void move()
   {
      boolean changeDirection = true;
      if(x + xa < 0)
         xa = game.ballSpeed;
      else if(x + xa > game.getWidth() - DIAMETER)
         xa = -game.ballSpeed;
      else if(y + ya < 0)
         ya = game.ballSpeed;
      else if(y + ya > game.getHeight() - DIAMETER)
         game.GameOver(); //ya = -1;
      else if(collision())
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
            case 1:
               game.resizeActive = true;
               break;
            case 3:
               //game.slowActive = true;
               break;
            case 5:
               //game.barActive = true;
               break;
            /*case endBar:          //Constant Expression Required...
               game.resizeRacquet(100);
               break;*/
            default:
               //Do nothing...
               break;        
         }
         
         int balance = game.getBalance();
         
         if(balance > game.SLOW_COST)
         {
            game.slowActive = true;
         }
         if(balance > game.BAR_COST)
         {
            game.barActive = true;
         }
         
         if(game.score == endBar)
         {
            game.resizeRacquet(100);
            endBar = -1;
         }
      }
      else
         changeDirection = false;
         
      if(changeDirection)
      {
         //Sound.BALL.play();
      }
      
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
