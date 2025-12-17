package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.*;

public class Thief extends Movable implements Interactable {

	public Thief(Point2D initialPosition) {
		super(initialPosition);
		setVida(1);
	}

	@Override
	public void interact(GameElement obj, Room room) {
		if (obj.getName().equals("JumpMan")) {
			setVida(0);
			ImageGUI.getInstance().setStatusMessage("Mataste o ladrão!");
		}
	}

	@Override
	public boolean isConsumable() {
		return true;
	}

	@Override
	public void move(Room room) {
		Point2D newPosition = getPosition().plus(Direction.random().asVector());
		GameElement elementAtNewPosition = room.getElementAt(newPosition);		
		if (room.hasInteractableAt(newPosition)) {
            Interactable interactable = room.getInteractableAt(newPosition);
            interactable.interact(this, room);
            if (interactable.isConsumable()) {
                room.removeElement((GameElement) interactable);
            }
        }
		
		if (limites(newPosition) && !room.hasCollision(newPosition) && elementAtNewPosition == null)
			setPosition(newPosition);
	}

	@Override
	public String getName() {
		return "Thief";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

}