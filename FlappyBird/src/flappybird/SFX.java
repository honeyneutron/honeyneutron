package flappybird;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class SFX 
{
    public void flapSFX()
    {
        //String flap = "D:/Java Project/FlappyBird/sound/sfx_wing.wav";
        
        try 
        {
            AudioInputStream aS = AudioSystem.getAudioInputStream(new File("D:/Java Project/FlappyBird/sound/sfx_wing.wav").getAbsoluteFile());
            
            Clip clip = AudioSystem.getClip();
            clip.open(aS);
            clip.start();
        } 
        catch (Exception ex) 
        {
            System.out.println("Error with playing sound.");
        }
    }
    
    public void pointSFX()
    {
        //String point = "D:/Java Project/FlappyBird/sound/sfx_point.wav";
        
        try 
        {
            AudioInputStream aS = AudioSystem.getAudioInputStream(new File("D:/Java Project/FlappyBird/sound/sfx_point.wav").getAbsoluteFile());
            
            Clip clip = AudioSystem.getClip();
            clip.open(aS);
            clip.start();
        } 
        catch (Exception ex) 
        {
            System.out.println("Error with playing sound.");
        }
    }
    
    public void hitSFX()
    {
        //String hit = "D:/Java Project/FlappyBird/sound/sfx_hit.wav";
        
        try 
        {
            AudioInputStream aS = AudioSystem.getAudioInputStream(new File("D:/Java Project/FlappyBird/sound/sfx_hit.wav").getAbsoluteFile());
            
            Clip clip = AudioSystem.getClip();
            clip.open(aS);
            clip.start();
        } 
        catch (Exception ex) 
        {
            System.out.println("Error with playing sound.");
        }
    }
    
}
