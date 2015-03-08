package com.untamedears.contraptions.utility;

import com.untamedears.contraptions.contraptions.Contraption;

public class Resource {

    double amount;
    Contraption contraption;

    public Resource(double amount, Contraption contraption) {
        this.amount = amount;
        this.contraption = contraption;
    }

    public void set(double amount) {
        this.amount = amount;
        contraption.update(this);
    }

    public void change(double change) {
        set(amount + change);
    }
    /*
     Changes the resource with a floor to which it will not lower the resource below
     */

    public double safeChange(double change, double minimum, double maximum) {
        double oldAmount = amount;
        if (change >= 0) {
            if (amount > maximum) {
                return 0;
            } else {
                if (amount + change <= maximum) {
                    set(amount + change);
                } else {
                    set(maximum);
                }
                return oldAmount - amount;

            }
        } else {
            if (amount < minimum) {
                return 0;
            } else {
                if (amount + change >= minimum) {
                    set(amount + change);
                } else {
                    set(maximum);
                }
                return oldAmount - amount;

            }
        }
    }

    public double get() {
        return amount;
    }
}
