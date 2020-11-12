package com.isaque.maps;

import java.awt.image.BufferedImage;


public class Portal extends Tile{
    
    public Portal(int x, int y, BufferedImage sprite) {
        super(x, y, sprite);
    }
    
    public Portal(int x, int y) {
        super(x, y);
    }
    
    public Portal (int x, int y, BufferedImage[] sprites) {
        super(x, y, sprites);
    }
    
}
