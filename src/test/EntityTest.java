package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.Entity;
import resources.Nurse;

/*
 * This is the JUnit test for EntityTest
 * @author Quentin
 */
public class EntityTest {
	EmergencyDepartment ED;
	Entity entity1;
	Entity entity2;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("ED");
		entity1 = new Nurse("nurse1", "surname1", ED);
		entity2 = new Nurse("nurse1", "surname1", ED);		
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
	   ED = null;
	   entity1 = null;
	   entity2 = null;
    }
	
	/**
	 * This checks if a given entity is equal to itself 
	 * and if it is different from another entity with the same attributes values
	 */
	@Test
	public void equalsTest() {
		assertTrue(entity1.equals(entity1));
		assertFalse(entity1.equals(entity2));
	}

}
