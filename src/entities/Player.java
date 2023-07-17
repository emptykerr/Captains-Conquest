package entities;

import java.awt.image.BufferedImage;

import audio.AudioPlayer;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import static utils.Constants.*;



import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import java.awt.Point;


public class Player extends Entity{

    private BufferedImage[][] animations;
    private BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
    private boolean moving = false, attacking = false;
    private boolean left, right, down, jump, sprint;
    private float playerSprint = 1.4f * Game.SCALE;
    private float playerCrouch = 0.5f * Game.SCALE;
    private int normalHitbox = 27;
    private int[][] lvlData;
    protected float xDrawOffset = 21 * Game.SCALE;
    protected float yDrawOffset = 4 * Game.SCALE;

    //jumping / gravity

    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    
    //status bar UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarX = (int) (34 * Game.SCALE);
    private int healthBarY = (int) (14 * Game.SCALE);

    
    private int healthWidth = healthBarWidth;

   

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = Game.SCALE*1.0f;
        loadAnimations(img);
        initHitbox(19, normalHitbox);
        initAttackBox();
        
        
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }
    

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }


    public void update() {
        updateHealthBar();
        if(currentHealth <= 0){
            if(state != DEAD){
                state = DEAD;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if(aniIndex == GetSpriteAmount(DEAD) - 2 && aniTick >= ANI_SPEED -1) {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
               
            } else {
                updateAnimationTick();
            }

            return;
        }

        updateAttackBox();

        updatePos();
        if(moving)
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();

        
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
        
    }


    private void updateAttackBox() {
        if(right) {
            attackBox.x = hitbox.x + hitbox.width + (int) (10 * Game.SCALE);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (10 * Game.SCALE);

        }
        attackBox.y = hitbox.y + (10 * Game.SCALE);
    }


    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }


    public void render(Graphics g, int lvlOffset) {
     
        g.drawImage(animations[state][aniIndex], 
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, 
                (int) (hitbox.y -yDrawOffset), 
                width * flipW, height, null);
        
        // drawHitbox(g, lvlOffset); //hitbox drawing
        // drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    

   
   

   


    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);
    }


    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(state)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {

        int startAni = state;

        if(moving)
            state = RUNNING;
        else 
            state = IDLE;
        
        if(inAir){
            if(airSpeed < 0)
                state = JUMP;
            
            else
                state = FALLING;
        }

        
        
        if(down)
            state = CROUCH;    

        if(attacking){
            state = ATTACK_1;
            if(startAni != ATTACK_1) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if(startAni != state)
            resetAniTick();

    }
    
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }


    private void updatePos() {

        moving = false;
        if(jump)
            jump();
        // if(!left && !right && !inAir)
        //     return;
        if(!inAir)
            if((!left && !right) || (right && left))
                return;
        

        float xSpeed = 0;
        if(sprint && !down){
            walkSpeed = playerSprint;
        }
        else if(down){
            walkSpeed = playerCrouch;
        } else
            walkSpeed = 1.0f * Game.SCALE;
        
        

        if(left){
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
    
        if (right){
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }
        
        


        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData)) 
                inAir = true;
            

        if(inAir) {
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0) 
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else 
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }


    private void resetInAir() {
        inAir = false;
        airSpeed = 0;

    }


    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
       
    }
    

   public void changeHealth(int value){
       currentHealth += value;
       if(currentHealth <= 0) {
           currentHealth = 0;
           //gameOver();
       } else if (currentHealth >= maxHealth) {
           currentHealth = maxHealth;
       }
   }

   public void kill(){
         currentHealth = 0;
   }

   public void changePower(int value){
        System.out.println("power added");
   }

    private void loadAnimations(BufferedImage img) {
            
            animations = new BufferedImage[9][8];

            for(int j = 0; j < animations.length; j++)
                for(int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }


    


    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }


    public void resetDirBooleans() {
        left = false;
        right = false;
        down = false;
        sprint = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }


    public boolean isSprint() {
        return sprint;
    }


    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }


    public boolean isLeft() {
        return left;
    }


    public void setLeft(boolean left) {
        this.left = left;
    }


    public boolean isRight() {
        return right;
    }


    public void setRight(boolean right) {
        this.right = right;
    }


    public boolean isDown() {
        return down;
    }


    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll(){
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public int getTileY(){
        return  tileY;
    }
    
}
