package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public abstract class Trap extends GameElement implements Interactable {

	public Trap(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public abstract String getName();

	@Override
	public int getLayer() {
		return 1;
	}
	
	@Override
	public boolean isCollidable() {
        return false;
    }

	@Override
	public void interact(GameElement obj, Room room) {
		if ("JumpMan".equals(obj.getName())) {
			JumpMan.getInstance(null).setDano((JumpMan.getInstance(null).getDano() - 10));
			ImageGUI.getInstance().setStatusMessage("Ups! Caíste na armadilha. Dano atual: " + JumpMan.getInstance(null).getDano());
		}
	}

	@Override
	public boolean isConsumable() {
		return false;
	}

}