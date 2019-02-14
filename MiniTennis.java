import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//http://www.edu4java.com/en/game/game4.html
//https://codereview.stackexchange.com/questions/27197/pong-game-in-java/27211

//https://www.gamedesigning.org/learn/android/
//http://zetcode.com/tutorials/javagamestutorial/
//http://www.pvtuts.com/java/java-introduction
//http://gamecodeschool.com/android/building-a-simple-game-engine/

//https://github.com/m-gagne/limit.js

@SuppressWarnings("serial")
public class MiniTennis extends JPanel
{  
   Ball ball = new Ball(this);
   Racquet racquet = new Racquet(this);
   //int speed = 1;
   int racquetSpeed = 1;
   int ballSpeed = 1;
   int score = 0;
   private static final int HEIGHT = 400;
   static final int SLOW_COST = 150;
   static final int BAR_COST = 200;
   private int cash = 0;
   boolean resizeActive = false;
   boolean slowActive = false;
   boolean barActive = false;
   
   public int getBalance()
   {
      return cash;
   }
   
   public void getPaid(int amount)
   {
      cash += amount;
   }
   
   public void cashCheques(int cost)
   {
      cash -= cost;
      
      if(cash < SLOW_COST)
      {
         slowActive = false;
      }
      if(cash < BAR_COST)
      {
         barActive = false;
      }
   }
   
   private int getScore()
   {
      //return speed - 1;
      return score;
   }
   
   /*public int getHeight()
   {
      return HEIGHT;
   }*/
   
   public MiniTennis()
   {
      addKeyListener(new KeyListener()
      {
         @Override
         public void keyTyped(KeyEvent e)
         {
         
         }
         @Override
         public void keyPressed(KeyEvent e)
         {
            System.out.println("keyPressed=" +KeyEvent.getKeyText(e.getKeyCode()));
            racquet.keyPressed(e);
         }
         
         @Override
         public void keyReleased(KeyEvent e)
         {
            if(resizeActive && e.getKeyCode() == KeyEvent.VK_A)
            {
               resizeActive = false;
               racquet.resize(100);
            }
            
            if(slowActive && e.getKeyCode() == KeyEvent.VK_S)
            {
               slowActive = false;
               ballSpeed = 2;
               racquetSpeed = 2;
               cashCheques(SLOW_COST);
            }
            
            if(barActive && e.getKeyCode() == KeyEvent.VK_D)
            {
               barActive = false;
               racquet.activateBar();
               cashCheques(BAR_COST);
               ball.endBar = score + 1;
            }
                        
            System.out.println("keyReleased=" +KeyEvent.getKeyText(e.getKeyCode()));
            racquet.keyReleased(e);
         }
   
      });
      setFocusable(true);
      //Sound.BACK.loop();
   }
   
   private void move()
   {
      ball.move();
      racquet.move();
   }
   
   private AlphaComposite makeComposite(float alpha) 
   {
      int type = AlphaComposite.SRC_OVER;
      return(AlphaComposite.getInstance(type, alpha));
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
                              
      ball.paint(g2d);
      racquet.paint(g2d);
      
      g2d.setColor(Color.GRAY);
      g2d.setFont(new Font("Verdanda", Font.BOLD, 30));
      g2d.drawString(String.valueOf(getScore()), 10, 30);         
      
      Composite originalComposite = g2d.getComposite();
      g2d.setComposite(makeComposite(0.75f));
      
      g2d.drawString("A", 200, 30);
      g2d.drawString("S", 230, 30);
      g2d.drawString("D", 255, 30);
      
      g2d.drawString("£" + String.valueOf(getBalance()), 200, 70);
         
      g2d.setComposite(originalComposite);
      g2d.setColor(Color.RED);
      
      if(resizeActive)
         g2d.drawString("A", 200, 30);
      if(slowActive)
         g2d.drawString("S", 230, 30);
      if(barActive)
         g2d.drawString("D", 255, 30);
   }
   
   public void resizeRacquet(int size)
   {
      racquet.resize(size);
   }
   
   public void GameOver()
   {
      //Sound.BACK.stop();
      //Sound.GAMEOVER.play();
      JOptionPane.showMessageDialog(this, "Your Score is: " + getScore(), "Game Over", JOptionPane.YES_NO_OPTION);
      System.exit(ABORT);
   }
   
   public static void main(String[] args) throws InterruptedException
   {
      //Do something...
      JFrame frame = new JFrame("Mini Tennis");
      MiniTennis game = new MiniTennis();
            
      //Keyboard keyboard = new Keyboard();
      //frame.add(keyboard);
      
      frame.add(game);
      frame.setSize(300, 400);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      System.out.println(game.getHeight());
      
      while(true)
      {
         game.move();
         game.repaint();
         Thread.sleep(10);
      }
   }
}