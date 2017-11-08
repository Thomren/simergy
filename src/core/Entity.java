package core;

/**
 * Entity is an abstract class to represent all the different entities of the Emergency Department
 * It attributes them a unique ID and a name
 *
 */
public abstract class Entity {
	protected static int lastID;
	protected int ID;
	protected String name;
	
	Entity(String name) {
		this.ID = lastID + 1;
		this.name = name;
		lastID++; 
	}
	
	Entity() {
		this.ID = lastID + 1;
		lastID++; 
	}
}
