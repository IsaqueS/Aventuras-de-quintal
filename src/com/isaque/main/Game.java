package com.isaque.main;

import com.isaque.Entities.Enemies;
import com.isaque.Entities.Entity;
import com.isaque.Entities.Player;
import com.isaque.Entities.Projectiles;
import com.isaque.Graphics.Pause;
import com.isaque.Graphics.SpriteSheet;
import com.isaque.Graphics.PlayerUI;
import com.isaque.maps.Maps;
import com.isaque.maps.PortalCoordinates;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

	public static JFrame frame;
        public static final Dimension screenResolution = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	public static final double screenW = screenResolution.width, screenH = screenResolution.height;
        public static final double proportion = proportionCalc();
        public static final int DHEIGHT = 160;
        public static final int HEIGTH = resCalcH();
        public static final int WIDTH = resCalcW();       
        public static final int WWIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
        public static final int WHEIGTH = Toolkit.getDefaultToolkit().getScreenSize().height;     
        private static boolean isVertical = false;
	public static final double SCALE_W = screenW / WIDTH;
        public static final double SCALE_H = screenH / HEIGTH;
	private final boolean wResizable = false;
	private final boolean wVisible = true;
        private final boolean wUndecorated = true;	
	public static final String msgStart = "Game iniciado";
        public static final String msgWindow = "Aventuras de quintal";
        private boolean isPaused = false, gameOver = false, skipGameOver = false;
        private boolean isRealease = true;
        public static boolean isStarted = false;
        public static boolean isReadyToLoad = false;
        private Graphics g;
        private BufferStrategy bs;
        
	private Thread thread;	
	private static boolean isRunning = true;	
	public static BufferedImage image;
        
        public static ArrayList<Entity> entities;
        public static ArrayList<Enemies> enemies;  
        public static ArrayList<Projectiles> playerProjectiles;
        public static ArrayList<Projectiles> enemyProjectiles;
        public static ArrayList<PortalCoordinates> portals;
        
        public static Player player;      
        public static SpriteSheet spritesheet;       
        public static Maps maps;        
        public static Random random;
        public static PlayerUI playerUI;
        public static Pause pause;
	
	public Game() {            
            /*resolutionSettings: {
                System.out.println(screenResolution.width);
                System.out.println(screenResolution.height);
                //double screenW = screenResolution.width, screenH = screenResolution.height;
                System.out.println(proportion);
                System.out.println(WIDTH);
                System.out.println(HEIGTH);
                
            }*/
            //load
            portals = new ArrayList<PortalCoordinates>();
            image = new BufferedImage(WIDTH, HEIGTH, BufferedImage.TYPE_INT_RGB);
            random = new Random();           
            entities = new ArrayList<Entity>();
            enemies = new ArrayList<Enemies>();
            playerProjectiles = new ArrayList<Projectiles>();
            enemyProjectiles = new ArrayList<Projectiles>();
            spritesheet = new SpriteSheet("/textures/SpriteSheet.png", "/textures/icon.png"); 
            player = new Player(0,0,16,16);
            entities.add(player);
            playerUI = new PlayerUI();
            maps = new Maps("/maps/");
            maps.loadLevel();
            pause = new Pause();
            //load   
            addKeyListener(this);
            addMouseListener(this);
            //setPreferredSize(new Dimension(WIDTH * SCALE, HEIGTH * SCALE));
            
	}
	
	public void startFrame() {
            //setPreferredSize(new Dimension(SWIDTH, SHEIGTH));
            setPreferredSize(screenResolution);
            frame = new JFrame(msgWindow);
            frame.add(this);
            frame.setUndecorated(wUndecorated);
            frame.setResizable(wResizable);
       	    frame.pack();
      	    frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setIconImage(spritesheet.getIcon());
	    frame.setVisible(wVisible);
                
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public static synchronized void stop() {
		//this.setVisible(false);
                isRunning = false;
		System.out.println("Progama encerrado");
                Runtime.getRuntime().exit(0);
		//System.out.println("Progama encerrado");
	}
	
	
	public static void main(String[] args) {
		System.out.println(msgStart);
		Game game = new Game();
		game.start();
		
		
	}
	
	public void run() {
		
		long lastLoopTime = System.nanoTime();
		final long amontOfTicks = 60;
		final long ns = 1000000000 / amontOfTicks;
		double delta = 0;		
		int frames = 0;
		double timer = System.currentTimeMillis();
                long now;

                startFrame();
                requestFocus();
                                
		while (isRunning) {
			
			now = System.nanoTime();
			delta += (now - lastLoopTime) / (double)ns;
			lastLoopTime = now;
                        
			
			if (delta >= 1) {				                            				
				g = Game.image.getGraphics();
                                bs = this.getBufferStrategy();
                                frames++;
				delta--;
                                if (!isPaused() && !isReadyToLoad){
                                    tick();
                                    render();
                                } else if (isReadyToLoad){
                                    Game.maps.nextLevel();
                                    Game.reload();
                                    isReadyToLoad = false;
                                } else if (isPaused){
                                    pause.tick();
                                    pause.render(g, bs);
                                } else if (gameOver){
                                    
                                }
                                                   
			} else {
                           try {
                                Thread.sleep((lastLoopTime - System.nanoTime() + ns) / 10000000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer+=1000;
			}
			
		}
			stop();
		
	
	}
	
	public void tick() {
            for (int i = 0; i < entities.size(); i++){
                Entity e = entities.get(i);
                e.tick();
            }
            for (int i = 0; i < playerProjectiles.size(); i++){
                Projectiles e = playerProjectiles.get(i);
                e.tick();
            }	
	}
	
	public void render() {
				
		if (bs == null) {			
			this.createBufferStrategy(3);
			return;
		}		

		// Rendeliza��o do jogo
		
                g.setColor(Color.WHITE);
                g.fillRect(0, 0,WIDTH,  HEIGTH);
                    
                maps.render(g);
                    
                for (int i = 0; i < playerProjectiles.size(); i++){
                    Projectiles e = playerProjectiles.get(i);
                    e.render(g);
                }
                for (int i = 0; i < entities.size(); i++){
                     Entity e = entities.get(i);
                     e.render(g);
                     //e.renderCollisionBox(g, Color.BLUE);
                }
                playerUI.render(g);
                
		// fim do render
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WWIDTH, WHEIGTH, null);
		bs.show();
		
	}

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    //Evento ao apertar
    public void keyPressed(KeyEvent e) {
        //switch usando novidade do JDK 14
        switch (e.getKeyCode()){
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.left = true;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.right = true;
            case KeyEvent.VK_W, KeyEvent.VK_UP -> player.up = true;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> player.down = true;
            case KeyEvent.VK_ENTER -> skipGameOver = true;
            case KeyEvent.VK_ESCAPE -> {
                if (isRealease){
                    isPaused = !isPaused;
                }
                isRealease = false;
            }
            case KeyEvent.VK_F1 -> stop();
        }
        
    }

    @Override
    //Evento a soutar a tecla
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.left = false;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.right = false;
            case KeyEvent.VK_W, KeyEvent.VK_UP -> player.up = false;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> player.down = false;
            case KeyEvent.VK_ESCAPE -> isRealease = true;
            case KeyEvent.VK_ENTER -> skipGameOver = false;
        }
    }
    
    private static int resCalcW(){
        if (isVertical){
            //System.out.println("W1");
            return (int)(DHEIGHT * proportion);
        } 
        //System.out.println("W2");
        return DHEIGHT;
    }
    
    private static int resCalcH(){
        if (!isVertical){
            //System.out.println("H1");
            return (int)(DHEIGHT * proportion);
        }
        //System.out.println("H2");
        return DHEIGHT;
    }
    
    private static double proportionCalc(){
        double p = (screenH / screenW);
        if (p > 1){
            //System.out.println("-->");
            //System.out.println(p);
            //System.out.println(isVertical);
            return p;
            
        }
        //System.out.println("<--");
        
        isVertical = true;
        //System.out.println(isVertical);
        
        return (screenW / screenH);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.isFocusOwner()){
            switch (e.getButton()){
                case MouseEvent.BUTTON1 -> {
                    player.mouseShoot = true;
                    player.mouseX = (int) (e.getXOnScreen() / SCALE_W);
                    player.mouseY = (int) (e.getYOnScreen() / SCALE_H);
                } 
                case MouseEvent.BUTTON2 -> player.specialAttack = true;
                case MouseEvent.BUTTON3 -> player.jump = true;
            }
        }        

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()){
            case MouseEvent.BUTTON1 -> player.mouseShoot = false;
            case MouseEvent.BUTTON2 -> player.specialAttack = false;
            case MouseEvent.BUTTON3 -> player.jump = false;
        }      
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    private boolean isPaused(){
        if (!this.isFocusOwner() && isStarted){
            isPaused = true;
            return true;
        }
        return isPaused;    
    }
    
    public static void reload(){
            //load
            //Thread.sleep(1000);
            portals = new ArrayList<PortalCoordinates>();           
            entities = new ArrayList<Entity>();
            enemies = new ArrayList<Enemies>();
            playerProjectiles = new ArrayList<Projectiles>();
            enemyProjectiles = new ArrayList<Projectiles>();
            player = new Player(0,0,16,16);
            entities.add(player);
            Game.maps.loadLevel();
            //load   
    }
}
