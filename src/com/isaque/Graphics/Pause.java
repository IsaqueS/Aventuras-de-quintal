package com.isaque.Graphics;

import com.isaque.main.Game;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;


public class Pause extends UI{

    
    public void render(Graphics g, BufferStrategy bs){                    
        drawCenterMSG(g, "PAUSE - ESC PARA CONTINUAR", 0,0, Game.WIDTH, Game.HEIGTH, new Font("arial", Font.BOLD, 9));
        
        g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(Game.image, 0, 0, Game.WWIDTH, Game.WHEIGTH, null);
	bs.show();
    }
    public void tick(){
        
    }
    
}
