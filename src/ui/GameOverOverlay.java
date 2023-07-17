package ui;

import main.Game;
import utils.LoadSave;
import gamestates.Gamestate;
import gamestates.Playing;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;

import java.awt.event.MouseEvent;

import static utils.Constants.UI.URMButtons.*;



public class GameOverOverlay {


    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, replayB;
    private Playing playing;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();
        
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.GAMEOVER_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth()/1.5 * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight()/1.5 * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);

    }
    
    private void createUrmButtons() {
        int menuX = (int) (343 * Game.SCALE);
        int replayX = (int) (417 * Game.SCALE);
        int bY = (int) (230 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
    }

    public void update() {
        menuB.update();
        replayB.update();

    }

    public void draw(Graphics g){
        
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        replayB.draw(g);
    }

    

    

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            playing.setGameState(Gamestate.MENU);
        }
    }


    public void mousePressed(MouseEvent e) {
     
        if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
       

    }

    public void mouseReleased(MouseEvent e) {
      
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
               
                playing.resetAll();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WOOD_CLICK);
                playing.setGameState(Gamestate.MENU);
                // playing.unpauseGame();
                // playing.unGameOver();
            }
        } else if (isIn(e, replayB)) 
            if (replayB.isMousePressed()){
                playing.resetAll();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WOOD_CLICK);
                playing.getGame().getAudioPlayer().setlevelSong(playing.getLevelManager().getLevelIndex());
        }
        
    
        menuB.resetBools();
        replayB.resetBools();
    }

   

    public void mouseMoved(MouseEvent e) {
       

        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
  
 
     
        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        

    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
