package com.isaque.Entities;
import com.isaque.main.Game;
import com.isaque.main.Sound;
import com.isaque.maps.Camera;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Spider extends Enemies{
    
    private byte frames, index, maxIndex = 1, damageFrames;
    private final byte maxFrames = 5, maxDamageFrames = 5;
    private boolean moved = false;
    private byte dir;
    private static final short maxFindDistance = 144;
    private final byte dirUp = 0, dirDown = 1, dirLeft = 2, dirRight = 3;
    
    private static final BufferedImage[] spritesUp = {Game.spritesheet.getSprite(0, 32, 16, 16), Game.spritesheet.getSprite(16, 32, 16, 16)};
    private static final BufferedImage[] spritesDown = {Game.spritesheet.getSprite(0, 48, 16, 16), Game.spritesheet.getSprite(16, 48, 16, 16)};
    private static final BufferedImage[] spritesUpDamage = {Game.spritesheet.getSprite(0, 64, 16, 16), Game.spritesheet.getSprite(16, 64, 16, 16)};
    private static final BufferedImage[] spritesDownDamage = {Game.spritesheet.getSprite(0, 80, 16, 16), Game.spritesheet.getSprite(16, 80, 16, 16)};
    
    private static final BufferedImage[] spritesRight = {Game.spritesheet.getSprite(0, 112, 16, 16), Game.spritesheet.getSprite(16, 112, 16, 16)};
    private static final BufferedImage[] spritesLeft = {Game.spritesheet.getSprite(0, 96, 16, 16), Game.spritesheet.getSprite(16, 96, 16, 16)};
    private static final BufferedImage[] spritesRightDamage = {Game.spritesheet.getSprite(0, 144, 16, 16), Game.spritesheet.getSprite(16, 144, 16, 16)};    
    private static final BufferedImage[] spritesLeftDamage = {Game.spritesheet.getSprite(0, 128, 16, 16), Game.spritesheet.getSprite(16, 128, 16, 16)};
    
    private byte waitForFindPath = 0;
    private final int damageFix = 10, bonusRandom = 5;
    private int attackDelayMax = 60, attackDelay = 0;
    private boolean lastFrame = false;
    private double lastX, lastY;
    
    
    public Spider(double x, double y, int width, int height) {
        super(x, y, width, height);
        setMask(2,3,12,11);
        this.speed = 0.8;
        maxHP = 20;
        HP = maxHP;
        damage = damageFix;
        isDamage = false;
        dir = dirUp;
        this.depth = 2;
    }
    
    @Override
    public void tick(){
        if (lastFrame){
            dead();
        }
        
        moved = false;
        
        lastX = x;
        lastY = y;
        
        //if is not Colliding it will try to walk
        if (!isCollidingWithPlayer()){
        
        attackDelay = 0;
        
        //movement
        /*
        if (getX() < Game.player.getX() && canMove(x,y,true,false)){
            x+=speed;
            moved = true;
        } else if (getX() > Game.player.getX() && canMove(x,y,true,true)){
            moved = true;
            x-=speed;
        } if (getY() > Game.player.getY() && canMove(x,y,false,true)){
            y-=speed;
            moved = true;
            dir = dirUp;
        } else if (getY() < Game.player.getY() && canMove(x,y,false,false)){
            y+=speed;
            moved = true;
            dir = dirDown;
        }*/
        
        createPath(maxFindDistance);
        followPath(path, speed, 16);
        
        if (lastX != x || lastY != y) moved = true;
        if (lastX > x) dir = dirRight;
        if (lastX < x) dir = dirLeft;
        if (lastY > y) dir = dirUp;
        if (lastY < y) dir = dirDown;
        
        //movement
        if (moved){
            frames++;
            if (frames > maxFrames){
                index++;
                frames = 0;
                    if (index > maxIndex){
                    index = 0;
                    }
            }
        } else {
            index = 0;
            frames = 0;
        }
        //if is Collinding it will attack
        } else {
            if (attackDelay == 0){
                damage = damageFix;
                damage += Game.random.nextInt(bonusRandom);
                Game.player.damageHP(damage);
                Game.player.resetDamageAnimation();
                attackDelay++;
                Sound.hitt.play();
           
            } else {
                attackDelay++;
                if (attackDelay >= attackDelayMax) attackDelay = 0;
            }
        }
        
        collidingWithProjectiles(getX(), getY());
        
        if (isDamage){
           damageFrames++;
           if (damageFrames == maxDamageFrames){
               isDamage = false;
               damageFrames = 0;
           }
        } 
        if (isDamage){
           damageFrames++;
           if (damageFrames == maxDamageFrames){
               isDamage = false;
               damageFrames = 0;
           }
        }
        
        if (this.HP <= 0) {
            lastFrame = true;
        }
        
    }
    
    @Override
    public void render(Graphics g){
        if (!isDamage){
            if (dir == dirUp){
                g.drawImage(spritesUp[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null); 
            } else if (dir == dirDown){
                g.drawImage(spritesDown[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null); 
            } else if(dir == dirRight){
                g.drawImage(spritesRight[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            } else if (dir == dirLeft){
                g.drawImage(spritesLeft[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            }            
        } else {
            if (dir == dirUp){
                g.drawImage(spritesUpDamage[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            } else if (dir == dirDown){
                g.drawImage(spritesDownDamage[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null); 
            } else if(dir == dirRight){
                g.drawImage(spritesRightDamage[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            } else if (dir == dirLeft){
                g.drawImage(spritesLeftDamage[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            } 
             
        }
               
    }
    
    @Override
    protected void dead(){
        deleteSelf();
        return;
    }  
}
