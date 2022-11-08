package assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignment1.Piece.Type;

public class ThreeMusketeers extends Observable  implements multipleUndoHandler{

    private final Board board;
    private Agent musketeerAgent, guardAgent;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Move> moves = new ArrayList<>();
    public static List<Board> savedBoards = new ArrayList<Board>();

    // All possible game modes
    public enum GameMode {
        Human("Human vs Human"),
        HumanRandom("Human vs Computer (Random)"),
        HumanGreedy("Human vs Computer (Greedy)");

        private final String gameMode;
        GameMode(final String gameMode) {
            this.gameMode = gameMode;
        }
    }

    /**
     * Default constructor to load Starter board
     */
    public ThreeMusketeers() {
        this.board = new Board();
    }

    /**
     * Constructor to load custom board
     * @param boardFilePath filepath of custom board
     */
    public ThreeMusketeers(String boardFilePath) {
        this.board = new Board(boardFilePath);
    }

    /**
     * Play game with human input mode selector
     */
    public void play(){
        System.out.println("Welcome! \n");
        final GameMode mode = getModeInput();
        System.out.println("Playing " + mode.gameMode);
        play(mode);
    }

    /**
     * Play game without human input mode selector
     * @param mode the GameMode to run
     */
    public void play(GameMode mode){
        selectMode(mode);
        runGame();
    }
    
    public void play_clone(GameMode mode){
        selectMode_clone(mode);
        runGame();
    }
    
    /**
     * Mode selector sets the correct agents based on the given GameMode
     * @param mode the selected GameMode
     */
    private void selectMode(GameMode mode) {
        switch (mode) {
            case Human -> {
                musketeerAgent = new HumanAgent(board);
                guardAgent = new HumanAgent(board);
            }
            case HumanRandom -> {
                String side = getSideInput();
                
                // The following statement may look weird, but it's what is known as a ternary statement.
                // Essentially, it sets musketeerAgent equal to a new HumanAgent if the value M is entered,
                // Otherwise, it sets musketeerAgent equal to a new RandomAgent
                musketeerAgent = side.equals("M") ? new HumanAgent(board) : new RandomAgent(board);
                
                guardAgent = side.equals("G") ? new HumanAgent(board) : new RandomAgent(board);
            }
            case HumanGreedy -> {
                String side = getSideInput();
                musketeerAgent = side.equals("M") ? new HumanAgent(board) : new GreedyAgent(board);
                guardAgent = side.equals("G") ? new HumanAgent(board) : new GreedyAgent(board);
            }
        }
    }
    
    private void selectMode_clone(GameMode mode) {
        switch (mode) {
            case Human -> {
                musketeerAgent = HumanAgent.clone(board);
                guardAgent = HumanAgent.clone(board);
            }
            case HumanRandom -> {
                String side = getSideInput();
                musketeerAgent = side.equals("M") ? HumanAgent.clone(board) : RandomAgent.clone(board);                
                guardAgent = side.equals("G") ? HumanAgent.clone(board) : RandomAgent.clone(board);
            }
            case HumanGreedy -> {
                String side = getSideInput();
                musketeerAgent = side.equals("M") ? HumanAgent.clone(board) : GreedyAgent.clone(board);
                guardAgent = side.equals("G") ? HumanAgent.clone(board) : GreedyAgent.clone(board);
            }
        }
    }

    /**
     * Runs the game, handling human input for move actions
     * Handles moves for different agents based on current turn 
     */
    private void runGame() {
        while(!board.isGameOver()) {
            System.out.println("\n" + board);

            final Agent currentAgent;
            if (board.getTurn() == Piece.Type.MUSKETEER)
                currentAgent = musketeerAgent;
            else
                currentAgent = guardAgent;

            if (currentAgent instanceof HumanAgent) // Human move
                switch (getInputOption()) {
                    case "M":
                        move(currentAgent);
                        break;
                    case "N":
                    	if (board.isGameOver()) {
                    		board.newRound();
                    	}
                    	System.out.println("Game is not over yet, cannot start new round");
                    	break;
                    case "U":
                        if (moves.size() == 0) {
                            System.out.println("No moves to undo.");
                            continue;
                        }
                        else if (moves.size() == 1 || isHumansPlaying()) {
                            undoMove();
                        }
                        else {
                            undoMove();
                            undoMove();
                        }
                        break;
                    case "S":
                        board.saveBoard();
                        break;
                    case "X":
                    	int num  = 0;
                    	num += handleMultipleUndoRequest();
                    	for (int i = 0; i < num; i++) {
	                    	if (moves.size() == 0) {
	                            System.out.println("No moves to undo.");
	                            continue;
	                        }
	                        else if (moves.size() == 1 || isHumansPlaying()) {
	                            undoMove();
	                        }
	                        else {
	                            undoMove();
	                            undoMove();
	                        }
                        }
                    	break;	
                    case "R":
                    	update();
                    	restartGame();
                    case "C":
                    	switchGameMode();                   
                    case "P":
                   	  this.getProbabilityOfWinning();
                    	break;
                    case "L":
                    	getProxyBoardsMoves();
                    	break;
                }
            else { // Computer move
                System.out.printf("[%s] Calculating move...\n", currentAgent.getClass().getSimpleName());
                move(currentAgent);
            }
        }

        System.out.println(board);
        System.out.printf("\n%s won!%n", board.getWinner().getType());
    }

