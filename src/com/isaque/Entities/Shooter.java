package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import java.awt.image.BufferedImage;

public class Shooter extends Enemies{
    
    private final int damageFix, shootWaitMax, bonusDamage, maxDamageDelay;
    private int shootWait = 0, damageDelay = 0;
    
    public Shooter(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height);
        this.damageFix = 10;
        setMask(0,0,16,16);
        this.speed = 0.8;
        maxHP = 20;
        HP = maxHP;
        damage = damageFix;
        isDamage = false;
        //dir = dirUp;
        this.depth = 2;
        this.sprite = sprite;
        this.bonusDamage = 5;
        this.shootWaitMax = 90;
        this.maxDamageDelay = 60;
    }
    
    public void tick(){
        if (HP <= 0){
           this.dead();
        }
        if (canGo(getX(), getY(), Game.player.getX(), Game.player.getY()) < 144){
            if (shootWait > shootWaitMax){  
                int random = Game.random.nextInt(30) - 15;
                
                double angle = Math.atan2(Game.player.getXCenter() - (getXCenter()) + random, Game.player.getYCenter() - (getYCenter()) - random);               
                
                double dx = Math.sin(angle);
                double dy = Math.cos(angle);
                
                Sound.shoot.play();
                StoneShoot p = new StoneShoot(getXCenter() - 1,
                    getYCenter() - 1, 3, 3, dx, dy, damage + Game.random.nextInt(bonusDamage)
                    , Projectiles.color, 2.0);
                Game.enemyProjectiles.add(p);
                shootWait = 0;
            } else {
                shootWait++;
            }
            if (this.isCollidingWithPlayer()){
                if (damageDelay == 0){
                    damage = damageFix;
                    damage += Game.random.nextInt(bonusDamage);
                    Game.player.damageHP(damage);
                    Game.player.resetDamageAnimation();    
                    damageDelay++;
                    Sound.hitt.play();
                }
                if (damageDelay < maxDamageDelay){
                    damageDelay++;
                }
                else damageDelay = 0;
                
            } else {
                damageDelay = 0;
            }
        }
        collidingWithProjectiles(getX(), getY());
    }
}
