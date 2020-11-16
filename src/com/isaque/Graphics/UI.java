package com.isaque.Graphics;

import com.isaque.main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;


public class UI {
    protected void drawCenterMSG(Graphics g, String text, int x, int y, int w, int h, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x2 = x + (w - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y2 = y + ((h - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.setColor(Color.BLACK);
        g.fillRect(x2 - 5, y2 - metrics.getHeight(), metrics.stringWidth(text) + 10, metrics.getHeight() + 5);
        g.setColor(Color.WHITE);
        g.drawRect(x2 - 6, y2 - metrics.getHeight() -1, metrics.stringWidth(text) + 11, metrics.getHeight() + 6);
        g.drawString(text, x2, y2);
    }

    protected void drawMenuMSG(Graphics g, String text, int x, int w, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        
        // Determine the X coordinate for the text
        int x2 = x + (w - metrics.stringWidth(text)) / 2;
        
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y2 = 25;
        
        // Set the font
        g.setFont(font);
        // Draw the String
        g.setColor(Color.BLACK);
        g.fillRect(x2 - 5, y2 - metrics.getHeight(), metrics.stringWidth(text) + 10, metrics.getHeight() + 5);
        g.setColor(Color.WHITE);
        g.drawRect(x2 - 6, y2 - metrics.getHeight() -1, metrics.stringWidth(text) + 11, metrics.getHeight() + 6);
        g.drawString(text, x2, y2);
    }
    public void render(Graphics g, BufferStrategy bs){                      
        drawCenterMSG(g, "UI", 0,0, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.BOLD, 9));
        
        g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(Game.image, 0, 0, Game.WWIDTH, Game.WHEIGTH, null);
	bs.show();
    }
    public void tick(){

    }
}