    private void getProxyBoardsMoves() {
		List<Board> lst = getProxyList(board.getCopyBoard(this.board)); // no of poss moves
		for (int x = 0; x < lst.size(); x++) {
			lst.get(x).move(lst.get(x).getPossibleMoves().get(x));
			System.out.println("The board below shows move " + (x+1));
			System.out.println(lst.get(x));
		}
    }

	private List<Board> getProxyList(Board x) {
		List<Board> lstBoards = new ArrayList<Board>();
		for (int numMoves = 0; numMoves < this.board.getPossibleMoves().size(); numMoves++) {
			ProxyBoard p = new ProxyBoard(x);

			lstBoards.add(p.board);
		}
		return lstBoards;
	}

	/**
     * Gets a move from the given agent, adds a copy of the move using the copy constructor to the moves stack, and
     * does the move on the board.
     * @param agent Agent to get the move from.
     */
    protected void move(final Agent agent) {
        final Move move = agent.getMove();
        moves.add(new Move(move));
        board.move(move);
    }

    /**
     * Removes a move from the top of the moves stack and undoes the move on the board.
     */
    private void undoMove() {
        board.undoMove(moves.remove(moves.size() - 1));
        System.out.println("Undid the previous move.");
    }

    private double getProbabilityOfWinning() {
    	if (this.board.getTurn() == Type.GUARD){
    		VisitorImplement v = new VisitorImplement(this.board);
    		Guard new_guard = new Guard();
    		new_guard.accept(v);
    	}
    	if (this.board.getTurn() == Type.MUSKETEER){
    		VisitorImplement v = new VisitorImplement(this.board);
    		Musketeer new_musketeer = new Musketeer();
    		new_musketeer.accept(v);
    	}
    	return 0.0;
    }

    /**
     * Get human input for move action
     * @return the selected move action, 'M': move, 'U': undo, and 'S': save
     */
    private String getInputOption() {
        System.out.printf("[%s] Enter 'M' to move, 'L' to enter learner mode, 'R' to restart the game, 'X' to go back multiple steps, 'N' to start new round, 'U' to undo, 'C' to change sides, 'P' to see probability of winning, or 'S' to save: ", board.getTurn().getType());
        while (!scanner.hasNext("[MNRUSPCXLmnruspcxl]")) {
            System.out.print("Invalid option. Enter 'M', 'L', 'R', 'U', 'C', 'X', 'P', 'N' or 'S': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }

	/**
     * Returns whether both sides are human players
     * @return True if both sides are Human, False if one of the sides is a computer
     */
    private boolean isHumansPlaying() {
        return musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent;
    }

    /**
     * Get human input for side selection
     * @return the selected Human side for Human vs Computer games,  'M': Musketeer, G': Guard
     */
    private String getSideInput() {
        System.out.print("Enter 'M' to be a Musketeer or 'G' to be a Guard: ");
        while (!scanner.hasNext("[MGmg]")) {
            System.out.println("Invalid option. Enter 'M' or 'G': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }

    /**
     * Get human input for selecting the game mode
     * @return the chosen GameMode
     */
    private GameMode getModeInput() {
        System.out.println("""
                    0: Human vs Human
                    1: Human vs Computer (Random)
                    2: Human vs Computer (Greedy)""");
        System.out.print("Choose a game mode to play i.e. enter a number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid option. Enter 0, 1, or 2: ");
            scanner.next();
        }
        final int mode = scanner.nextInt();
        if (mode < 0 || mode > 2) {
            System.out.println("Invalid option.");
            return getModeInput();
        }
        return GameMode.values()[mode];
    }
    
    private void switchGameMode() {
    	GameMode mode = getModeInput();
    	System.out.println("Now playing " + mode.gameMode);
        play_clone(mode);
    }
    
  // New method for restarting game 
	protected static void restartGame() {
		// Instantiation a new ThreeMusketeers object
		ThreeMusketeers new_game = new ThreeMusketeers();
		// Calling the method on the object that begins the game
		new_game.play();
	}

    public static void main(String[] args) {
        String boardFileName = "Boards/Starter.txt";
        ThreeMusketeers game = new ThreeMusketeers(boardFileName);
        game.play();
    }

	public static void winningRounds() {
		// Prints to the console how many rounds the Musketeer player has won, and how many rounds the Guard player has won.
		int counterM = 0;
		int counterG = 0;
		for (Board b: savedBoards) {
			if (b.getWinner() == Piece.Type.MUSKETEER) {
				counterM += 1;
			}
			if (b.getWinner() == Piece.Type.GUARD) {
				counterG += 1;
			}
		}
		System.out.println("Musketeer has won: " + counterM + " rounds.");
		System.out.println("Guard has won: " + counterG + " rounds.");
	}

	@Override
	public int handleMultipleUndoRequest() {
		// TODO Auto-generated method stub
	    System.out.println("Enter number of steps to undo : ");
	    return scanner.nextInt();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		System.out.println("Restarting game...");
	}
	
}
