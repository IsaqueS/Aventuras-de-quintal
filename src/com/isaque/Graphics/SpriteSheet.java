package com.isaque.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class SpriteSheet {
    
    private BufferedImage spritesheet;
    private BufferedImage icon;
    
    public SpriteSheet(String pathSprite, String pathIcon){
        try {
            spritesheet = ImageIO.read(getClass().getResource(pathSprite));
            icon = ImageIO.read(getClass().getResource(pathIcon));
        } catch (IOException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getSprite(int x, int y, int width, int height){
        return spritesheet.getSubimage(x, y, width, height);
    }
    
    public BufferedImage getIcon(){
        return icon;
    }            
}
