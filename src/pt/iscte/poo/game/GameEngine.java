package pt.iscte.poo.game;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private int nivel = 0;
	private final static File[] ficheiros;
	private List<Room> niveis = new ArrayList<>();
	private List<HighScore> scores = new ArrayList<>();
	private final File leaderboard = new File("highscores.txt");
	private static GameEngine instance;
	
	public GameEngine() {
		niveis = loadRooms();
	    if (!niveis.isEmpty()) {
	        currentRoom = niveis.get(0);
	        currentRoom.drawRoom();
	    }
	    loadScores();
	    ImageGUI.getInstance().update();
	}
	
	public static GameEngine getInstance() {
		if (instance == null)
			instance = new GameEngine();
		return instance;
	}
	
	public int getNivel() {
		return nivel;
	}
	
	static {
		
		String path = System.getProperty("user.dir");
		File roomsDir = new File(path, "rooms");
		if (roomsDir.exists() && roomsDir.isDirectory())
			ficheiros = roomsDir.listFiles();
		else {
			System.err.println("A pasta 'rooms' não foi encontrada no diretório");
			ficheiros = new File[0];
		}
		
	}

	@Override
	public void update(Observed source) {
		
		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			System.out.println("Keypressed " + k);
			if (Direction.isDirection(k)) {
				System.out.println("Direction! ");
				currentRoom.moveJumpMan();
			} else if (k == 66 && JumpMan.getInstance(null).hasBomb()) {
				Bomba bomba = new Bomba(JumpMan.getInstance(null).getPosition());
	            bomba.drop();
	            currentRoom.addElement(bomba);
	            JumpMan.getInstance(null).setHasBomb(false);
	       } 
		}
			
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

	private void processTick() {
		
		JumpMan jumpman = JumpMan.getInstance(null);
		currentRoom.getJumpMan().fall(currentRoom);		
		
		if (currentRoom.getBomba() != null)
			currentRoom.getBomba().tick(currentRoom);		
		
		if (lastTickProcessed % 10 == 0) {
			List<GameElement> elementsToTransform = new ArrayList<>();
		    for (GameElement element : currentRoom.getElements())
		        if (element.getName().equals("GoodMeat"))
		            elementsToTransform.add(element);      
		    
		    for (GameElement goodMeat : elementsToTransform) {
		    	BadMeat badMeat = new BadMeat(goodMeat.getPosition());
		    	currentRoom.removeElement(goodMeat);
		        currentRoom.addElement(badMeat);
		    }		    
		}
		
		currentRoom.moveCharacters();
		
		if (jumpman.getVida() <= 0 || jumpman.getDano() <= 0) {
			reset();
            return;
        }
		
		if (currentRoom.getElementAt(currentRoom.getJumpMan().getPosition()).getName().equals("DoorClosed")) {
			nivel++;
	        if (nivel < niveis.size()) {
	            for (GameElement element : currentRoom.getElements()) {
	                ImageGUI.getInstance().removeImage(element);
	            }
	            currentRoom = niveis.get(nivel);
	            currentRoom.drawRoom();
	            ImageGUI.getInstance().setStatusMessage("Nível " + (nivel+1) + "!");
	            JumpMan.getInstance(null).setPosition(currentRoom.getStartPositionForJumpMan());
	        } else {
	            ImageGUI.getInstance().setStatusMessage("Parabéns! Terminaste o jogo!");
	        }
	    }
			
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
	}
	
	public static List<Room> loadRooms() {
	    
		List<Room> niveis = new ArrayList<>();
		for (File f : ficheiros)
	        niveis.add(new Room(f));
	        
	    return niveis;
	    
	}
	
	private void reset() {
		ImageGUI.getInstance().removeImages(currentRoom.getElements());
	    JumpMan.getInstance(null).setVida(3);
	    JumpMan.getInstance(null).setDano(50);
	    nivel = 0;
	    niveis = loadRooms();
	    currentRoom = niveis.get(nivel);
	    currentRoom.drawRoom();
	    JumpMan.getInstance(null).setPosition(currentRoom.getStartPositionForJumpMan());
	    ImageGUI.getInstance().setStatusMessage("Morreste! O jogo foi reiniciado.");
	}
	
	public void endgame() {
		JOptionPane.showMessageDialog(null, "Salvaste a princesa! Fim do jogo");
		String name = JOptionPane.showInputDialog(null, "Escreve o teu nome para registar o teu score:");
	    if (name != null && !name.trim().isEmpty())
	        saveScore(name.trim(), lastTickProcessed);
	    else
	        saveScore("Jogador Desconhecido", lastTickProcessed);
        displayHighScores();
        ImageGUI.getInstance().removeImages(currentRoom.getElements());
        ImageGUI.getInstance().dispose();
        System.exit(0);
	}
	
	public void saveScore(String name, int time) {
    	
    	scores.add(new HighScore(name, time));
    	scores.sort((p1, p2) -> p1.getTime() - p2.getTime());
        try {
            PrintWriter pw = new PrintWriter(leaderboard);
        	int rank = 1;
        	for (HighScore entry : scores) {
        		if (rank > 10) break;
        		pw.println(rank + ". " + entry.toString());
        		rank++;
        	}
        	pw.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar highscores");
        }
        
    }

    public void displayHighScores() {
    	String scores = "=== Highscores ===\n";
        try {
			Scanner sc = new Scanner(leaderboard);
			while (sc.hasNextLine())
				scores += sc.nextLine() + "\n";          
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro encontrar ficheiro");
		}
        JOptionPane.showMessageDialog(null, scores, "High Scores", JOptionPane.INFORMATION_MESSAGE);        
    }
    
    private void loadScores() {
        if (!leaderboard.exists()) {
            System.out.println("Nenhum ficheiro de highscores encontrado");
            return;
        }
        try (Scanner sc = new Scanner(leaderboard)) {
            while (sc.hasNextLine()) {
            	String line = sc.nextLine();
                String[] parts = line.split("\\. ", 2);
                if (parts.length == 2) {
                	String[] scoreParts = parts[1].split(" - ");
                    if (scoreParts.length == 2) {
                        String name = scoreParts[0].trim();
                        int time = Integer.parseInt(scoreParts[1].replace(" segundos", "").trim());
                        scores.add(new HighScore(name, time));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar highscores");
        }
    }
	
	private class HighScore {
    	
        private String name;
        private int time;

        public HighScore(String name, int time) {
            this.name = name;
            this.time = time;
        }

        public int getTime() {
            return time;
        }
        
        @Override
        public String toString() {
        	return name + " - " + time + " segundos";
        }
        
    }

}