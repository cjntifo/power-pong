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

//@SuppressWarnings("serial")
public class MiniTennis extends JPanel implements Runnable
{  
   Ball ball = new Ball(this);
   Racquet racquet = new Racquet(this);
   private static final int HEIGHT = 400;
   
   private static boolean running = false;
   private Thread thread;
   
   public static final int SLOW_COST = 150;
   public static final int BAR_COST = 200;
   
   private int cash;
   public int score;
   public int racquetSpeed = 1, ballSpeed = 1 ;  //int speed = 1;
   public boolean resizeActive, slowActive, barActive;
   
   private void init()
   {
      cash = 0;
      score = 0;
      
      resizeActive = false;
      slowActive = false;
      barActive = false;
   }
   
   public synchronized void start()
   {
      if(running)
         return;
         
      running = true;
      thread = new Thread(this);
      thread.start();
   }
   
   public void run()
   {
      System.out.println("Running");
      
      long lastTime = System.nanoTime();
      double amountOfTicks = 60.0;
      double ns = 1000000000 / amountOfTicks;
      double delta = 0;
      long timer = System.currentTimeMillis();
      int updates = 0;
      int frames = 0;
      
      while(running)
      {
         long now = System.nanoTime();
         delta += (now - lastTime) / ns;
         lastTime = now;
         while(delta >= 1)
         {
            move();
            updates++;
            delta--;
         }
         repaint();
         frames++;
         
         if(System.currentTimeMillis() - timer > 1000)
         {
            timer += 1000;
            System.out.println("FPS: " + frames + " TICKS: " + updates);
            frames = 0;
            updates = 0; 
         }
      }
   }
   
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
      return score;  //return (speed - 1);
   }
   
   
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
   
   public static void main(String[] args)
   {      
      Window window = new Window(300, 400, "Carl's Magical Power Pong", new MiniTennis());
   }
}