package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Gorila extends Movable implements Interactable {

	public Gorila(Point2D initialPosition) {
		super(initialPosition);
		setVida(100);
		setDano(1);
	}

	@Override
	public String getName() {
		return "DonkeyKong";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	@Override
	public boolean isCollidable() {
        return true;
    }
	
	@Override
	public void move(Room room) {
		
		double probabilidade = Math.min(0.2 + GameEngine.getInstance().getNivel()*0.1, 1);
		Direction direction;
	    if (Math.random() < probabilidade)
	    	direction = (JumpMan.getInstance(null).getPosition().getX() - getPosition().getX()) > 0 ? Direction.RIGHT : Direction.LEFT;
	    else
	    	direction = Direction.random();
	    
	    Point2D newPosition = getPosition().plus(direction.asVector());
		GameElement elementAtNewPosition = room.getElementAt(newPosition);		
		if (room.hasInteractableAt(newPosition)) {
            Interactable interactable = room.getInteractableAt(newPosition);
            interactable.interact(this, room);
            if (interactable.isConsumable() && !((GameElement)interactable).getName().equals("Bat")) {
                room.removeElement((GameElement) interactable);
            }
        }
		
		if (limites(newPosition) && !room.hasCollision(newPosition) && 
				(elementAtNewPosition == null || !"Stairs".equals(elementAtNewPosition.getName())))
			setPosition(newPosition);
		
		if (direction == Direction.UP || direction == Direction.DOWN)
			room.addElement(new Banana(getPosition().plus(Direction.DOWN.asVector())));
		
	}

	@Override
	public void interact(GameElement obj, Room room) {		
		if ("JumpMan".equals(obj.getName())) {
			setVida(getVida() - obj.getDano());
        	ImageGUI.getInstance().setStatusMessage("Atacaste o gorila! Vida atual dele: " + getVida());
		}
	}
	
	@Override
	public boolean isConsumable() {
		return false;
	}

}