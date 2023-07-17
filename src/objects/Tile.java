package objects;

import java.awt.image.BufferedImage;

import utils.LoadSave;


public class Tile {

    private BufferedImage[] levelSprite;
    private int index;
    private BufferedImage sprite;
    public Tile(BufferedImage sprite) {
        this.sprite = sprite;
    }

   public BufferedImage getSprite() {
       return sprite;
   }

   public int getID() {
    BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
    levelSprite = new BufferedImage[48];
    for(int j = 0; j < 4; j++)
        for(int i = 0; i < 12; i++){
            index = j*12 + i;
            levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            

        }
        return index;
}

    
}
