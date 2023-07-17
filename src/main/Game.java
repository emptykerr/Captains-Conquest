package main;

import levels.LevelManager;
import ui.AudioOptions;
import ui.GameOptions;
import utils.LoadSave;
import entities.Player;
import gamestates.Editing;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;


import java.awt.Graphics;

import audio.AudioPlayer;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private double timePerFrame = 1000000000.0 / FPS_SET;
    public int FPS_VALUE = FPS_SET;

    private Playing playing;
    private Menu menu;
    private Editing editing;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2.0f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        LoadSave.CreateFolder();
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
        
    }
    
    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        editing = new Editing(this);
        gameOptions = new GameOptions(this);
        
        
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
        
    }

    public void update() {
        switch(Gamestate.state) {
            

            case MENU:
                menu.update();
                break;
               


            case PLAYING:
                playing.update();
                break;


            case OPTIONS:
                gameOptions.update();
                break;


            case QUIT:
            
            

            default:
                System.exit(0);
                break;
            
        }
        
    }

    public void render(Graphics g) {
        switch(Gamestate.state) {

            

            case MENU:
                menu.draw(g);
                break;

                
            case PLAYING:
                playing.draw(g);

                break;

            case EDITING:
                editing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            
            default:
                break;
            
        }
        
        
    }

    @Override
    public void run() {
        System.out.println(FPS_VALUE);
        timePerFrame = 1000000000.0 / FPS_VALUE;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();


        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;


        while(true){

            long currentTime = System.nanoTime();


            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // if(now - lastFrame >= timePerFrame){

            //     gamePanel.repaint();
            //     lastFrame = now;
            //     frames++;
            // }

            
            if(System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS:" + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
    
            }
        }
        
    }

    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    public void setFPS(int fps) {
        this.FPS_VALUE = fps;
        System.out.println("workin fps: " + fps);
        timePerFrame = 1000000000.0 / FPS_VALUE;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Editing getEditor() {
        return editing;
    }

    public GameOptions getGameOptions(){
        return gameOptions;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}