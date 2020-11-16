package com.isaque.Entities;

import com.isaque.main.Sound;
import java.awt.image.BufferedImage;


public class Itens extends Entity{
    public Itens(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }
    public void Collected(){
        Sound.pickIten.play();
    }
}
