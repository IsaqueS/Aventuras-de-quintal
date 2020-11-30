package com.isaque.Entities;

import java.awt.Color;


public class Projectiles extends Entity{
    
    protected double dx, dy;
    protected int distance = 60, distanceCur = 0, damage = 1;
    protected double speed;
    public static Color color = new Color(128,35,35);
    
    //protected boolean isPlayerProjectiles;
    
    public Projectiles(int x, int y, int width, int height){
        super(x, y, width, height);
    }
    
    public int getDamage(){
        return this.damage;
    }
}
