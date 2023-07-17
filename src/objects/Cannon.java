package objects;

import main.Game;

public class Cannon extends GameObject{

    private int tileY;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        hitbox.x -= (int)(Game.SCALE * 4);
        hitbox.y += (int)(Game.SCALE * 6);
    }

    public void update() {
        if(doAnimation)
            updateAnimationTick();
    }

    public int getTileY(){
        return tileY;
    }
    
}
