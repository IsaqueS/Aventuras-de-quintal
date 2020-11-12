package com.isaque.Entities;

import com.isaque.main.Game;
import java.awt.image.BufferedImage;


public class Weapon extends Itens{
    
    public Weapon(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        setMask(1,0,14,16);
    }
    
    @Override
    public void Collected(){
        Game.player.weaponCollected();
    }
}
