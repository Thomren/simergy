package core;

import java.util.Random;

import resource.Patient;

/**
 * Abstract human factory that gives useful functions to generate human names and surnames.
 * @author Thomas
 *
 */
public abstract class HumanFactory {
	private String[] names;
	private String[] surnames;
	private Random rand;
	
	public HumanFactory(String[] names, String[] surnames) {
		super();
		this.names = names;
		this.surnames = surnames;
		this.rand = new Random();
	}
	
	public HumanFactory() {
		super();
		this.names = new String[]{"James","David","Christopher","George","John","Richard","Daniel","Kenneth",
		              "Anthony","Robert","Charles","Paul","Steven","Kevin","Michael","Joseph","Mark",
		              "Edward","Jason","William","Thomas","Donald","Brian","Jeff","Mary","Jennifer",
		              "Lisa","Sandra","Michelle","Patricia","Maria","Nancy","Donna","Laura",
		              "Linda","Susan","Karen","Carol","Sarah","Barbara","Margaret","Betty","Ruth",
		              "Kimberly","Elizabeth","Dorothy", "Helen","Sharon","Deborah"};
		this.surnames = new String[]{"Smith","Johnson","Williams","Jones","Brown","Davis","Miller","Wilson",
				"Moore","Taylor","Anderson","Thomas","Jackson","White","Harris","Martin","Thompson",
				"Garcia","Martinez","Robinson","Clark","Rodriguez","Lewis","Lee","Walker"};
		this.rand = new Random();
	}
	
	protected String getRandomName() {
		return names[rand.nextInt(names.length)];
	}
	
	protected String getRandomSurname() {
		return surnames[rand.nextInt(surnames.length)];
	}
	
	protected HealthInsurance getRandomInsurance() {
		double randomValue = rand.nextDouble();
		if(randomValue < 0.2) {
			return new GoldInsurance();
		}
		else if (randomValue < 0.7) {
			return new SilverInsurance();
		}
		else {
			return new NoInsurance();
		}
	}
}
