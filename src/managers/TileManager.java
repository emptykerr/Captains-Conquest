package managers;

import objects.Tile;
import utils.LoadSave;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

import main.Game;

public class TileManager {
    public Tile GRASS_TOP_LEFT_CORNER_ROCK;     //0
    public Tile GRASS_TOP_MIDDLE;     //1
    public Tile GRASS_TOP_RIGHT_CORNER_ROCK;    //2


    public Tile SINGLE_GRASS_ROCK_ALL;      //39

    public BufferedImage atlas;
    public ArrayList<Tile> tiles = new ArrayList<>();
    private Game game;
    private BufferedImage[] levelSprite;


    public TileManager() {
        this.game = game;
        importOutsideSprites();
        loadAtlas();
        createTiles();
    }


    private void createTiles() {
        tiles.add(GRASS_TOP_LEFT_CORNER_ROCK = new Tile(getSprite(0, 0)));
        tiles.add(GRASS_TOP_MIDDLE = new Tile(getSprite(1, 0)));
        tiles.add(GRASS_TOP_RIGHT_CORNER_ROCK = new Tile(getSprite(2, 0)));

        tiles.add(SINGLE_GRASS_ROCK_ALL = new Tile(getSprite(4, 4)));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int j = 0; j < 4; j++)
            for(int i = 0; i < 12; i++){
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);

            }
}
    private void loadAtlas() {
        atlas = LoadSave.GetSpriteAtlas("outside_sprites");
    }

    public BufferedImage getSprite(int id) {
        return tiles.get(id).getSprite();
    }

    private BufferedImage getSprite(int xCord, int yCord) {
        return atlas.getSubimage(xCord * 32, yCord * 32, 32, 32);
    }
}