package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.maps.Maps;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemies extends Entity{
    
    public double speed = 0.5;
    protected int maxHP = 10, HP = maxHP, damage = 1;
    protected boolean isDamage;
    
    
    public Enemies(double x, double y, int width, int height) {
        super(x, y, width, height);
    }
    public Enemies(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }
    
    public boolean isCollidingWithPlayer(){
        Rectangle thisEnemyBox = getEnemyBox();
        if (thisEnemyBox.intersects(Game.player.getPlayerBox()) && !Game.player.isJumping){
            return true;
        }
        return false;
    }
    
    public Rectangle getEnemyBox(){
        return new Rectangle(getX() + maskX, getY() + maskY, maskW, maskH);
    }
    
    public boolean isCollidingWithOtherEnemy (int nextX, int nextY){
        Rectangle thisEnemyBox = new Rectangle(nextX + maskX, nextY + maskY, maskW, maskH);
        for (int i = 0; i < Game.enemies.size(); i++){
            Enemies e = Game.enemies.get(i);
            if (this == e) continue;
            Rectangle otherEnemyBox = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
            if (thisEnemyBox.intersects(otherEnemyBox)){
                return true;
            }
        }
                    
        return false;
    }
    
    public boolean canMove(double nextX, double nextY, boolean isX, boolean isNegative){
        
        boolean isFree, isColliding;
        
        if (isX){
            if (isNegative) {
                isFree = Maps.isFree((int)(nextX - speed), (int)(nextY),maskX, maskY, maskW, maskH);
            } else {
                isFree = Maps.isFree((int)(nextX + speed), (int)(nextY),maskX, maskY, maskW, maskH);
            }
        } else {
            if (isNegative) {
                isFree = Maps.isFree((int)(nextX), (int)(nextY - speed),maskX, maskY, maskW, maskH);
            } else {
                isFree = Maps.isFree((int)(nextX), (int)(nextY + speed),maskX, maskY, maskW, maskH);
            }
        }
        
        if (isX){
            if (isNegative) {
                isColliding = isCollidingWithOtherEnemy((int)(nextX - speed), (int)(nextY));
            } else {
                isColliding = isCollidingWithOtherEnemy((int)(nextX + speed), (int)(nextY));
            }
        } else {
            if (isNegative) {
                isColliding = isCollidingWithOtherEnemy((int)(nextX), (int)(nextY - speed));
            } else {
                isColliding = isCollidingWithOtherEnemy((int)(nextX), (int)(nextY + speed));
            }
        }
        
        if (isFree && !isColliding) return true;
        
        return false;
    }
    
    protected void CollidingWithProjectiles (int nextX, int nextY){
        Rectangle thisEnemyBox = getEnemyBox();
        for (int i = 0; i < Game.playerProjectiles.size(); i++){
            Projectiles p = Game.playerProjectiles.get(i);
            Rectangle projectileBox = new Rectangle(p.getX(), p.getY(), width, height);
            if (thisEnemyBox.intersects(projectileBox)){
                damageHP(p.getDamage());
                this.isDamage = true;
                Game.playerProjectiles.remove(p);
                return;
            }
        }
    }
    
    public void damageHP(int HP){
        this.HP -= HP;
        this.isDamage = true;
    }
    public void healHP (int HP){
        this.HP += HP;
        if (this.HP > maxHP) this.HP = maxHP;
    }
    
    public int getHP(){
        return HP;
    }
    public int getMaxHP(){
        return maxHP;
    }
    
    protected void deleteSelf(){
        Game.entities.remove(this);
        Game.enemies.remove(this);
    }
    
    protected void dead(){
        deleteSelf();
        return;
    }
}
