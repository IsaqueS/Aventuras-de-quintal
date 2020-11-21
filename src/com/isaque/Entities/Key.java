package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import java.awt.image.BufferedImage;

public class Key extends Itens{
    
    public Key(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        setMask(4,4,8,8);
        this.depth = 1;
    }
    
    @Override
    public void Collected(){
        Sound.pickIten.play();
        Game.player.hasKey = true;
    }
    
}
