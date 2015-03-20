package vg.civcraft.mc.contraptions.utility;

import vg.civcraft.mc.contraptions.contraptions.Contraption;

/**
 * Represents a resource of a single Contraption
 */
public class Resource {

    double amount;
    Contraption contraption;

    /**
     * Creates the resource
     *
     * @param amount Starting amount of the Resource
     * @param contraption Associated contraption
     */
    public Resource(double amount, Contraption contraption) {
        this.amount = amount;
        this.contraption = contraption;
    }

    /**
     * Set the resource and notifies the Contraptions
     *
     * @param amount Amount to set the resource to
     */
    public void setAndUpdate(double amount) {
        this.amount = amount;
        contraption.update(this);
    }

    /**
     * Sets the resource without updating the associated Contraption
     *
     * Primarily used to prevent loops
     *
     * @param amount
     */
    public void set(double amount) {
        this.amount = amount;
    }

    /**
     * Changes the resource by change
     *
     * @param change Amount to change the resource by
     */
    public void change(double change) {
        set(amount + change);
    }
    
        /**
     * Changes the resource by change and updates the contraption
     *
     * @param change Amount to change the resource by
     */
    public void changeAndUpdate(double change) {
        setAndUpdate(amount + change);
    }

    /**
     * Changes the resource while gauranteeing it will stay in a given range
     *
     * @param change Amount to change the resource by
     * @param minimum Minimum value of the resource
     * @param maximum Maximum value of the resource
     * @return The amount the resource was unable to change
     */
    public double safeChange(double change, double minimum, double maximum) {
        double oldAmount = amount;
        if (change >= 0) {
            if (amount > maximum) {
                return 0;
            } else {
                if (amount + change <= maximum) {
                    setAndUpdate(amount + change);
                } else {
                    setAndUpdate(maximum);
                }
                return oldAmount - amount;

            }
        } else {
            if (amount < minimum) {
                return 0;
            } else {
                if (amount + change >= minimum) {
                    setAndUpdate(amount + change);
                } else {
                    setAndUpdate(maximum);
                }
                return oldAmount - amount;

            }
        }
    }

    /**
     * Gets the current value of the resource
     *
     * @return The current value of the resource
     */
    public double get() {
        return amount;
    }
}
