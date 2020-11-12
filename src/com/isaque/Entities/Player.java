package com.isaque.Entities;

import com.isaque.main.Game;
import com.isaque.maps.Camera;
import com.isaque.maps.Maps;
import com.isaque.maps.PortalCoordinates;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Player extends Entity{
    
    public boolean right, left, up, down, shoot, mouseShoot, specialAttack;
    public int mouseX, mouseY;
    public final double speed = 1.5;
    public boolean leftDir = false, rightDir = true;
    public boolean dir = rightDir;
    
    private static Rectangle playerBox;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] rightPlayerDamage;
    private BufferedImage[] leftPlayerDamage;
    
    private byte frames = 0, maxFrames = 6, index = 0, maxIndex = 3, damageFrames, maxDamageFrames = 4;
    private boolean moved = false;
    
    private final int maxHP = 150;
    private int HP = maxHP;
    private int ammo;
    private boolean hasWeapon = false;
    private boolean isDamage = false;
    
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        
        rightPlayer =  new BufferedImage[4];
        leftPlayer =  new BufferedImage[4];
        rightPlayerDamage =  new BufferedImage[4];
        leftPlayerDamage =  new BufferedImage[4];
        
        setMask(3, 0, 10, 16);
        //xCenter = maskW / 2;
        //yCenter = maskH /2;
        rightSprites: {
            rightPlayer[0] = Game.spritesheet.getSprite(32, 0, width, height);
            rightPlayer[1] = Game.spritesheet.getSprite(48, 0, width, height);
            rightPlayer[2] = Game.spritesheet.getSprite(64, 0, width, height);
            rightPlayer[3] = Game.spritesheet.getSprite(80, 0, width, height);
        }
        rightSpritesDamage: {
            rightPlayerDamage[0] = Game.spritesheet.getSprite(32, 32, width, height);
            rightPlayerDamage[1] = Game.spritesheet.getSprite(48, 32, width, height);
            rightPlayerDamage[2] = Game.spritesheet.getSprite(64, 32, width, height);
            rightPlayerDamage[3] = Game.spritesheet.getSprite(80, 32, width, height);
        }
        
        leftSprites: {
            leftPlayer[0] = Game.spritesheet.getSprite(32, 16, width, height);
            leftPlayer[1] = Game.spritesheet.getSprite(48, 16, width, height);
            leftPlayer[2] = Game.spritesheet.getSprite(64, 16, width, height);
            leftPlayer[3] = Game.spritesheet.getSprite(80, 16, width, height);
        }
        leftSprites: {
            leftPlayerDamage[0] = Game.spritesheet.getSprite(32, 48, width, height);
            leftPlayerDamage[1] = Game.spritesheet.getSprite(48, 48, width, height);
            leftPlayerDamage[2] = Game.spritesheet.getSprite(64, 48, width, height);
            leftPlayerDamage[3] = Game.spritesheet.getSprite(80, 48, width, height);
        }
    }
    
    public void tick(){
        
        playerBox = new Rectangle(getX() + maskX, getY() + maskY, maskW, maskH);
        
        //Death
        if (HP <= 0) death();
        //Death
        
        //colliding with portal
        for (int i = 0; i < Game.portals.size(); i++){
            PortalCoordinates p = Game.portals.get(i);
            if (playerBox.intersects(p.getX() - 1, p.getY() -1, 2, 2)){
                //next map
                Game.isReadyToLoad = true;
            }
        }
        //colliding with portal
        
        //movement
        moved = false;        
        if (left && Maps.isFree((int)(x - speed), getY(), maskX, maskY, maskW, maskH)) {
            x-= speed;
            dir = leftDir;
            moved = true;
        }
        if (right && Maps.isFree((int)(x + speed), getY(), maskX, maskY, maskW, maskH)){ 
            x+= speed;
            dir = rightDir;
            moved = true;
        }
        if (up && Maps.isFree(getX(), (int)(y - speed), maskX, maskY, maskW, maskH)) {
            y-= speed;
            moved = true;
        }
        if (down && Maps.isFree(getX(), (int)(y + speed), maskX, maskY, maskW, maskH)) {
            y+= speed;
            moved = true;
        }
        //movement
        
        //animation
        if (moved){
            frames++;
            if (Game.isStarted == false) Game.isStarted = true;
            if (frames == maxFrames){
                frames = 0;
                index++;
                if (index > maxIndex){
                    index = 0;                   
                }
            }
        } else {
            index = 0;
            frames = 0;
        }
        if (isDamage){
            damageFrames++;
            if (damageFrames == maxDamageFrames){
                isDamage = false;
                damageFrames = 0;
            }
        }
        //animation
        
        
        //attack
        if (ammo > 0 && hasWeapon){
            if (shoot){
                ammo --;
                int dx;
                if (dir == rightDir){
                    dx = 1;
                } else {
                    dx = -1;
                }
                StoneShoot stone = new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, dx, 0);
                Game.projectiles.add(stone);
                }
            if (mouseShoot){
                
                ammo --;
                double angle = Math.atan2(mouseX - (getXCenter() - Camera.getX()), mouseY - (getYCenter() - Camera.getY()));               
                
                double dx = Math.sin(angle);
                double dy = Math.cos(angle);

                StoneShoot stone = new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, dx, dy);
                Game.projectiles.add(stone);                
            }
            if (ammo >= 8 && specialAttack){
                
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.0, 1.0));                
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 1.0, 0.0));
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.5, 0.5));
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -0.5, 0.5));
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -0.5, -0.5));
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.5, -0.5));
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -1.0, 0));
                Game.projectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.0, -1.0));

                ammo-=8;
                
                specialAttack = false;
            }
            //attack
        }        
        
        //Others
        isCollidingWithItens();
        
        Camera.setX(Camera.clamp(((this.getX() + 8) - (Game.WIDTH / 2)), 0, (Maps.getWidth() * 16) - Game.WIDTH));
        Camera.setY(Camera.clamp(((this.getY() + 8) - (Game.HEIGTH / 2)), 0, (Maps.getHeight() * 16) - Game.HEIGTH));
        
        shoot = false;
        mouseShoot = false;
        //Others
    }
    
    public void render(Graphics g){
        if (!isDamage){
            if (dir == rightDir){
                g.drawImage(rightPlayer[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY(), null);
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() + 6 - Camera.getX(), getY() +2 - Camera.getY(), null);
                }
            } 
            if (dir == leftDir){
                g.drawImage(leftPlayer[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY(), null);
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() - 6 - Camera.getX(), getY() +2 - Camera.getY(), null);
                }
            }
            
        }else {
            if (dir == rightDir){
                g.drawImage(rightPlayerDamage[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY(), null);
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() + 6 - Camera.getX(), getY() +2 - Camera.getY(), null);
                }
            } 
            if (dir == leftDir){
                g.drawImage(leftPlayerDamage[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY(), null);
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() - 6 - Camera.getX(), getY() +2 - Camera.getY(), null);
                }
            }
            
        }
    }
    
    public void isCollidingWithItens(){
        for (int i = 0; i < Game.entities.size(); i++){
            Entity entity = Game.entities.get(i);
            if (entity instanceof Itens){
                Itens iten = (Itens) entity;
                if (iten instanceof Hamburguer && isColliding(this, iten)){
                    iten.Collected();
                    Game.entities.remove(i);
                } else if (iten instanceof Weapon && isColliding(this, iten)){                    
                    iten.Collected();
                    Game.entities.remove(i);
                } else if (iten instanceof Stone && isColliding(this, iten)){                    
                    iten.Collected();
                    Game.entities.remove(i);
                }
            } else {
                continue;
            }
        }
    }
    
    public Rectangle getPlayerBox(){
        return playerBox;
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
    
    private void death(){
        //Game.stop();
        new Game();
    }
    
    public void resetHP(){
        HP = maxHP;
    }
    
    public void giveAmmo(int ammo){
        this.ammo += ammo;
    }
    
    public int getAmmo(){
        return this.ammo;
    }
    
    public boolean haveWeapon(){
        return this.hasWeapon;
    }
    
    public void weaponCollected(){
        this.hasWeapon = true;
    }
    public void resetDamageAnimation(){
        this.damageFrames = 0;
    }
}
