package com.isaque.Entities;
import com.isaque.main.Game;
import com.isaque.maps.Camera;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Spider extends Enemies{
    
    private byte frames, index, maxIndex = 1, damageFrames;
    private final byte maxFrames = 5, maxDamageFrames = 5;
    private boolean moved = false;
    private boolean dir;
    private final boolean dirUp = true, dirDown = false;
    private static final BufferedImage[] spritesUp = {Game.spritesheet.getSprite(0, 32, 16, 16), Game.spritesheet.getSprite(16, 32, 16, 16)};
    private static final BufferedImage[] spritesDown = {Game.spritesheet.getSprite(0, 48, 16, 16), Game.spritesheet.getSprite(16, 48, 16, 16)};
    private static final BufferedImage[] spritesUpDamage = {Game.spritesheet.getSprite(0, 64, 16, 16), Game.spritesheet.getSprite(16, 64, 16, 16)};
    private static final BufferedImage[] spritesDownDamage = {Game.spritesheet.getSprite(0, 80, 16, 16), Game.spritesheet.getSprite(16, 80, 16, 16)};
    
    private final int damageFix = 10, bonusRandom = 5;
    private int attackDelayMax = 60, attackDelay = 0;
    
    
    public Spider(double x, double y, int width, int height) {
        super(x, y, width, height);
        setMask(2,3,12,11);
        this.speed = 0.8;
        maxHP = 20;
        HP = maxHP;
        damage = damageFix;
        isDamage = false;
    }
    
    @Override
    public void tick(){
        
        //dir = dirUp;
        moved = false;
        
        
        //if is not Colliding it will try to walk
        if (!isCollidingWithPlayer()){
        
        attackDelay = 0;
            
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
        } 
    
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
           
            } else {
                attackDelay++;
                if (attackDelay >= attackDelayMax) attackDelay = 0;
            }
        }
        
        CollidingWithProjectiles(getX(), getY());
        
        if (isDamage){
           damageFrames++;
           if (damageFrames == maxDamageFrames){
               isDamage = false;
               damageFrames = 0;
           }
        }
        
        
        CollidingWithProjectiles(getX(), getY());
        if (isDamage){
           damageFrames++;
           if (damageFrames == maxDamageFrames){
               isDamage = false;
               damageFrames = 0;
           }
        }
        
        if (this.HP <= 0) {
            dead();
        }
        
    }
    
    @Override
    public void render(Graphics g){
        if (!isDamage){
            if (dir == dirUp){
                g.drawImage(spritesUp[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null); 
            } else {
                g.drawImage(spritesDown[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null); 
            }            
        } else {
            if (dir == dirUp){
                g.drawImage(spritesUpDamage[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            } else {
                g.drawImage(spritesDownDamage[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
            }
             
        }
               
    }
    
    @Override
    protected void dead(){
        deleteSelf();
        return;
    }
    
    
}
