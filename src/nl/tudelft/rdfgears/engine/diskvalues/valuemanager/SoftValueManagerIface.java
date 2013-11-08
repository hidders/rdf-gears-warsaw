package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

public interface SoftValueManagerIface {

	public abstract void remove(long id);

	public abstract boolean alive();

}