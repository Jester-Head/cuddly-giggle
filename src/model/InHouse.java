package model;

/**
 * A subclass of Part, adding the machine ID to the instance.
 *
 * @see Part
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * @param id        Automatically generated part ID.
     * @param name      The name of the part entered by the user.
     * @param price     The price of the part entered by the user.
     * @param stock     The current number of part entered by the user.
     * @param min       The minimum number of allowed parts entered by the user.
     * @param max       The maximum number of allowed parts entered by the user.
     * @param machineId The machine ID of the part entered by the user.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super( id, name, price, stock, min, max );
        this.machineId = machineId;
    }

    /**
     * Gets the machine ID.
     *
     * @return machineId int the part's machine ID.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets the machine ID of the part.
     *
     * @param machineId machine ID entered by user.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;

    }
}