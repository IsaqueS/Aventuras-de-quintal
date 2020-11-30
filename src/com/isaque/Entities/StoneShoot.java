package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.maps.Camera;
import com.isaque.maps.Maps;
import java.awt.Color;
import java.awt.Graphics;


public class StoneShoot extends Projectiles{
    
    public Color color = new Color(35,35,35);
    
    public StoneShoot(int x, int y, int width, int height, double dx, double dy, int damage){
        super(x, y, width, height);
        
        this.dx = dx;
        this.dy = dy;
        this.speed = 4.0;
        this.distance = 80;
        this.damage = damage;
    }
    public StoneShoot(int x, int y, int width, int height, double dx, double dy, int damage, Color color, double speed){
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.distance = 80;
        this.damage = damage;
        this.color = color;
    }
    
    public void tick(){
        x+=dx*speed;
        y+=dy*speed;
        distanceCur++;
        if (distanceCur == distance){
            Game.playerProjectiles.remove(this);
            Game.enemyProjectiles.remove(this);
            return;
        }
        if (!Maps.isFree(getX(), getY(),0,0,width, height)){
            Game.playerProjectiles.remove(this);
            Game.enemyProjectiles.remove(this);
            return;
        }
    }
    
    public void render(Graphics g){
       g.setColor(color);
       g.fillOval(getX() - Camera.getX(), getY() - Camera.getY(), width, height);
    }
}
