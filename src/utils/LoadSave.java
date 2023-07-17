package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Crabby;

import java.awt.Color;

import main.Game;
import static utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {

    public static String homePath = System.getProperty("user.home");
    public static String saveFolder = "MatthewsGame";
    public static String levelFile = "/resources/level_one_data_long.png";
    public static String filePath = homePath + File.separator + saveFolder + File.separator + levelFile;
    private static File lvlFile = new File(filePath);



    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String PLAYER_ATLAS_REVERSED = "player_sprites_reversed.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";

    public static final String LEVEL_ATLAS = "outside_sprites.png";
    //public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";

    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTOMS = "volume_buttons.png";
    public static final String BUTTON_ATLAS = "button_atlas.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String OPTIONS_MENU = "options_background.png";

    public static final String MENU_WALLPAPER = "background_menu.png";
    public static final String WALLPAPER_DAY = "BackgroundDay.jpg";
    public static final String WALLPAPER_SUNSET = "BackgroundEvening.jpg";
    public static final String WALLPAPER_NIGHT = "BackgroundNight.jpg";

    public static final String HELP_MENU = "keybind_menu.png";
    public static final String GAMEOVER_BACKGROUND = "gameover_background.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";

    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";

    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";

    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";



    public static void CreateFolder() {
        File folder = new File(homePath + File.separator + saveFolder);
        if (!folder.exists())
            folder.mkdir();
    }

    public static void CreateLevel (int[][] lvlData) {
        if(lvlFile.exists()) {
            System.out.println("File: " + lvlFile + " already exists!");
            return;
        } else {
            try {
                lvlFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/resources/" + fileName);

        try {
            img = ImageIO.read(is);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/resources/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for(int i = 0; i < filesSorted.length; i++) 
            for(int j = 0; j < files.length; j++) {
                if(files[j].getName().equals((i+1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }
 
        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }

   

    
}
