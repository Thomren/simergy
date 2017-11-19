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
	protected EmergencyDepartment emergencyDepartment;
	
	public Entity(String name, EmergencyDepartment emergencyDepartment) {
		this.ID = lastID + 1;
		this.name = name;
		this.emergencyDepartment = emergencyDepartment;
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

	public EmergencyDepartment getEmergencyDepartment() {
		return emergencyDepartment;
	}

	public void setEmergencyDepartment(EmergencyDepartment emergencyDepartment) {
		this.emergencyDepartment = emergencyDepartment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Entity))
			return false;
		Entity other = (Entity) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
		
}
