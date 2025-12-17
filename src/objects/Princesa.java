package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Princesa extends GameElement implements Interactable {

	public Princesa(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public String getName() {
		return "Princess";
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
	public boolean isConsumable() {
		return false;
	}

	@Override
	public void interact(GameElement obj, Room room) {
		if ("JumpMan".equals(obj.getName()))
			GameEngine.getInstance().endgame();
	}
	
}