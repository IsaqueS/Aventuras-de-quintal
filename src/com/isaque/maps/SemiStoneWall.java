package com.isaque.maps;
/*
import com.isaque.Entities.Projectiles;
import com.isaque.main.Game;
import com.isaque.main.Sound;
import static com.isaque.maps.Camera.getX;
import static com.isaque.maps.Camera.getY;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class SemiStoneWall extends Wall{
    
    private final int maxHP = 30;
    private int HP = maxHP;
    private boolean isDamage = false;
    
    
    public void render (Graphics g){
        g.drawImage(sprite, x - Camera.getX(), y - Camera.getY(), null);
    }
    
    public void tick(){
        CollidingWithProjectiles(getX(), getY());
        if (isDamage){
           damageFrames++;
           if (damageFrames == maxDamageFrames){
               isDamage = false;
               damageFrames = 0;
           }
        }
    }
    protected void CollidingWithProjectiles (int nextX, int nextY){
        Rectangle thisTileBox = new Ractangle();
        for (int i = 0; i < Game.playerProjectiles.size(); i++){
            Projectiles p = Game.playerProjectiles.get(i);
            Rectangle projectileBox = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            if (thisEnemyBox.intersects(projectileBox)){
                Sound.hitt.play();
                damageHP(p.getDamage());
                this.isDamage = true;
                Game.playerProjectiles.remove(p);
                return;
            }
        }
    }
    private void damageHP(int HP){
        this.HP -= HP;
        this.isDamage = true;
    }
}
*/
