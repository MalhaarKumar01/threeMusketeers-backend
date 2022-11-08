package assignment1;

// ConcreteCommand Class
public class NewRound implements Round{
	
	private Board board;
	
	public NewRound(Board board) {
		this.board = board;
	}

	@Override
	public void execute() {
		board.newRound();
		
	}
	
	public void MakeNewRound(Board boardRound) {
		// Instantiates the current board round
		board = boardRound;
	}
}