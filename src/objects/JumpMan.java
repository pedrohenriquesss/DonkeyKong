package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class JumpMan extends Movable implements Interactable {

	private static JumpMan INSTANCE;
	private boolean hasBomb = false;
	private boolean hasSword = false;
	
	private JumpMan(Point2D initialPosition){
		super(initialPosition);
		setVida(3);
		setDano(50);
	}
	
	public static JumpMan getInstance(Point2D initialPosition) {
		if (INSTANCE == null)
			INSTANCE = new JumpMan(initialPosition);
		return INSTANCE;
	}
	
	@Override
	public String getName() {
		return "JumpMan";
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
	public void move(Room room) {
		
		Direction direction = Direction.directionFor(ImageGUI.getInstance().keyPressed());
        Point2D newPosition = getPosition().plus(direction.asVector());
        Point2D belowNewPosition = newPosition.plus(Direction.DOWN.asVector());
        GameElement elementAtNewPosition = room.getElementAt(newPosition);
	    GameElement elementBelowNewPosition = room.getElementAt(belowNewPosition);
	    
	    if (room.hasInteractableAt(newPosition)) {
            Interactable interactable = room.getInteractableAt(newPosition);
            interactable.interact(this, room);
            if (interactable.isConsumable()) {
                room.removeElement((GameElement) interactable);
                ImageGUI.getInstance().removeImage((GameElement) interactable);
            }
            if (elementAtNewPosition.getName().equals("Bat"))
            	return;
        }
	    
	    if (limites(newPosition) && !room.hasCollision(newPosition)) {
	        if (direction == Direction.UP) {
	            if (elementAtNewPosition != null && "Stairs".equals(elementAtNewPosition.getName()) || 
	            		"Floor".equals(elementBelowNewPosition.getName()) || "Stairs".equals(elementBelowNewPosition.getName()))
	                setPosition(newPosition);
	        } else {
	            setPosition(newPosition);
	        }
	    }
	    	
	}
	
	public void fall(Room room) {
		
	    Point2D belowPosition = getPosition().plus(Direction.DOWN.asVector());
	    GameElement belowElement = room.getElementAt(belowPosition);

	    if (belowElement == null)
	    	setPosition(belowPosition);
	    else if (belowElement.getName().equals("Trap") || belowElement instanceof HiddenTrap) {
	    	((Trap)belowElement).interact(this, room);
	    	setPosition(belowPosition);
	    } else if (belowElement.getName().equals("Floor"))
	    	setPosition(belowPosition); 
	        
	}

	@Override
	public void interact(GameElement obj, Room room) {
		
		if ("DonkeyKong".equals(obj.getName())) {
			setVida(getVida() - obj.getDano());
	    	ImageGUI.getInstance().setStatusMessage("Atacado pelo gorila! Vida atual: " + getVida());
	    	setPosition(room.getStartPositionForJumpMan());
		}
		
		if ("Bat".equals(obj.getName())) {
	        setVida(getVida() - obj.getDano());
	        setPosition(room.getStartPositionForJumpMan());
	        ImageGUI.getInstance().setStatusMessage("Atingido pelo morcego. Vida atual: " + getVida());
	        ImageGUI.getInstance().removeImage(obj);
	    }
		
		if (obj.getName().equals("Banana")) {
			setVida(getVida() - obj.getDano());
			setPosition(room.getStartPositionForJumpMan());
			ImageGUI.getInstance().setStatusMessage("Foste atingido por uma banana! Vida atual: " + getVida());
			ImageGUI.getInstance().removeImage(obj);
		}
		
		if (obj instanceof Thief) {
			if (hasSword == true) {
				setHasSword(false);
				setDano(getDano() - 10);
				((Thief)obj).setVida(0);
				ImageGUI.getInstance().setStatusMessage("O ladrão roubou-te a espada! Dano atual: " + this.getDano());
			} else {
				((Thief)obj).setVida(0);
				ImageGUI.getInstance().setStatusMessage("O ladrão tentou-te roubar mas não tinhas nada! Ele morre");
			}
		}
		
	}
	
	@Override
	public boolean isConsumable() {
		return false;
	}
	
	public void loseLife() {
        setVida(getVida()-1);
	}
	
	public void gainLife() {
		setVida(getVida()+1);
	}

	public boolean hasBomb() {
		return hasBomb;
	}

	public void setHasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}
	
	public boolean hasSword() {
		return hasSword;
	}

	public void setHasSword(boolean hasSword) {
		this.hasSword = hasSword;
	}
			
}