package model;

/**
 * A subclass of Part, adding the company name to the instance.
 *
 * @see Part
 */
public class OutSourced extends Part {
    private String companyName;

    /**
     * @param id          Automatically generated part ID.
     * @param name        The name of the part entered by the user.
     * @param price       The price of the part entered by the user.
     * @param stock       The current number of part entered by the user.
     * @param min         The minimum number of allowed parts entered by the user.
     * @param max         The maximum number of allowed parts entered by the user.
     * @param companyName The name of the part's supplier.
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super( id, name, price, stock, min, max );
        this.companyName = companyName;
    }

    /**
     * Get the company's name.
     *
     * @return String name of the company
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company's name.
     *
     * @param companyName String company name entered by user
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
