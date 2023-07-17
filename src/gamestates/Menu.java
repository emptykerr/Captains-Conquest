package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import audio.AudioPlayer;



public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg, wallpaperBackground;
    private int menuX, menuY, menuWidth, menuHeight;
    private Date currentDate;
    private SimpleDateFormat hour;
    
    public Menu(Game game) {
        super(game);
        loadButtons();   
        loadBackground();
        loadTimeImage();
    
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) ( 45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamestate.QUIT);

    }

    @Override
    public void update() {
        for(MenuButton mb : buttons)
            mb.update();        
    }

    @Override
    public void draw(Graphics g) {
        
        g.drawImage(wallpaperBackground, 0, 0, (int) (wallpaperBackground.getWidth() * Game.SCALE), (int) (wallpaperBackground.getHeight() * Game.SCALE), null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for(MenuButton mb : buttons)
            mb.draw(g);
    }

    private void loadTimeImage() {
        hour = new SimpleDateFormat("HH");
        currentDate = new Date();
        int hourInt = Integer.parseInt(hour.format(currentDate));
        if (hourInt > 20 || hourInt <= 5) {
            wallpaperBackground = LoadSave.GetSpriteAtlas(LoadSave.WALLPAPER_NIGHT);
        } else if (hourInt >= 18) {
            wallpaperBackground = LoadSave.GetSpriteAtlas(LoadSave.MENU_WALLPAPER);
        } else if (hourInt >= 17){
            wallpaperBackground = LoadSave.GetSpriteAtlas(LoadSave.WALLPAPER_SUNSET);
        } else if (hourInt >= 10){
            wallpaperBackground = LoadSave.GetSpriteAtlas(LoadSave.WALLPAPER_DAY);
        } else if (hourInt > 5){
            wallpaperBackground = LoadSave.GetSpriteAtlas(LoadSave.WALLPAPER_DAY);
        }
        System.out.println(currentDate);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isIn(e,mb)) {
                mb.setMousePressed(true);
                    break;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isIn(e,mb)) {
                if(mb.isMousePressed()){
                    mb.applyGameState();
                    game.getAudioPlayer().playEffect(AudioPlayer.WOOD_CLICK);
                }
                if(mb.getState() == Gamestate.PLAYING){
                    game.getAudioPlayer().setlevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                    game.getAudioPlayer().playEffect(AudioPlayer.WOOD_CLICK);
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton mb : buttons)
            mb.resetBools();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
            mb.setMouseOver(false);
        for(MenuButton mb : buttons)
            if(isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;

        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
