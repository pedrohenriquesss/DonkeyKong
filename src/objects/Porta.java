package objects;

import pt.iscte.poo.utils.Point2D;

public class Porta extends GameElement {

	public Porta(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public String getName() {
		return "DoorClosed";
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