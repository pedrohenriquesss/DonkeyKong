package pt.iscte.poo.game;

import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

import java.io.*;
import java.util.*;

public class Room {
	
	private Point2D startPosition;
	private List<GameElement> elements = new ArrayList<>();
	private List<Movable> movables = new ArrayList<>();

    public Room(File f) {
        elements = fileToObj(f);
    }

    public void moveCharacters() {
    	
    	List<Movable> copyOfMovables = new ArrayList<>(movables); 
        List<Movable> toRemove = new ArrayList<>();
        
        for (Movable personagem : copyOfMovables) {
            if (personagem.getVida() <= 0) {
                toRemove.add(personagem);
                if (personagem.getName().equals("DonkeyKong"))
                	ImageGUI.getInstance().setStatusMessage("Derrotaste um gorila!");
            }
            else
                personagem.move(this);
        }
        
        movables.removeAll(toRemove);
        elements.removeAll(toRemove);
        ImageGUI.getInstance().removeImages(toRemove);
            
    }
    
    public JumpMan getJumpMan() {
        return JumpMan.getInstance(null);
    }
    
    public void moveJumpMan() {
    	JumpMan.getInstance(null).move(this);
    }

    public List<GameElement> getElements() {
        return elements;
    }
    
    public List<Movable> getMovables() {
        return movables;
    }
    
    public boolean hasCollision(Point2D position) {
        
    	for (GameElement element : elements) 
            if (element.isCollidable() && element.getPosition().equals(position)) 
                return true;
            
        return false;
        
    }
    
    public GameElement getElementAt(Point2D position) {
		
		for (GameElement element : elements)
	        if (element.getPosition().equals(position))
	            return element;
	      
	    return null;
	    
	}
    
    public List<GameElement> fileToObj (File f) {
		
		List<GameElement> elements = new ArrayList<>();
		try {
			Scanner sc = new Scanner(f);
			int y = 0;
	        sc.nextLine();
	        while (sc.hasNextLine()) {
	        	String line = sc.nextLine();
	            if (line.length() < 10) {
	            	line = "";
                    System.err.println("Linha incompleta no ficheiro " + f.getName() + ", linha " + (y + 1));
                    for (int i = 0; i <= 9; i++)
                    	line += " ";
                }
	            for (int x = 0; x < line.length(); x++) {
	                char symbol = line.charAt(x);
	                Point2D position = new Point2D(x, y);
	                ImageGUI.getInstance().addImage(new Floor(position));
	                switch (symbol) {
	                    case 'W':
	                        elements.add(new Wall(position));
	                        break;
	                    case 'S':
	                        elements.add(new Stairs(position));
	                        break;
	                    case 's':
	                        elements.add(new Espada(position));
	                        break;
	                    case 'H':
	                    	startPosition = position;
	                    	elements.add(JumpMan.getInstance(position));
	                        break;
	                    case 'G':
	                    	Gorila kong = new Gorila(position);
	                    	elements.add(kong);
	                    	movables.add(kong);
	                        break;
	                    case 't':
	                    	elements.add(new NormalTrap(position));
	                    	break;
	                    case 'T':
	                    	elements.add(new HiddenTrap(position));
	                    	break;
	                    case 'm':
	                    	elements.add(new GoodMeat(position));
	                    	break;
	                    case 'P':
	                    	elements.add(new Princesa(position));
	                    	break;
	                    case '0':
	                    	elements.add(new Porta(position));
	                    	break;
	                    case 'b':
	                    	elements.add(new Bomba(position));
	                    	break;
	                    case 'M':
	                    	Morcego morcego = new Morcego(position);
	                    	elements.add(morcego);
	                    	movables.add(morcego);
	                    	break;
	                    case 'L':
	                    	Thief ladrao = new Thief(position);
	                    	elements.add(ladrao);
	                    	movables.add(ladrao);
	                    case ' ':
	                    	break;
	                    default:
	                    	System.out.println("Caracter desconhecido na posição: (" + x + ", " + y + ")");
	                    	break;	                    
	                }
	            }
	            y++;
	        }
	        sc.close();
	        if (y != 10) {
                System.out.println("Falta uma linha no ficheiro. Jogo abortado");
                ImageGUI.getInstance().dispose();
                System.exit(1);
            }
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado. Indique o nome do ficheiro: ");
			Scanner sc = new Scanner(System.in);
			String ficheiro = sc.nextLine();
			sc.close();
	        return fileToObj(new File(ficheiro));
		}

        return elements;
	
	}
    
    public void drawRoom() {
    	
    	for (GameElement element : elements)
    		ImageGUI.getInstance().addImage(element);
        
    }
    
    public void addElement(GameElement obj) {
		elements.add(obj);
		if (obj instanceof Movable)
			movables.add((Movable)obj);
		ImageGUI.getInstance().addImage(obj);
	}
	
	public void removeElement(GameElement obj) {
		
		elements.remove(obj);
        movables.remove(obj);
        ImageGUI.getInstance().removeImage(obj);
	    
	}
	
	public Interactable getInteractableAt(Point2D position) {
		
	    for (GameElement element : elements)
	        if (element instanceof Interactable && element.getPosition().equals(position))
	            return (Interactable) element;
	    
	    return null;
	    
	}

	public boolean hasInteractableAt(Point2D position) {
	    return getInteractableAt(position) != null;
	}

	public Point2D getStartPositionForJumpMan() {
		return startPosition;
	}
	
	public Bomba getBomba() {
		for (GameElement element : elements)
			if (element.getName().equals("Bomb"))
				return (Bomba)element;
		return null;
	}
	
}