package com.isaque.maps;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Portal extends Tile{
    
    private final byte maxFrames, maxIndex;
    private byte frames, index;
    
    public Portal(int x, int y, BufferedImage[] sprites) {
        super(x, y, sprites);
        this.maxFrames = 16;
        this.maxIndex = (byte) (sprites.length -1);
    }
    @Override
    public void render (Graphics g){
        frames++;
            if (frames == maxFrames){
                frames = 0;
                index++;
                if (index > maxIndex){
                    index = 0;
                }
            }
        g.drawImage(sprites[index], x - Camera.getX(), y - Camera.getY(), null);
    }  
    
}
