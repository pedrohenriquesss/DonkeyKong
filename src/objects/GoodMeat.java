package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class GoodMeat extends GameElement implements Interactable {

	public GoodMeat(Point2D initialPosition) {
		super(initialPosition);
		setDano(10);
	}

	@Override
	public String getName() {
		return "GoodMeat";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void interact(GameElement obj, Room room) {
		if ("JumpMan".equals(obj.getName())) {
			JumpMan.getInstance(null).setDano(JumpMan.getInstance(null).getDano() + this.getDano());
			ImageGUI.getInstance().setStatusMessage("Boa! Obtiveste nutrientes do bife. Dano atual: " + JumpMan.getInstance(null).getDano());
			ImageGUI.getInstance().removeImage(this);
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