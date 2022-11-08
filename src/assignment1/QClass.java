package assignment1;

import java.util.ArrayList;
import java.util.List;

// Invoker
public class QClass {
	private List<Object> m_roundsQueue = new ArrayList<Object>();
	
	public QClass() {
		
	}
	
	public void addRound(Round round) {
		// Adds the given round to the rounds queue and executes the needed moves.
		m_roundsQueue.add(round);
		round.execute();
	}
}
