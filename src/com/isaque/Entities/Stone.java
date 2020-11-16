package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import java.awt.image.BufferedImage;


public class Stone extends Itens{
    
    private int ammo = 5;
    
    public Stone(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        setMask(4,5,8,7);
    }
    @Override
    public void Collected(){
        Game.player.giveAmmo(ammo);
        Sound.pickIten.play();
    }
    
}
