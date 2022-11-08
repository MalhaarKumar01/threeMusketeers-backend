package assignment1;

public class VisitorImplement implements Visitor{
	Board board;
	public VisitorImplement(Board board) {
		this.board = board;
	}

	@Override
	public double visit(Guard guardType) {
		System.out.println("The probability you will win based on the current board state is:" + guardType.probabilityOfWinning(board));
		return guardType.probabilityOfWinning(board);
	}

	@Override
	public double visit(Musketeer musketeerType) {
		System.out.println("The probability you will win based on the current board state is:" + musketeerType.probabilityOfWinning(board));
		return musketeerType.probabilityOfWinning(board);
	}
}
