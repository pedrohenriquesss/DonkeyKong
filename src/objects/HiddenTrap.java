package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class HiddenTrap extends Trap {

	private String name;
	
	public HiddenTrap(Point2D initialPosition) {
		super(initialPosition);
		name = "Wall";
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void changeName() {
		this.name = "Trap";
	}
	
	@Override
	public void interact(GameElement obj, Room room) {
		changeName();
		super.interact(obj, room);
	}

}