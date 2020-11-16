package com.isaque.Graphics;

import com.isaque.main.Game;
import com.isaque.main.Sound;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class Menu extends UI{
    
    public String[] options = {"Novo jogo", "Carregar Jogo", "Fechar"};
    public int currentOption = 0;
    public final int maxOption = options.length - 1;
    public boolean up, down;
    public boolean isStarted = false;
    
    public void tick(){
        if (Game.skip == true){
            if (currentOption == 0){
                Game.maps.nextLevel();
                Game.resetGame();
            }
            if (currentOption == 1){
                Game.maps.nextLevel();
                Game.resetGame();
            }
            if (currentOption == 2){
                Game.stop();
            }
            
        }
        if (up){
            currentOption--;
            up = false;
            if (currentOption < 0){
                currentOption = maxOption;
            }
            Sound.select.play();
        }
        if (down){
            currentOption++;
            down = false;
            if (currentOption > maxOption){
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
            g.fillRect(0, 0, Game.WWIDTH, Game.WHEIGTH);
            isStarted = true;
        }
        drawMenuMSG(g, "Aventuras de quintal", 0, Game.WIDTH, new Font("arial", Font.PLAIN, 12));
        drawMenuOption(g,options[0], 0, -30, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 0);
        drawMenuOption(g,options[1], 0, 0, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 1);
        drawMenuOption(g,options[2], 0, 30, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.PLAIN, 10), 2);
        
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