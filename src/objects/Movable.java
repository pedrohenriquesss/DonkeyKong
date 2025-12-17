package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement {

	private int vida;
	
	public Movable(Point2D initialPosition) {
		super(initialPosition);
	}
	
	public int getVida() {
		return vida;
	}
	
	public void setVida(int vida) {
		this.vida = vida;
	}
	
	public abstract void move(Room room);
	
	public boolean limites(Point2D position) {		
		return position.getX() < 10 && position.getY() < 10 && 
				position.getX() >=0 && position.getY() >= 0;	
	}

}