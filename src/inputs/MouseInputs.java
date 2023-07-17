package inputs;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

import java.awt.event.MouseEvent;

public class MouseInputs implements MouseListener,MouseMotionListener {

    private GamePanel gamePanel;
    
    public MouseInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (Gamestate.state) {
        case PLAYING:
            gamePanel.getGame().getPlaying().mouseDragged(e);
            break;
        case EDITING:
            gamePanel.getGame().getEditor().mouseDragged(e);
        case OPTIONS:
            gamePanel.getGame().getGameOptions().mouseDragged(e);
        default:
            break;
        }       
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case EDITING:
                gamePanel.getGame().getEditor().mouseMoved(e);
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseMoved(e);
            default:
                break;
            
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(Gamestate.state) {
            
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
            case EDITING:
                gamePanel.getGame().getEditor().mouseClicked(e);
            default:
                break;
            
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case EDITING:
                gamePanel.getGame().getEditor().mousePressed(e);
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mousePressed(e);
            default:
                break;
            
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case EDITING:
                gamePanel.getGame().getEditor().mouseReleased(e);
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseReleased(e);
            default:
                break;
            
        }
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
