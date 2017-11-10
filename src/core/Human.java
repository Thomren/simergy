package core;

/**
 * The Human abstract class add a surname property to Entity so as to represents Humans
 * @author Thomas
 *
 */
public abstract class Human extends Entity {
	protected String surname;

	public Human() {
		super();
	}

	public Human(String name, String surname) {
		super(name);
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}
