package com.isaque.maps;

import com.isaque.Entities.Entity;
import com.isaque.Entities.Hamburguer;
import com.isaque.Entities.SemiWall;
import com.isaque.Entities.Spider;
import com.isaque.Entities.Stone;
import com.isaque.Entities.Weapon;
import com.isaque.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Maps {
    
    private static Tile[] tiles;
    private static int width, height;
    public static final int TILE_SIZE = 16;
    private static int level = 0;
    public final static int maxLevel = 3;
    private String path;
    private static BufferedImage map;
   
    public Maps(String path) {
        this.path = path;       
    }
    public void render(Graphics g){
        int xStart = Camera.getX() >> 4;
        int yStart = Camera.getY() >> 4;
        int xFinal = xStart + (Game.WWIDTH >> 4);
        int yFinal = yStart + (Game.WHEIGTH >> 4);
        
        
        for (int xx = xStart; xx <= xFinal; xx++){
            for (int yy = yStart; yy <= yFinal; yy++){
                if (xx < 0 || yy < 0 || xx >= width || yy >= height) continue;
                Tile tile = tiles[xx + (yy*width)];
                tile.render(g); 
            }
        }
    }
    
    public static int getWidth(){
        return width;
    }
    public static int getHeight(){
        return height;
    }
    public static Tile[] getTiles(){
        return tiles;
    }
    
    public static boolean isFree(int x, int y){
        int x1 = (x / TILE_SIZE);
        int y1 = (y  / TILE_SIZE);
        
        int x2 = (x + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = (y + TILE_SIZE - 1) / TILE_SIZE;
        
        return !((tiles[x1 + (y1* Maps.width)] instanceof Wall) ||
                (tiles[x2 + (y2* Maps.width)] instanceof Wall) ||
                (tiles[x1 + (y2* Maps.width)] instanceof Wall) ||
                (tiles[x2 + (y1* Maps.width)] instanceof Wall));
    }
    
    public static boolean isFree(int x, int y, int maskX, int maskY, int maskW, int maskH){
        int x1 = ((x + maskX) / TILE_SIZE);
        int y1 = ((y + maskY)  / TILE_SIZE);
        
        int x2 = (x + (maskW) + maskX - 1) / TILE_SIZE;
        int y2 = (y + (maskH) + maskY - 1) / TILE_SIZE;
        
        return !((tiles[x1 + (y1* Maps.width)] instanceof Wall) ||
                (tiles[x2 + (y2* Maps.width)] instanceof Wall) ||
                (tiles[x1 + (y2* Maps.width)] instanceof Wall) ||
                (tiles[x2 + (y1* Maps.width)] instanceof Wall));
    }
    /*
    public static boolean isFreePlayer(int x, int y, int maskX, int maskY, int maskW, int maskH){
        int x1 = ((x + maskX) / TILE_SIZE);
        int y1 = ((y + maskY)  / TILE_SIZE);
        
        int x2 = (x + (maskW) + maskX - 1) / TILE_SIZE;
        int y2 = (y + (maskH) + maskY - 1) / TILE_SIZE;
        
        if (!((tiles[x1 + (y1* Maps.width)] instanceof Wall) ||
                (tiles[x2 + (y2* Maps.width)] instanceof Wall) ||
                (tiles[x1 + (y2* Maps.width)] instanceof Wall) ||
                (tiles[x2 + (y1* Maps.width)] instanceof Wall))){
            return true;
        }
        
        if (Game.player.getZ() > 0){
            return true;
        }      
        return false;
    }
    */
    public void nextLevel(){
        level++;
        if (level > maxLevel){
            level = 1;
        }
    }
    public void setLevel(int level){
        this.level = level;
        if (this.level > maxLevel){
            this.level = 1;
        }
    }
    
    public int getLevel(){
        return level;
    }
    
    public void resetLevel(){
        level = 1;
    }
    public static Tile getTile(int n){
        return tiles[n];
    }
    
    public void loadLevel(){
               
        try {
            
            map = ImageIO.read(getClass().getResource(this.path + "map_" + level + ".png"));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            
            width = map.getWidth();
            height = map.getHeight();
            tiles =  new Tile[width * height];

            for (int xx = 0;xx < width; xx++ ){
                for (int yy = 0; yy < height; yy++){
                    switch (pixels[xx + (yy * width)]){
                        case 0xFFFFFFFF -> {
                            //FLOOR
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            break;
                        } case 0xFF000000 -> {
                            //WALL
                            tiles[xx + (yy * width)] = new Wall(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
                            break;
                        } case 0xFFFF0000 -> {
                            //SPIDER
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            Spider enemy = new Spider(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            Game.entities.add(enemy);
                            Game.enemies.add(enemy);
                            break;
                        } case 0xFF0000FF -> {
                            //STONE
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            Game.entities.add(new Stone(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.STONE_SPRITE));
                            break;
                        } case 0xFFe6ffff -> {
                            //FLOOR WHITH FLOWER BLUE
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_FLOWER2);
                            break;
                        } case 0xFFffe6e6 -> {
                            //FLOOR WHITH FLOWER RED
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_FLOWER1);
                            break;
                        } case 0xFF1a1a1a -> {
                            //FLOOR WHITH FLOWER RED
                            tiles[xx + (yy * width)] = new Wall(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_WHITH_SIGN);
                            break;
                        } case 0xFFff00ff -> {
                            //TARGET
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            SemiWall enemy = new SemiWall(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            Game.entities.add(enemy);
                            Game.enemies.add(enemy);
                            break;
                        } case 0xFF00FFFF -> {
                            //HAMBURGUER
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            Game.entities.add(new Hamburguer(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.HAMBURGUER_SPRITE));
                            break;
                        } case 0xFF00FF00 -> {
                            //WEAPON
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            Game.entities.add(new Weapon(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.WEAPON_SPRITE));
                            break;
                        } case 0xFF7f7f00 ->{
                            //portal
                            tiles[xx + (yy * width)] = new Portal(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_PORTAL_GRASS);
                            Game.portals.add(new PortalCoordinates(xx * TILE_SIZE + (TILE_SIZE / 2), yy * TILE_SIZE + (TILE_SIZE / 2)));
                            break;
                        } case 0xFFFFFF00 -> {
                            //PLAYER
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            Game.player.setX(xx * TILE_SIZE);
                            Game.player.setY(yy * TILE_SIZE);
                            break;
                        }
                        default -> {
                            tiles[xx + (yy * width)] = new Floor(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            System.out.println("WARNING ON TILE X:" + xx + " Y:" + yy + " WAS PLACED A DEFAULT TILE!");
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Maps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
