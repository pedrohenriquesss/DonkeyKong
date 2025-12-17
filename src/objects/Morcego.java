package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Morcego extends Movable implements Interactable {

	public Morcego(Point2D initialPosition) {
		super(initialPosition);
		setVida(1);
	}

	@Override
	public String getName() {
		return "Bat";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	@Override
	public void move(Room room) {
		
		Point2D newPosition = getPosition().plus(Direction.random().asVector());
	    Point2D belowCurrentPosition = getPosition().plus(Direction.DOWN.asVector());
	    GameElement elementBelow = room.getElementAt(belowCurrentPosition);
	    GameElement elementAtNewPosition = room.getElementAt(newPosition);
	    JumpMan jumpman = JumpMan.getInstance(null);

		if (newPosition.equals(jumpman.getPosition()))
	    	this.interact(jumpman, room);
		
		if (room.hasInteractableAt(newPosition)) {
            Interactable interactable = room.getInteractableAt(newPosition);
            interactable.interact(this, room);
            if (interactable.isConsumable()) {
                room.removeElement((GameElement) interactable);
            }
        }
		
	    if (elementBelow != null && "Stairs".equals(elementBelow.getName())) {
	        newPosition = belowCurrentPosition;
	    }

        if (limites(newPosition) && !room.hasCollision(newPosition) && elementAtNewPosition == null && !(elementAtNewPosition instanceof Trap)) {
            setPosition(newPosition);
        }
		
		
	}

	@Override
	public void interact(GameElement obj, Room room) {
		
		if ("JumpMan".equals(obj.getName())) {
			JumpMan.getInstance(null).setVida(JumpMan.getInstance(null).getVida() - 1);
			JumpMan.getInstance(null).setPosition(room.getStartPositionForJumpMan());
			ImageGUI.getInstance().setStatusMessage("Atingido pelo morcego. Vida atual: " + JumpMan.getInstance(null).getVida());
			setVida(0);
        	ImageGUI.getInstance().removeImage(this);
		}
		
	}

	@Override
	public boolean isConsumable() {
		return true;
	}

	@Override
	public boolean isCollidable() {
		return true;
	}
	
}