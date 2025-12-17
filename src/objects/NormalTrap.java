package objects;

import pt.iscte.poo.utils.Point2D;

public class NormalTrap extends Trap {

	public NormalTrap(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public String getName() {
		return "Trap";
	}

}