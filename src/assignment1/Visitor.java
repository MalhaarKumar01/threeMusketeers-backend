package assignment1;

public interface Visitor {
	public double visit(Guard guardType);
	public double visit(Musketeer musketeerType);
}
