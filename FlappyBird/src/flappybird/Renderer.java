package flappybird;

import java.awt.Graphics;
import javax.swing.JPanel;


public class Renderer extends JPanel
{

    @Override
    protected void paintComponent(Graphics grphcs) 
    {
        super.paintComponent(grphcs); 
        
        FlappyBird.flappyBird.repaint(grphcs);
    }
    
   
}
