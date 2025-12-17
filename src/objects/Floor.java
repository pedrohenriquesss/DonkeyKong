package objects;

import pt.iscte.poo.utils.Point2D;

public class Floor extends GameElement {

	public Floor(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public String getName() {
		return "Floor";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

}