package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Espada extends GameElement implements Interactable {

	public Espada(Point2D initialPosition) {
		super(initialPosition);
		setDano(10);
	}

	@Override
	public String getName() {
		return "Sword";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void interact(GameElement obj, Room room) {
		if (obj instanceof JumpMan) {
			JumpMan.getInstance(null).setDano(JumpMan.getInstance(null).getDano() + this.getDano());
        	ImageGUI.getInstance().setStatusMessage("Apanhaste uma espada! Dano atual que podes causar: " + JumpMan.getInstance(null).getDano());
        	ImageGUI.getInstance().removeImage(this);
        	((JumpMan) obj).setHasSword(true);
        }		
	}

	@Override
	public boolean isConsumable() {
		return true;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

}