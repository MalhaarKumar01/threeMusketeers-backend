package assignment1;

public class ProxyBoard extends Board implements LearnerBoard{
    public int size = 5;
    public Board board;
    protected Piece.Type turn;
    protected Piece.Type winner;
    protected int learnerMoveNum = 0;
	
    public ProxyBoard(Board b) {
    	this.board = b.getCopyBoard(b);
    	this.turn = b.turn;
    	this.winner = b.winner;
    }
	
	public void move(Move m) {
        Piece piece = m.fromCell.getPiece();
        m.toCell.setPiece(piece);
        m.fromCell.removePiece();
        changeTurn();
	}
}
