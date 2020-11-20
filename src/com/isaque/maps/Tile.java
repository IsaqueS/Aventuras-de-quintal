package com.isaque.maps;

import com.isaque.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Tile {
    
    public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
    public static BufferedImage TILE_FLOOR_FLOWER1 = Game.spritesheet.getSprite(32, 96, 16, 16);
    public static BufferedImage TILE_WALL_WHITH_SIGN = Game.spritesheet.getSprite(32, 112, 16, 16);
    public static BufferedImage TILE_FLOOR_FLOWER2 = Game.spritesheet.getSprite(48, 96, 16, 16);
    public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
    public static BufferedImage[] TILE_PORTAL_GRASS = {Game.spritesheet.getSprite(96, 16, 16, 16),Game.spritesheet.getSprite(96, 32, 16, 16),Game.spritesheet.getSprite(96, 48, 16, 16),Game.spritesheet.getSprite(96, 32, 16, 16)};
    
    protected BufferedImage sprite;
    protected BufferedImage[] sprites;
    protected int x,y;
    
    public void render (Graphics g){
        g.drawImage(sprite, x - Camera.getX(), y - Camera.getY(), null);
    }
    
    public Tile(int x, int y, BufferedImage sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Tile(int x, int y, BufferedImage[] sprites){
        this.x = x;
        this.y = y;
        this.sprites = new BufferedImage[sprites.length];
        for (int i = 0; i < sprites.length; i++){
            this.sprites[i] = sprites[i];
        }
    }
}
