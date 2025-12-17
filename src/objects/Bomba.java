package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.*;

public class Bomba extends GameElement implements Interactable {

    private boolean ativa;
    private int tics = 5;

    public Bomba(Point2D initialPosition) {
        super(initialPosition);
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public void drop() {
        this.ativa = true;
        this.setPosition(JumpMan.getInstance(null).getPosition());
    }

    public void tick(Room room) {
        if (ativa) {
            tics--;
            if (tics <= 0)
                explode(room);
        }
    }
    
    public void explode(Room room) {
    	for (Point2D raio : this.getPosition().getWideNeighbourhoodPoints()) {
    		GameElement element = room.getElementAt(raio);
    		GameElement element1 = room.getElementAt(getPosition());
    		if (element != null) {
                if (!element.getName().equals("Stairs") && !element.getName().equals("Floor") && !element.getName().equals("Wall") && !element.getName().equals("JumpMan")) {
                    room.removeElement(element);
                    ImageGUI.getInstance().removeImage(element);
                }
                if (element.getName().equals("JumpMan") || element1.getName().equals("JumpMan")) {
                	JumpMan.getInstance(null).loseLife();
                	JumpMan.getInstance(null).setPosition(room.getStartPositionForJumpMan());
                	ImageGUI.getInstance().setStatusMessage("Foste atingido pela explosão. Vida atual: " + JumpMan.getInstance(null).getVida());
                }
            }
    	}
    	room.removeElement(this);
        ImageGUI.getInstance().removeImage(this);
    }

	@Override
	public void interact(GameElement obj, Room room) {
		if ("JumpMan".equals(obj.getName()) && !ativa) {
	        JumpMan jumpman = (JumpMan) obj;
	        if (!jumpman.hasBomb()) {
	            jumpman.setHasBomb(true);
	            ImageGUI.getInstance().setStatusMessage("Boa! Agora podes bombardear tudo.");
	            room.removeElement(this);
	            ImageGUI.getInstance().removeImage(this);
	        }
	    }
		if (("JumpMan".equals(obj.getName()) || "DonkeyKong".equals(obj.getName()) || "Bat".equals(obj.getName())) && ativa) {
			explode(room);
		}
	}

	@Override
	public boolean isConsumable() {
		return true;
	}

}