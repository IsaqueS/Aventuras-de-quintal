package com.isaque.Graphics;

import com.isaque.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;


public class GameOver extends UI{   
    
    public void render(Graphics g, BufferStrategy bs){              
        drawCenterMSG(g, "Game Over - Enter para Continuar", 0,0, Game.WIDTH, Game.HEIGTH, Game.font2);
        
        g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(Game.image, 0, 0, Game.WWIDTH, Game.WHEIGTH, null);
	bs.show();
    }
    public void tick(){
        if (Game.skip == true){
            Game.resetGame();
        }
    }
}
    