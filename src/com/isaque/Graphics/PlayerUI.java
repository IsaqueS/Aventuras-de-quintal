package com.isaque.Graphics;

import com.isaque.Entities.Entity;
import com.isaque.main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class PlayerUI {
    
    private final int maxBarSize, maxBarWidth, barX, barY;
    private int barMultipliyer;
    private Color life, dead;
    
    public PlayerUI(){
        barX = 6;
        barY = 6;
        maxBarSize = 10;
        maxBarWidth = 75;
        barMultipliyer = Game.player.getMaxHP() / maxBarWidth;
        dead = new Color(247,36,35);
        life = new Color(22, 214, 32);
    }
    
    public void render(Graphics g){
        playeLife: {
            g.setColor(Color.WHITE);
            g.drawRect(barX - 1, barY - 1, maxBarWidth + 1, maxBarSize + 1);
            g.setColor(dead);
            g.fillRect(barX, barY, maxBarWidth, maxBarSize);
            g.setColor(life);
            g.fillRect(barX, barY,(int) ((Game.player.getHP() / barMultipliyer)), maxBarSize);
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 8));
            g.drawString("VIDA: " + Game.player.getHP() + " / " + Game.player.getMaxHP(), barX + 2, barY + 8);
            renderIten(g);
            
        }

    }
    
    public void renderIten(Graphics g){
        if (Game.player.haveWeapon()){
                g.drawImage(Entity.WEAPON_SPRITE, barX -1 , barY + maxBarSize + 1, null);
                g.drawImage(Entity.STONE_SPRITE, barX  + 10,  barY + maxBarSize, null);
                g.drawString(Integer.toString(Game.player.getAmmo()), barX + 23, barY + maxBarSize + 11);
            } else {                
                g.drawImage(Entity.STONE_SPRITE, barX - 5,  barY + maxBarSize, null);
                g.drawString(Integer.toString(Game.player.getAmmo()), barX + 9, barY + maxBarSize + 11);
            }
    }
    
}
