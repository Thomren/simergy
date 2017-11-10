package resource;

import core.Entity;

/**
 * Room is an abstract class to represent every room in the Emergency Department.
 * It has a capacity.
 * It inherits from Entity.
 * @author Quentin
 *
 */

public abstract class Room extends Entity {
	protected int capacity;
	
	public Room(String name, int capacity) {
		super(name);
		this.capacity = capacity;
		// TODO Auto-generated constructor stub
	}

}
