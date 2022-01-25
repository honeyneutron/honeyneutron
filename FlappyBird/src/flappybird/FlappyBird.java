package flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

 public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
    public static FlappyBird flappyBird;
    
    public final int WIDTH = 800, HEIGHT = 700;//window size
    
    public Renderer renderer;//for double buffering
    
    public SFX sfx;//Sound effects
    
    public Rectangle bird;
    
    public boolean gameOver, started = false; 
    
    public Random random;
    
    public ArrayList<Rectangle> columns;
    
    public int ticks, yMotion, score, hiScore;
    
    int collide = 1; //for hit sound to play on only first collision
    
    public FlappyBird()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        sfx = new SFX();
        
        random = new Random();
        Record r = new Record();
        
        jframe.add(renderer);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setTitle("Flappy Bird - Java Editon");
        jframe.setResizable(false);
        jframe.setVisible(true);
        
        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<>();
        
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        
        hiScore = r.Load();
        
        timer.start();
    }
    
    private void addColumn(boolean start)
    {
        int space = 300, width = 100;
        int height = 50+random.nextInt(300);
        if (start)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height -120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else
        {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }
    
    public void paintColumn(Graphics g, Rectangle column)
    {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y,column.width , column.height);
    }
    
    public void flap()
    {
        if (gameOver)
        {
            //Reset all values
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;
            collide = 1;
        
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            
            gameOver = false;
        }
        if (!started)
        {
            started = true;
        }
        else if (started && !gameOver)
        {
            if (yMotion > 0)
            {
                yMotion = 0;
            }
            
            yMotion -= 10;//fly up
            sfx.flapSFX();//play flap sound
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        ticks++;
        if (started)
        {
            
            int speed = 5;//game speed

            for(int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);
                column.x -= speed;//moving columns from right to left
            }

            if (ticks % 2 == 0 && yMotion < 15)
            {
                yMotion += 2;
            }

            for(int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);

                if (column.x + column.width < 0)
                {
                    columns.remove(column);

                    if (column.y == 0)
                    {
                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;//bird falls


            for(Rectangle column : columns)
            {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - speed && bird.x + bird.width / 2 < column.x + column.width / 2 + speed)
                {
                    if(!gameOver)
                    {
                        score++;//in between the column
                        sfx.pointSFX();//play score sound
                    
                        if(score > hiScore)
                        {
                            hiScore = score;
                        }
                        
                        Record r = new Record();
                        r.Save(hiScore);//saving hi-score
                    }  
                }
                
                if (column.intersects(bird) )//adding collision to bird
                {
                    while(collide == 1)//play hit sound only once
                    {
                        sfx.hitSFX();
                        collide++;
                    }
                    gameOver = true;
                    
                    if (bird.x <= column.x)
                    {
                        bird.x = column.x - bird.width;
                    }
                    else
                    {
                        if (column.y != 0)
                        {
                            bird.y = column.y - bird.height;
                        }
                        else if (bird.y < column.height)
                        {
                            bird.y = column.height;
                        }
                    }
                }
            }
            
//if bird goes off screen(top) or falls down
            if (bird.y > HEIGHT - 120 || bird.y < 0) 
            {
                while(collide == 1)//play hit sound only once
                {
                    sfx.hitSFX();
                    collide++;
                }
                gameOver = true;
            }
            
            if (bird.y + yMotion >= HEIGHT - 120)
            {
                while(collide == 1)//play hit sound only once
                {
                    sfx.hitSFX();
                    collide++;
                }
                
                bird.y = HEIGHT - 120 - bird.height;
                
                gameOver = true;
            }
        }
        renderer.repaint();
    }
    
    Map<?,?> desktophints = (Map<?,?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
    
    public void repaint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        /*g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);*/
        
        if(desktophints != null){ //better solution
            g2d.setRenderingHints(desktophints);
        }
                
        g2d.setColor(Color.cyan);//background
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        g2d.setColor(Color.orange);//ground
        g2d.fillRect(0, HEIGHT - 120, WIDTH, 120);
        g2d.setColor(Color.green);//grass
        g2d.fillRect(0, HEIGHT - 120, WIDTH, 20);
        
        g2d.setColor(Color.red);//bird
        g2d.fillRect(bird.x, bird.y, bird.width, bird.height);
        
        for(Rectangle column : columns)//re-painting columns
        {
            paintColumn(g2d, column);
        }
        //displaying text messages
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Calibri", 1, 100));
        
        if (!started)
        {
            g2d.drawString("Flappy Bird", 150, HEIGHT / 2 - 50);
            
            g2d.setFont(new Font("Calibri", 1, 35));
            g2d.drawString("Click to Start!", 270, HEIGHT / 2 + 200);
        }
        
        if (gameOver)
        {
            g2d.drawString("Game Over", 150, HEIGHT / 2 - 75);
            
            g2d.setFont(new Font("Calibri", 1, 45));
            
            g2d.drawString("Score", 230, HEIGHT / 2 + 25);
            g2d.drawString(String.valueOf(score), 510, HEIGHT / 2 + 25);
            
            g2d.setFont(new Font("Calibri", 1, 45));
            
            g2d.drawString("Hi-Score", 230, HEIGHT / 2 + 75);
            
            g2d.setColor(Color.red);
            
            g2d.drawString(String.valueOf(hiScore), 510, HEIGHT / 2 + 75);
            
            g2d.setColor(Color.black);
            g2d.setFont(new Font("Calibri", 1, 35));
            
            g2d.drawString("Click to Start!", 270, HEIGHT / 2 + 200);
        }
        
        if(!gameOver && started)
        {
            g2d.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }
            
    public static void main(String[] args) 
    {
        flappyBird = new FlappyBird();//calling constructor
    }
//overriden methods
    @Override
    public void mouseClicked(MouseEvent me) 
    {
        flap();
    }

    @Override
    public void mouseExited(MouseEvent me) 
    {
    }

    @Override
    public void mouseEntered(MouseEvent me) 
    {
    }

    @Override
    public void mousePressed(MouseEvent me) 
    {
    }

    @Override
    public void mouseReleased(MouseEvent me) 
    {
    }

    @Override
    public void keyReleased(KeyEvent ke) 
    {
        if (ke.getKeyCode() == KeyEvent.VK_SPACE)
        {
            flap();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent ke) 
    {
    }

    @Override
    public void keyTyped(KeyEvent ke) 
    {
    }
}
