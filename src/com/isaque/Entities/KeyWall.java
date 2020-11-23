/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author isaqu
 */
public class KeyWall extends Enemies{
    public KeyWall(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height);
        setMask(0,0,16,16);
        this.sprite = sprite;
        this.depth = 0;
    }
    
    public void tick(){
        collidingWithProjectiles(getX(), getY());
    }
    
    @Override
    protected void collidingWithProjectiles (int nextX, int nextY){
        Rectangle thisEnemyBox = getEnemyBox();
        for (int i = 0; i < Game.playerProjectiles.size(); i++){
            Projectiles p = Game.playerProjectiles.get(i);
            Rectangle projectileBox = new Rectangle(p.getX(), p.getY(), width, height);
            if (thisEnemyBox.intersects(projectileBox)){
                Game.playerProjectiles.remove(p);
                Sound.hitt.play();
                return;
            }
        }
    }
    
    @Override
    protected void dead(){
        deleteSelf();
        Game.player.hasKey = false;
        Sound.key.play();
        return;
    }
}
