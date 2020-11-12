package com.isaque.Entities;

import com.isaque.main.Game;
import java.awt.image.BufferedImage;


public class Hamburguer extends Itens{
    
    private static int HP;
    
    public Hamburguer(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        HP = 50;
        setMask(4,4,8,8);
    }
    
    @Override
    public void Collected(){
        Game.player.healHP(getHP());
    }
    
    public static int getHP(){
        return HP;
    }
}
