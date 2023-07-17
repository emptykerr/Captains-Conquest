package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.Game;
import managers.TileManager;
import objects.Tile;
import utils.LoadSave;
import java.awt.image.BufferedImage;


public class Editing extends State implements Statemethods{

    private int[][] lvlData;
    private TileManager tileManager;

    private Tile selectedTile;
    private int mouseX, mouseY;
    private int lastTileX, lastTileY, lastTileID;
    private boolean drawSelect;
    private BufferedImage[] levelSprite;
    private int index;



    public Editing(Game game) {
        super(game);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(Graphics g) {
        drawSelectedTile(g);
    }

    private void drawSelectedTile(Graphics g) {
        if (selectedTile != null && drawSelect) 
            g.drawImage(selectedTile.getSprite(), mouseX, mouseY, Game.TILES_SIZE, Game.TILES_SIZE, null);
        
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
        drawSelect = true;
    }

    private void changeTile(int x, int y) {
        if(selectedTile != null) {
            int tileX = x / 32;
            int tileY = y / 32;

            if(lastTileX == tileX && lastTileY == tileY && lastTileID == selectedTile.getID())
                return;

            lastTileX = tileX;
            lastTileY = tileY;
            lastTileID = selectedTile.getID();

            lvlData[tileY][tileX] = selectedTile.getID();
        }
    }

    


    @Override
    public void mouseDragged(MouseEvent e) {
        e.getX();
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
