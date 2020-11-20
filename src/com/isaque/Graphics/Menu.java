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
    public int currentOption = 0;
    public final int maxOption = options.length - 1;
    public final int maxOptionStart = options.length - 2;
    public boolean up, down;
    public boolean isStarted = false, reset = false;
    
    @Override
    public void tick(){
        if (Game.isStarted){
            if (Game.skip == true){
            if (currentOption == 0){
                Game.loadGame();
                currentOption = 0;
            }  
            if (currentOption == 1){
                Game.reload();
                currentOption = 0;
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
            drawMenuMSG(g, "Aventuras de quintal", 0, Game.WIDTH, new Font("arial", Font.PLAIN, 12));
            drawMenuOption(g,options[0], 0, -30, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 0);
            //drawMenuOption(g,options[1], 0, 0, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 1);
            drawMenuOption(g,options[2], 0, 0, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 1);
            drawMenuOption(g,options[3], 0, 30, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 2);
            //credids
            g.setFont(new Font("arial", Font.PLAIN, 10));
            g.setColor(Color.black);
            //g.drawString("Se gostar da musica por-favor vá para:", 2, Game.HEIGTH - 10);
            g.drawString("Jogo feito por: Isaque Sebold", 2, Game.HEIGTH - 22);
            g.drawString("Musica: the FlashBack team", 2, Game.HEIGTH - 12);
            g.drawString("flashback.mors-games.com", 2, Game.HEIGTH -2);
            g.setColor(Color.white);
            //g.drawString("Se gostar da musica por-favor vá para:", 2, Game.HEIGTH - 11);
            g.drawString("flashback.mors-games.com", 2, Game.HEIGTH - 3);
            g.drawString("Musica: the FlashBack team", 2, Game.HEIGTH - 13);
            g.drawString("Jogo feito por: Isaque Sebold", 2, Game.HEIGTH - 23);
            //credids
        } else {
            drawMenuMSG(g, "Aventuras de quintal", 0, Game.WIDTH, new Font("arial", Font.PLAIN, 12));
            drawMenuOption(g,options[0], 0, -30, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 0);
            drawMenuOption(g,options[1], 0, 0, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 1);
            drawMenuOption(g,options[2], 0, 30, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 2);
            drawMenuOption(g,options[3], 0, 60, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 3);
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