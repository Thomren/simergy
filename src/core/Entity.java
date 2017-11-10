package core;

/**
 * Entity is an abstract class to represent all the different entities of the Emergency Department
 * It attributes them a unique ID and a name
 * @author Thomas
 *
 */
public abstract class Entity {
	protected static int lastID = 0;
	protected int ID;
	protected String name;
	
	public Entity(String name) {
		this.ID = lastID + 1;
		this.name = name;
		lastID++; 
	}
	
	public Entity() {
		this.ID = lastID + 1;
		lastID++; 
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
