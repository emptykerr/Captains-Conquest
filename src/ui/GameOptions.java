package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.URMButtons.*;
import static utils.Constants.UI.VolumeButtons.*;

import java.awt.image.BufferedImage;

import audio.AudioPlayer;

public class GameOptions extends State implements Statemethods{
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;
    private VolumeButtons framerateButton;

    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void createFramerateButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) ((278 -50) * Game.SCALE);
        framerateButton = new VolumeButtons(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void loadButton() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) ((325)* Game.SCALE);

        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
        createFramerateButton();
      
    }

    private void loadImgs() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.WALLPAPER_NIGHT);
        optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

        bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);

    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();   
        framerateButton.update();     
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);    
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);     
        menuB.draw(g);
        audioOptions.draw(g);
        framerateButton.draw(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(framerateButton.isMousePressed()) {
            float valueBefore = framerateButton.getFloatValue();
            framerateButton.changeX(e.getX());
            float valueAfter = framerateButton.getFloatValue();
            
            if(valueBefore != valueAfter) {
                int fps = (int) (valueAfter * 120 + 60);
                game.setFPS(fps);
                
            }
        } else
        audioOptions.mouseDragged(e); 
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if(isIn(e, framerateButton)) {
            framerateButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
    }
}

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)) {
            if(menuB.isMousePressed()) {
                game.getAudioPlayer().playEffect(AudioPlayer.WOOD_CLICK);
                Gamestate.state = Gamestate.MENU;
                
            }
            } else if (isIn(e, framerateButton)) {
                if(framerateButton.isMousePressed()) {
                    // Game.framerate = framerateButton.getValue();
                    // Game.fps = Game.framerate;
                }
            } else {
                audioOptions.mouseReleased(e);
            }
       
            framerateButton.resetBools();
            menuB.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        framerateButton.setMouseOver(false);
        if(isIn(e, menuB)) 
            menuB.setMouseOver(true);   
        else if (isIn(e, framerateButton)) 
            framerateButton.setMouseOver(true);
        else 
            audioOptions.mouseMoved(e);     
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
