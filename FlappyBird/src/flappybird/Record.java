package flappybird;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Record
{
    private int hiScore;
    
    public void Save(int hi_score)//write
    {
        try
        {
            File HS = new File("D:\\Java Project\\FlappyBird\\hiScore.bin");
            FileOutputStream fos = new FileOutputStream(HS);
            
            fos.write(hi_score);
            fos.close();
        }
        
        catch(IOException e)
                {
                    System.out.println(e);
                }
    }
    
    public int Load()//read
    {
        try
        {
            File HS = new File("D:\\Java Project\\FlappyBird\\hiScore.bin");
            FileInputStream fis = new FileInputStream(HS);
            
            hiScore = fis.read();
            fis.close();
        }
        
        catch(IOException e)
                {
                    System.out.println(e);
                }
        
        return hiScore;
    }
}
