package com.isaque.Graphics;

import com.isaque.main.Game;
import com.isaque.main.Save;
import com.isaque.main.Sound;
import com.isaque.maps.Maps;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu extends UI{
    
    public String[] options = {"Continar Jogo", "Reniciar fase", "Novo jogo","Fechar"};
    public final String[] labels = {"Jogo feito por Isaque Sebold", "Musica por the FlashBack team", "flashback.mors-games.com"};
    public int currentOption = 0;
    public final int maxOption = options.length - 1;
    public final int maxOptionStart = options.length - 2;
    public boolean up, down;
    public boolean isStarted = false, reset = false;
    //public Rectangle[] button = new Rectangle[4];
    //public Rectangle mouseClick;
    
    @Override
    public void tick(){
        //button();
        if (Game.isStarted){           
            if (Game.skip == true){
            if (currentOption == 0){
                Game.loadGame();
                currentOption = 0;
                Sound.music.resumeLoop();
            }  
            if (currentOption == 1){
                Game.reload();
                currentOption = 0;
                Sound.music.resetLoop();
             }
             if (currentOption == 2){
                Game.maps.resetLevel();
                Game.resetGame();
                try {
                    Save.saveLevel(Game.maps.getLevel());
                } catch (IOException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                //this.isStarted = false;
                currentOption = 0;
                Sound.music.resetLoop();
            }
            if (currentOption == 3){
                try {
                    Save.saveLevel(Game.maps.getLevel());
                } catch (IOException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                Game.stop();
                //this.isStarted = false;
                currentOption = 0;
            }
        }
        } else {
            if (Game.skip == true){
                if (currentOption == 0){
                    try {
                        int level = Integer.parseInt(Save.loadLevel());
                        if (level < 1) level = 1;
                        else if (level > Maps.maxLevel) level = 1;
                        Game.maps.setLevel(level);
                    } catch (IOException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Game.maps.resetLevel();
                    Game.resetGame();
                    currentOption = 0;
                }  
                if (currentOption == 1){
                    Game.resetGame();
                    Game.maps.resetLevel();
                    try {
                        Save.saveLevel(Game.maps.getLevel());
                    } catch (IOException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //this.isStarted = false;
                    currentOption = 0;
                }
                if (currentOption == 2){
                    Game.stop();
                    //this.isStarted = false;
                    currentOption = 0;
                }  
            } 
        }
        
        if (up){
            currentOption--;
            up = false;
            if (currentOption < 0 && Game.isStarted){
                currentOption = maxOption;
            } else if (currentOption < 0 && !Game.isStarted){
                currentOption = maxOptionStart;
            }
            Sound.select.play();
        }
        if (down){
            currentOption++;
            down = false;
            if (currentOption > maxOption && Game.isStarted){
                currentOption = 0;
            } if (currentOption > maxOptionStart && !Game.isStarted){
                currentOption = 0;
            }
            Sound.select.play();
        }
    }
  
    /*
    private void button(){
        for (int i = 0; i > maxOption; i++){
            if (button[i] == null){
                continue;
            }
            if (button[i].intersects(mouseClick)){
                System.out.println("click");
            }
        }
    }
    */
    @Override
    public void render(Graphics g, BufferStrategy bs){                      
        if (!isStarted){
            Graphics2D g2 = (Graphics2D) g; 
            g2.setColor(new Color(0,0,0,100));
            g2.fillRect(0, 0, Game.WWIDTH, Game.WHEIGTH);
            isStarted = true;
            //System.out.println("oi!");
            g2 = null;
        }
        if (!Game.isStarted){
            drawMenuMSG(g, "Aventuras de quintal", 0, Game.WIDTH, Game.font);
            drawMenuOption(g,options[0], 0, -30, Game.WIDTH, Game.HEIGTH, Game.font, 0);
            drawMenuOption(g,options[2], 0, 0, Game.WIDTH, Game.HEIGTH, Game.font, 1);
            drawMenuOption(g,options[3], 0, 30, Game.WIDTH, Game.HEIGTH, Game.font, 2);
            
            //credids
            g.setFont(Game.font2.deriveFont(Font.PLAIN, 14));
            
            g.setColor(Color.black);
            g.drawString(labels[0], 2, Game.HEIGTH - 22);
            g.drawString(labels[1], 2, Game.HEIGTH - 12);
            g.drawString(labels[2], 2, Game.HEIGTH -2);
            
            g.setColor(Color.white);
            g.drawString(labels[0], 2, Game.HEIGTH - 23);
            g.drawString(labels[1], 2, Game.HEIGTH - 13);
            g.drawString(labels[2], 2, Game.HEIGTH - 3);
            //credids
        } else {
            drawMenuMSG(g, "Aventuras de quintal", 0, Game.WIDTH, Game.font);
            drawMenuOption(g,options[0], 0, -30, Game.WIDTH, Game.HEIGTH, Game.font, 0);
            drawMenuOption(g,options[1], 0, 0, Game.WIDTH, Game.HEIGTH, Game.font, 1);
            drawMenuOption(g,options[2], 0, 30, Game.WIDTH, Game.HEIGTH, Game.font, 2);
            drawMenuOption(g,options[3], 0, 60, Game.WIDTH, Game.HEIGTH, Game.font, 3);
        }
        g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(Game.image, 0, 0, Game.WWIDTH, Game.WHEIGTH, null);
	bs.show();
    }
    
    private void drawMenuOption(Graphics g, String text, int x, int y, int w, int h, Font font, int optionNumber) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x2 = x + (w - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y2 = y + ((h - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        
        //this.button[optionNumber] = new Rectangle(x2 - 6, y2 - metrics.getHeight() - 1, metrics.stringWidth(text) + 101, metrics.getHeight() + 5);
        //System.out.println(this.button[optionNumber]);
        
        // Draw the String
        if (optionNumber == currentOption){
            g.setColor(Color.WHITE);
            g.fillRect(x2 - 5, y2 - metrics.getHeight(), metrics.stringWidth(text) + 10, metrics.getHeight() + 5);
            g.setColor(Color.black);
            g.drawRect(x2 - 6, y2 - metrics.getHeight() -1, metrics.stringWidth(text) + 11, metrics.getHeight() + 6);
            g.drawString(text, x2, y2);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(x2 - 5, y2 - metrics.getHeight(), metrics.stringWidth(text) + 10, metrics.getHeight() + 5);
            g.setColor(Color.WHITE);
            g.drawRect(x2 - 6, y2 - metrics.getHeight() -1, metrics.stringWidth(text) + 11, metrics.getHeight() + 6);
            g.drawString(text, x2, y2);
        }
    }
}