package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile {

	private Point2D position;
	private int dano;
	
	public GameElement(Point2D initialPosition){
		this.position = initialPosition;
	}
	
	@Override
    public Point2D getPosition() {
        return position;
    }
	
	public int getDano() {
		return dano;
	}
	
	public void setDano(int dano) {
		this.dano = dano;
	}
	
	public abstract String getName();	
	public abstract int getLayer();
	public abstract boolean isCollidable();
	
	public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }

}