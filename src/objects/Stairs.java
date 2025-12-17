package objects;

import pt.iscte.poo.utils.Point2D;

public class Stairs extends GameElement {

	public Stairs(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public String getName() {
		return "Stairs";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

}