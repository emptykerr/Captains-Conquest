package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import gamestates.Playing;
import utils.LoadSave;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class HelpOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;

    public HelpOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.HELP_MENU);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);


    }

    public void update() {
        
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        //Sound Buttons
       

    }
}
