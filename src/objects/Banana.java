package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.*;

public class Banana extends Movable implements Interactable {

	public Banana(Point2D initialPosition) {
		super(initialPosition);
		setVida(1);
		setDano(1);
	}

	@Override
	public String getName() {
		return "Banana";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	@Override
	public void move(Room room) {
		
		Point2D belowPosition = getPosition().plus(Direction.DOWN.asVector());
        if (limites(belowPosition)) {
            setPosition(belowPosition);
        } else {
            room.removeElement(this);
        }
        
        if (room.hasInteractableAt(belowPosition) && !(room.getElementAt(belowPosition) instanceof Trap)) {
            Interactable interactable = room.getInteractableAt(belowPosition);
            interactable.interact(this, room);
        }
        
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}

	@Override
	public boolean isConsumable() {
		return true;
	}

	@Override
	public void interact(GameElement obj, Room room) {
		if (obj.getName().equals("JumpMan")) {
			JumpMan.getInstance(null).setVida(JumpMan.getInstance(null).getVida() - getDano());
			JumpMan.getInstance(null).setPosition(room.getStartPositionForJumpMan());
			ImageGUI.getInstance().setStatusMessage("Foste atingido por uma banana! Vida atual: " + JumpMan.getInstance(null).getVida());
			ImageGUI.getInstance().removeImage(this);
		}
	}	

}