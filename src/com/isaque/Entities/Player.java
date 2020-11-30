package com.isaque.Entities;

import com.isaque.Entities.KeyWall;
import com.isaque.main.Game;
import com.isaque.main.Sound;
import com.isaque.maps.Camera;
import com.isaque.maps.Maps;
import com.isaque.maps.PortalCoordinates;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Player extends Entity{
    
    public boolean right, left, up, down, mouseShoot, specialAttack, jump, Game_Over;
    private boolean jRight, jLeft,jUp, jDown;
    public boolean isJumping = false;
    public int mouseX, mouseY;
    public final double speedJump = 2.5;
    public boolean leftDir = false, rightDir = true, dir = rightDir;
    public boolean hasKey = false;
    private int z, damage = 10;
    
    private static Rectangle playerBox;
    private final BufferedImage[] rightPlayer;
    private final BufferedImage[] leftPlayer;
    private final BufferedImage[] rightPlayerDamage;
    private final BufferedImage[] leftPlayerDamage;
    private final BufferedImage[] rightPlayerJump;
    private final BufferedImage[] leftPlayerJump;
    
    private final byte maxFrames = 6, maxIndex = 3, maxDamageFrames = 4, maxJumpFrames = 30, maxJumpIndex = 5, maxNextJFrame = maxJumpFrames / maxJumpIndex, maxJumpWait = 60;
    private boolean moved = false;
    private byte frames = 0, index = 0, damageFrames = 0, jumpFrames = 0, jumpIndex = 0, nextJFrame = 0, jumpWait;
    
    private final int maxHP = 150, bonusDamage = 5;
    private int HP = maxHP;
    private int ammo;
    private boolean hasWeapon = false;
    private boolean isDamage = false;
    
    public Player(int x, int y, int width, int height) {     
        super(x, y, width, height);
        this.depth = 1;
        this.speed = 1.5;
        
        rightPlayer =  new BufferedImage[4];
        leftPlayer =  new BufferedImage[4];
        rightPlayerDamage =  new BufferedImage[4];
        leftPlayerDamage =  new BufferedImage[4];
        rightPlayerJump =  new BufferedImage[5];
        leftPlayerJump =  new BufferedImage[5];
        
        setMask(3, 1, 10, 15);
        rightSprites: {
            rightPlayer[0] = Game.spritesheet.getSprite(32, 0, width, height);
            rightPlayer[1] = Game.spritesheet.getSprite(48, 0, width, height);
            rightPlayer[2] = Game.spritesheet.getSprite(64, 0, width, height);
            rightPlayer[3] = Game.spritesheet.getSprite(80, 0, width, height);
            
            rightPlayerJump[0] = Game.spritesheet.getSprite(32, 64, width, height);
            rightPlayerJump[1] = Game.spritesheet.getSprite(48, 64, width, height);
            rightPlayerJump[2] = Game.spritesheet.getSprite(64, 64, width, height);
            rightPlayerJump[3] = Game.spritesheet.getSprite(80, 64, width, height);
            rightPlayerJump[4] = Game.spritesheet.getSprite(96, 64, width, height);
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
            
            leftPlayerJump[0] = Game.spritesheet.getSprite(32, 80, width, height);
            leftPlayerJump[1] = Game.spritesheet.getSprite(48, 80, width, height);
            leftPlayerJump[2] = Game.spritesheet.getSprite(64, 80, width, height);
            leftPlayerJump[3] = Game.spritesheet.getSprite(80, 80, width, height);
            leftPlayerJump[4] = Game.spritesheet.getSprite(96, 80, width, height);
        }
        leftSpritesDamage: {
            leftPlayerDamage[0] = Game.spritesheet.getSprite(32, 48, width, height);
            leftPlayerDamage[1] = Game.spritesheet.getSprite(48, 48, width, height);
            leftPlayerDamage[2] = Game.spritesheet.getSprite(64, 48, width, height);
            leftPlayerDamage[3] = Game.spritesheet.getSprite(80, 48, width, height);
        }        
        
    }
    
    @Override
    public void tick(){
        //System.out.println("Jump: " + jump);
        //System.out.println("isJumping: " + isJumping);
        playerBox = new Rectangle(getX() + maskX, getY() + maskY, maskW, maskH);
        if (Game.isStarted == false) Game.isStarted = true;

        //Death
        if (HP <= 0) death();
        //Death
        
        if (jumpWait < maxJumpWait){
            jumpWait++;
        }
        
        if (jump){
            if (isJumping == false && maxJumpWait == jumpWait){
                jRight = right;
                jLeft = left;
                jUp = up;
                jDown = down;               
                jumpWait = 0;
                if (jUp == false && jDown == false && jLeft == false && jRight == false){
                    isJumping = false;
                    //index = 0;
                }
                else if (jUp == true && jDown == true && jLeft == false && jRight == false){
                    isJumping = false;
                    //index = 0;
                }
                else if (jUp == false && jDown == false && jLeft == false && jRight == false){
                    isJumping = false;
                    //index = 0;
                } else {
                    Sound.jump.play();
                    jump = false;
                    isJumping = true;
                    z = 1;
                    this.depth = 3;
                }
            } 
            
        }      
        
        if (isJumping){                     
            jumpFrames++;
            isDamage = false;
            if(jumpFrames >= maxNextJFrame + nextJFrame){
                //System.out.println(nextJFrame);
                nextJFrame += maxNextJFrame;
                jumpIndex++;
                if(jumpFrames >= maxJumpFrames){
                    jumpIndex = 0;
                    nextJFrame = 0;
                    jumpFrames = 0;
                    isJumping = false;
                    jumpFrames = 0;
                    z = 0;
                    isJumping = false;
                    jRight = false;
                    jLeft = false;
                    jUp = false;
                    jDown = false;
                }
            }          
            //jump moviment           
            if (jLeft && Maps.isFree((int)(x - speed - 1), getY(), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall((int) (getAccurateX() - speed - 1), getY())) {
                x-= speedJump;
                dir = leftDir;
                moved = true;
            }
            if (jRight && Maps.isFree((int)(x + speed + 1), getY(), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall((int) (getAccurateX() + speed + 1), getY())){ 
                x+= speedJump;
                dir = rightDir;
                moved = true;
            }
            if (jDown && Maps.isFree(getX(), (int)(y + speed + 1), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall(getX(), (int) (getAccurateY() + speed + 1))) {
                y+= speedJump;
                moved = true;
            }
            if (jUp && Maps.isFree(getX(), (int)(y - speed - 1), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall(getX(), (int) (getAccurateY() - speed - 1))) {
                y-= speedJump;
                moved = true;
            }
            Camera.setX(Camera.clamp(((this.getX() + 8) - (Game.WIDTH / 2)), 0, (Maps.getWidth() * 16) - Game.WIDTH));
            Camera.setY(Camera.clamp(((this.getY() + 8) - (Game.HEIGTH / 2)), 0, (Maps.getHeight() * 16) - Game.HEIGTH));
            //jump moviment
            
        } else {      
            //movement    
            this.depth = 1;
            moved = false;
            
            collidingWithProjectiles();
            
            if (left && Maps.isFree((int)(x - speed), getY(), maskX, maskY, maskW, maskH) && !isCollidingWithSemiWall((int)(getAccurateX() - speed), getY())) {
                x-= speed;
                dir = leftDir;
                moved = true;
            }
            if (right && Maps.isFree((int)(x + speed), getY(), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall((int)(getAccurateX() + speed), getY())){ 
                x+= speed;
                dir = rightDir;
                moved = true;
            }
            if (up && Maps.isFree(getX(), (int)(y - speed), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall(getX(), (int) (getAccurateY() - speed))) {
                y-= speed;
                moved = true;
            }
            if (down && Maps.isFree(getX(), (int)(y + speed), maskX, maskY, maskW, maskH)&& !isCollidingWithSemiWall(getX(), (int) (getAccurateY() + speed))) {
                y+= speed;
                moved = true;
            }
            //movement
        
            //attack
            if (hasWeapon){             
                if (mouseShoot){
                    if (ammo > 0){
                        ammo --;
                        double angle = Math.atan2(mouseX - (getXCenter() - Camera.getX()), mouseY - (getYCenter() - Camera.getY()));               
                
                        double dx = Math.sin(angle);
                        double dy = Math.cos(angle);

                        StoneShoot stone = new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, dx, dy, damage + Game.random.nextInt(bonusDamage));
                        Game.playerProjectiles.add(stone);       
                        Sound.shoot.play();
                    } else {
                        Sound.noAmmo.play();
                    }
                } 
                if (specialAttack){
                    if (ammo >= 8){
                        Sound.specialAttack.play();
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 1.0, 0.0, damage + Game.random.nextInt(bonusDamage)));
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.5, 0.5, damage + Game.random.nextInt(bonusDamage)));
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -0.5, 0.5, damage + Game.random.nextInt(bonusDamage)));
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -0.5, -0.5, damage + Game.random.nextInt(bonusDamage)));
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.5, -0.5, damage + Game.random.nextInt(bonusDamage)));
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -1.0, 0, damage + Game.random.nextInt(bonusDamage)));
                        Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.0, -1.0, damage + Game.random.nextInt(bonusDamage)));
                        ammo-=8;
                    } else {
                        Sound.noAmmo.play();
                    }
                    specialAttack = false;
                }
                //attack
            }                            
        
        //animation
        if (moved){
            Camera.setX(Camera.clamp(((this.getX() + 8) - (Game.WIDTH / 2)), 0, (Maps.getWidth() * 16) - Game.WIDTH));
            Camera.setY(Camera.clamp(((this.getY() + 8) - (Game.HEIGTH / 2)), 0, (Maps.getHeight() * 16) - Game.HEIGTH));
            frames++;       
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
        
        }        
        
        //Others
        isCollidingWithItens();
        mouseShoot = false; 
        
        //Others
        //colliding with portal
        for (int i = 0; i < Game.portals.size(); i++){
            PortalCoordinates p = Game.portals.get(i);
            if (playerBox.intersects(p.getX() - 1, p.getY() -1, 2, 2)){
                //next map
                Game.maps.nextLevel();
                Game.resetGame();
            }
        }
        //colliding with portal
    }
    
    @Override
    public void render(Graphics g){
        //if ()
        if (isJumping){
            g.drawImage(SHADOW, getX() - Camera.getX(), getY() - Camera.getY(), null);
            if (dir == rightDir){
                g.drawImage(rightPlayerJump[jumpIndex], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);
            } else {
                g.drawImage(leftPlayerJump[jumpIndex], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);
            }                      
            return;
        }
        g.drawImage(SMALL_SHADOW, getX() - Camera.getX(), getY() - Camera.getY() + 1, null);
        /*
        if (hasKey){
            g.drawImage(Entity.Key, getX() - Camera.getX() - 3, getY() - Camera.getY(), null);
        }*/
        
        if (!isDamage){
            if (dir == rightDir){
                if (hasKey){
                    g.drawImage(Entity.Key, getX() - Camera.getX() - 3, getY() - Camera.getY(), null);
                } 
                g.drawImage(rightPlayer[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);                
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() + 6 - Camera.getX(), getY() +2 - Camera.getY() -z, null);
                }
            } 
            if (dir == leftDir){
                if (hasKey){
                    g.drawImage(Entity.Key, getX() - Camera.getX() +3, getY() - Camera.getY(), null);
                }
                g.drawImage(leftPlayer[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() - 6 - Camera.getX(), getY() +2 - Camera.getY() -z, null);
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
    
    private void collidingWithProjectiles (){
        Rectangle playerBox = this.getPlayerBox();
        for (int i = 0; i < Game.enemyProjectiles.size(); i++){
            Projectiles p = Game.enemyProjectiles.get(i);
            Rectangle projectileBox = new Rectangle(p.getX(), p.getY(), p.width, p.height);
            if (playerBox.intersects(projectileBox)){
                Sound.hitt.play();
                damageHP(p.getDamage());
                this.isDamage = true;
                Game.enemyProjectiles.remove(p);
                return;
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
                } else if (iten instanceof Key && isColliding(this, iten)){                    
                    iten.Collected();
                    Game.entities.remove(i);
                }
            } else {
                continue;
            }
        }
    }
    
    public boolean isCollidingWithSemiWall (int nextX, int nextY){
        Rectangle thisEnemyBox = new Rectangle(nextX + maskX, nextY + maskY, maskW, maskH);
        for (int i = 0; i < Game.enemies.size(); i++){
            Enemies e = Game.enemies.get(i);
            if (e instanceof SemiWall){
                Rectangle otherEnemyBox = new Rectangle(e.getX() + e.getMaskX(), e.getY() + e.getMaskY(), e.getMaskW(), e.getMaskH());
                if (thisEnemyBox.intersects(otherEnemyBox)){
                    return true;
                }
            } else if(e instanceof KeyWall){
                Rectangle otherEnemyBox = new Rectangle(e.getX() + e.getMaskX(), e.getY() + e.getMaskY(), e.getMaskW(), e.getMaskH());
                if (thisEnemyBox.intersects(otherEnemyBox)){
                    if (Game.player.hasKey){
                        e.dead();
                        return false;
                    }
                    return true;
                }
            } else {
                continue;
            }                 
        }
        return false;
    }
    public Rectangle getPlayerBox(){
        return playerBox;
    }
    
    public void damageHP(int HP){
        this.HP -= HP;
        this.isDamage = true;
        if (this.HP < 0){
            this.HP = 0;
        }
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
        //new Game();
        Game.startGameOver();
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
    public int getZ(){
        return this.z;
    }
}
