package com.isaque.Entities;

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
    private int z;
    
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
    
    private final int maxHP = 15000;
    private int HP = maxHP;
    private int ammo;
    private boolean hasWeapon = false;
    private boolean isDamage = false;
    
    public Player(int x, int y, int width, int height) {     
        super(x, y, width, height);
        
        this.speed = 1.5;
        
        rightPlayer =  new BufferedImage[4];
        leftPlayer =  new BufferedImage[4];
        rightPlayerDamage =  new BufferedImage[4];
        leftPlayerDamage =  new BufferedImage[4];
        rightPlayerJump =  new BufferedImage[5];
        leftPlayerJump =  new BufferedImage[5];
        
        setMask(3, 0, 10, 16);
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
                else if (jLeft == true && jRight == true && jUp == false && jDown == false ){
                    isJumping = false;
                    //index = 0;
                } else {
                    Sound.jump.play();
                    jump = false;
                    isJumping = true;
                    z = 1;
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
            if (jRight && Maps.isFree((int)(x + speed + 1), getY(), maskX, maskY, maskW, maskH)){ 
                x+= speedJump;
                dir = rightDir;
                moved = true;
            }
            else if (jLeft && Maps.isFree((int)(x - speed - 1), getY(), maskX, maskY, maskW, maskH)) {
                x-= speedJump;
                dir = leftDir;
                moved = true;
            }
            if (jUp && Maps.isFree(getX(), (int)(y - speed - 1), maskX, maskY, maskW, maskH)) {
                y-= speedJump;
                moved = true;
            }
            else if (jDown && Maps.isFree(getX(), (int)(y + speed + 1), maskX, maskY, maskW, maskH)) {
                y+= speedJump;
                moved = true;
            }      
            //jump moviment
            
        } else {      
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
        
            //attack
            if (ammo > 0 && hasWeapon){             
                if (mouseShoot){
                
                    ammo --;
                    double angle = Math.atan2(mouseX - (getXCenter() - Camera.getX()), mouseY - (getYCenter() - Camera.getY()));               
                
                    double dx = Math.sin(angle);
                    double dy = Math.cos(angle);

                    StoneShoot stone = new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, dx, dy);
                    Game.playerProjectiles.add(stone);       
                    Sound.shoot.play();
                }
                if (ammo >= 8 && specialAttack){
                     
                    Sound.specialAttack.play();
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.0, 1.0));                
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 1.0, 0.0));
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.5, 0.5));
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -0.5, 0.5));
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -0.5, -0.5));
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.5, -0.5));
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, -1.0, 0));
                    Game.playerProjectiles.add(new StoneShoot(getXCenter() - 1, getYCenter() - 1, 3, 3, 0.0, -1.0));
                    ammo-=8;
                
                    specialAttack = false;
                }
                //attack
            }                            
        
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
        
        }        
        
        //Others
        isCollidingWithItens();
        
        Camera.setX(Camera.clamp(((this.getX() + 8) - (Game.WIDTH / 2)), 0, (Maps.getWidth() * 16) - Game.WIDTH));
        Camera.setY(Camera.clamp(((this.getY() + 8) - (Game.HEIGTH / 2)), 0, (Maps.getHeight() * 16) - Game.HEIGTH));
        
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
            if (dir == rightDir){
                g.drawImage(rightPlayerJump[jumpIndex], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);
            } else {
                g.drawImage(leftPlayerJump[jumpIndex], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);
            }                      
            return;
        }
        if (!isDamage){
            if (dir == rightDir){
                g.drawImage(rightPlayer[index], (int)this.getX() - Camera.getX(), (int)this.getY() - Camera.getY() -z, null);
                if (hasWeapon){
                    g.drawImage(Entity.WEAPON_MINI_SPRITE, getX() + 6 - Camera.getX(), getY() +2 - Camera.getY() -z, null);
                }
            } 
            if (dir == leftDir){
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
