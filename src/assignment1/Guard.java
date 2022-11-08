package assignment1;

import java.util.List;

public class Guard extends Piece implements PlayerType, Boardint{

    public Guard() {
        super("O", Type.GUARD);
    }

    /**
     * Returns true if the Guard can move onto the given cell.
     * @param cell Cell to check if the Guard can move onto
     * @return True, if Guard can move onto given cell, false otherwise
     */
    @Override
    public boolean canMoveOnto(Cell cell) {
        return !cell.hasPiece();
    }
    
    public double probabilityOfWinning(Board board) {
		double probability = this.probability(board);
    	return (probability * 100);
	}

    public double probability(Board board) {
    	List<Cell> get_musketeer_cells = board.getMusketeerCells();
    	int total_musketeer_moves = 0;
    	int total_moves_possible_if_no_guards_were_around = 0;
    	

    	for (int i = 0; i < get_musketeer_cells.size(); i++) {
    		total_musketeer_moves += board.getPossibleDestinations(get_musketeer_cells.get(i)).size();
    		int r = get_musketeer_cells.get(i).getCoordinate().row;
    		int c = get_musketeer_cells.get(i).getCoordinate().col;
	    	
    		if ((r == 0 || r == 4) && ((c == 0) || (c == 4))){
	    		total_moves_possible_if_no_guards_were_around += 2;
    		}

    		if ((r == 0) || (r == 4)) {
    			if (c == 1 || c == 2 || c == 3){
    				total_moves_possible_if_no_guards_were_around += 3;
    			}
    		}
    		
    		if ((r == 1) || (r == 1) || (r == 3)) {
    			if (c == 0 || c == 4){
    				total_moves_possible_if_no_guards_were_around += 3;
    			}
    		}

    		if ((r == 1) || (r == 2) || (r == 3)) {
    			if (c == 1 || c == 2 || c == 3){
    				total_moves_possible_if_no_guards_were_around += 4;
    			}
    		}
    	}
    	float division  = (float)total_musketeer_moves/total_moves_possible_if_no_guards_were_around;
    	return division;
    }

	@Override
	public double accept(Visitor visitor) {
		return visitor.visit(this);
	}

	
}
