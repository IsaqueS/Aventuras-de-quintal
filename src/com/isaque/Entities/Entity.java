package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.maps.Camera;
import com.isaque.maps.Maps;
import com.isaque.maps.Node;
import com.isaque.maps.Vector2i;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Entity {
    
    public static final BufferedImage HAMBURGUER_SPRITE = Game.spritesheet.getSprite(16*6, 0, 16, 16);
    public static final BufferedImage WEAPON_SPRITE = Game.spritesheet.getSprite(16*7, 0, 16, 16);
    public static final BufferedImage WEAPON_MINI_SPRITE = Game.spritesheet.getSprite(16*7, 16, 16, 16);
    public static final BufferedImage STONE_SPRITE = Game.spritesheet.getSprite(16*8, 0, 16, 16);
    //public static final BufferedImage ENEMY_SPRITE = Game.spritesheet.getSprite(0, 16, 16, 16);
    
    protected int width, height;
    protected double x, y, speed;
    protected int maskX, maskY, maskW, maskH;
    protected BufferedImage sprite;
    protected List<Node> path;
    
    public Entity(double x, double y, int width, int height, BufferedImage sprite){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.maskX = 0;
        this.maskY = 0;
        this.maskW = 16;
        this.maskH = 16;
    }
    
    public Entity(double x, double y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
    }
    
    public int getX(){
        return (int)this.x;
    }
    
    public int getY(){
        return (int)this.y;
    }
    
    public double getAccurateX(){
        return x;
    }
    
    public double getAccurateY(){
        return y;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public void setX(double x){
        this.x = x;
    }
    
    public void setY(double y){
        this.y = y;
    }
    
    public void render(Graphics g){
        g.drawImage(sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
    }
    
    public void tick(){
        
    }
    
    public static boolean isColliding(Entity e1, Entity e2){
        Rectangle mask1 = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskW, e1.maskH);
        Rectangle mask2 = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskW, e2.maskH);
        return mask1.intersects(mask2);
    }
    protected void setMask(int maskX,int maskY, int maskW, int maskH){
        this.maskX = maskX;
        this.maskY = maskY;
        this.maskW = maskW;
        this.maskH = maskH;
    }
    
    protected void setMask(int maskW, int maskH){        
        this.maskW = maskW;
        this.maskH = maskH;
    }
    
    public int getYCenter(){
        return (getY() + maskY) + (maskH / 2);
    }
    public int getXCenter(){
        return (getX() + maskX) + (maskW / 2);
    }
    
    public void renderCollisionBox(Graphics g, Color color){
        g.setColor(color);
        g.drawRect(getX() + maskX - Camera.getX(), getY() + maskY -Camera.getY(), maskW -1, maskH -1);
    }
    
    public int getMaskX(){
        return this.maskX;
    }
    public int getMaskY(){
        return this.maskY;
    }
    public int getMaskW(){
        return this.maskW;
    }
    public int getMaskH(){
        return this.maskH;
    }
    
    public double canGo(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); 
    }
}

    
