package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import com.isaque.maps.AStar;
import com.isaque.maps.Maps;
import com.isaque.maps.Node;
import com.isaque.maps.Vector2i;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Enemies extends Entity{
    
    public double speed = 0.5;
    protected int maxHP = 10, HP = maxHP, damage = 1;
    protected boolean isDamage;
    private byte waitForFindPath = 0;
    
    
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
            Rectangle otherEnemyBox = new Rectangle(e.getX() + e.getMaskX(), e.getY() + e.getMaskY(), e.getMaskW(), e.getMaskH());
            if (thisEnemyBox.intersects(otherEnemyBox)){
                return true;
            }
        }
                    
        return false;
    }
    public boolean canGo(int x1, int y1, int x2, int y2, double maxDistance){
        if (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) < maxDistance){
            return true;
        }
        return false;
    }
    
    public boolean canMove(double nextX, double nextY, boolean isX, boolean isNegative, double spd){
        
        boolean isFree, isColliding;
        
        if (isX){
            if (isNegative) {
                isFree = Maps.isFree((int)(nextX - spd), (int)(nextY), this.getMaskX(), this.getMaskY(), this.getMaskW(), this.getMaskH());
            } else {
                isFree = Maps.isFree((int)(nextX + spd), (int)(nextY),this.getMaskX(), this.getMaskY(), this.getMaskW(), this.getMaskH());
            }
        } else {
            if (isNegative) {
                isFree = Maps.isFree((int)(nextX), (int)(nextY - spd), this.getMaskX(), this.getMaskY(), this.getMaskW(), this.getMaskH());
            } else {
                isFree = Maps.isFree((int)(nextX), (int)(nextY + spd),this.getMaskX(), this.getMaskY(), this.getMaskW(), this.getMaskH());
            }
        }
        
        if (isX){
            if (isNegative) {
                isColliding = isCollidingWithOtherEnemy((int)(nextX - spd), (int)(nextY));
            } else {
                isColliding = isCollidingWithOtherEnemy((int)(nextX + spd), (int)(nextY));
            }
        } else {
            if (isNegative) {
                isColliding = isCollidingWithOtherEnemy((int)(nextX), (int)(nextY - spd));
            } else {
                isColliding = isCollidingWithOtherEnemy((int)(nextX), (int)(nextY + spd));
            }
        }
        
        if (isFree && !isColliding) return true;
        
        return false;
    }
    
    protected void collidingWithProjectiles (int nextX, int nextY){
        Rectangle thisEnemyBox = getEnemyBox();
        for (int i = 0; i < Game.playerProjectiles.size(); i++){
            Projectiles p = Game.playerProjectiles.get(i);
            Rectangle projectileBox = new Rectangle(p.getX(), p.getY(), p.width, p.height);
            if (thisEnemyBox.intersects(projectileBox)){
                Sound.hitt.play();
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
       
    public void followPath(List<Node> path, double spd, double maxPathDistence){
        if (path != null){
            if (path.size() > 0  && path.size() <= maxPathDistence){
                Vector2i target = path.get(path.size() - 1).tile;
                if (x < target.x * Maps.TILE_SIZE && canMove(x, y, true, false, spd)){
                    x += spd;
                    if(x > target.x * Maps.TILE_SIZE){
                    x = target.x * Maps.TILE_SIZE;
                    }
                } else if (x > target.x * Maps.TILE_SIZE && canMove(x, y, true, true, spd)){
                    x -= spd;
                    if(x < target.x * Maps.TILE_SIZE){
                    x = target.x * Maps.TILE_SIZE;
                    }
                }
                if (y < target.y * Maps.TILE_SIZE && canMove(x, y, false, false, spd)){
                    y += spd;
                    if(y > target.y * Maps.TILE_SIZE){
                    y = target.y * Maps.TILE_SIZE;
                    }
                } else if (y > target.y * Maps.TILE_SIZE && canMove(x, y, false, true, spd)){
                    y -= spd;
                    if(y < target.y * Maps.TILE_SIZE){
                    y = target.y * Maps.TILE_SIZE;
                    }
                }
                if (x == target.x * Maps.TILE_SIZE && y == target.y * Maps.TILE_SIZE){
                    path.remove(path.size() - 1);
                }
            }
        }
    }
    
    public void createPath(short maxDistance){
        this.waitForFindPath++;
        if (!isCollidingWithPlayer()  && canGo(getX(), getY(), Game.player.getX(), Game.player.getY(), maxDistance)){
            if ((waitForFindPath > 30 || path == null || path.size() == 0)){
                Vector2i start = new Vector2i((int)(x / Maps.TILE_SIZE), (int)(y / Maps.TILE_SIZE));
                Vector2i end = new Vector2i((int)(Game.player.getXCenter() / Maps.TILE_SIZE), (int)(Game.player.getYCenter() / Maps.TILE_SIZE));
                path = AStar.findPath(Game.maps, start, end);
                this.waitForFindPath = 0;
            }     
        } else {
            this.waitForFindPath = 0;
        }
    }
}
