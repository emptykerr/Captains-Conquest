package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D.Float;
import java.io.BufferedInputStream;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.HelpOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;
import java.awt.image.BufferedImage;
import static utils.Constants.Environment.*;
import java.awt.geom.Rectangle2D;

public class Playing extends State implements Statemethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private HelpOverlay helpOverlay;
    private boolean paused = false;
    private boolean help = false;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;

    // Moving screen
    private int xLvlOffset;
    private int leftBorder = (int) (0.35 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.5 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rndm = new Random();

    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean playerDying = false;

    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rndm.nextInt((int) ((100 * Game.SCALE)));

        calcLvlOffsets();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffsets() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(100 * Game.SCALE, 200 * Game.SCALE, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        helpOverlay = new HelpOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (gameOver) {
            gameOverOverlay.update();
        }

        if (paused) {
            if (help)
                unpauseGame();
            pauseOverlay.update();
        } else if (help) {
            if (paused)
                unhelpGame();

            helpOverlay.update();

        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if(gameOver){
            gameOverOverlay.update();
        } else if(playerDying){
                player.update();
            
        } else if (!gameOver) {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }

    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        if (paused) {
            if (help)
                unpauseGame();
            else {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
                pauseOverlay.draw(g);
            }

        } else if (gameOver) {
            gameOverOverlay.draw(g);
           
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        }

        if (help) {
            if (paused)
                unhelpGame();
            else {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
                helpOverlay.draw(g);
            }
        }

    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 5; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.35), (int) (204 * Game.SCALE),
                    BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.5), smallCloudsPos[i],
                    SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetAll() {
        // TODO: reset playing, enemy, lvl etc.
        
        gameOver = false;
        lvlCompleted = false;
        playerDying = false;
        paused = false;
        help = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();

    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public void checkObjectHit(Rectangle2D.Float attackbox){
        objectManager.checkObjectHit(attackbox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameOver){
            gameOverOverlay.mousePressed(e);
        }
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(gameOver){
            gameOverOverlay.mouseReleased(e);
        }
        if (!gameOver){
            if (paused)
                pauseOverlay.mouseReleased(e);
        else if(lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameOver){
            gameOverOverlay.mouseMoved(e);
        }
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void setLevelComplete(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if(levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void unhelpGame() {
        help = false;
    }

    public void unGameOver() {
        gameOver = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else

            switch (e.getKeyCode()) {
                // case KeyEvent.VK_W:
                // gamePanel.getGame().getPlayer().setUp(true);
                // break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_LEFT:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_DOWN:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_UP:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    unhelpGame();
                    break;
                case KeyEvent.VK_H:
                    help = !help;
                    unpauseGame();
                    break;
                case KeyEvent.VK_SHIFT:
                    player.setSprint(true);
                    break;
                case KeyEvent.VK_CONTROL:
                    player.setDown(false);
                    break;
            }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch (e.getKeyCode()) {
                // case KeyEvent.VK_W:
                // gamePanel.getGame().getPlayer().setUp(false);
                // break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_LEFT:
                    player.setLeft(false);

                case KeyEvent.VK_S:
                    player.setDown(false);
                    break;
                case KeyEvent.VK_DOWN:
                    player.setDown(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_UP:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_SHIFT:
                    player.setSprint(false);
                    break;
                case KeyEvent.VK_CONTROL:
                    player.setDown(false);
                    break;
                // case KeyEvent.VK_TAB:
                // help = !help;
                // break;

            }

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

	public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
	}

    
}
