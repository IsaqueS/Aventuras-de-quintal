package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import java.awt.image.BufferedImage;


public class Weapon extends Itens{
    
    public Weapon(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        setMask(1,0,14,16);
        this.depth = 1;
    }
    
    @Override
    public void Collected(){
        Game.player.weaponCollected();
        Sound.pickWeapon.play();
    }
}
