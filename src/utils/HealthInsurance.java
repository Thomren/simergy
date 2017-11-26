package utils;

/**
 * This abstract class represents an health insurance which give a discount to its subscribers on all charges
 * @author Thomas
 *
 */
public abstract class HealthInsurance {
	protected String name;
	protected double discount;
	
	public HealthInsurance(String name, double discount) {
		super();
		this.name = name;
		this.discount = discount;
	}
	
	/**
	 * Compute the price payed by the patient after the discount of his health insurance
	 * @param charge is the amount charged to the patient by the hospital
	 * @return the discounted price effectively paid by the patient
	 */
	public double computeDiscountedPrice(double charge) {
		return Math.round(charge * (1 - discount) * 100.0)/100.0;
	}

	@Override
	public String toString() {
		return "HealthInsurance " + name;
	}
	
}
