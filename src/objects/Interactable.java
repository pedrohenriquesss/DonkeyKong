package objects;

import pt.iscte.poo.game.Room;

public interface Interactable {
	void interact(GameElement obj, Room room);
	boolean isConsumable();
}