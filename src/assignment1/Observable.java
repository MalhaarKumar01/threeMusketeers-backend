package assignment1;

public abstract class Observable {
	public String input;
	public abstract void update();
	
	public void setInput(String s) {
		this.input = s;
	}
	
	public String getInput() {
		return input;
	}
}
