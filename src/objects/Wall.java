package objects;

import pt.iscte.poo.utils.Point2D;

public class Wall extends GameElement {

	public Wall(Point2D initialPosition) {
		super(initialPosition);
	}

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	@Override
	public boolean isCollidable() {
        return true;
    }
	
}