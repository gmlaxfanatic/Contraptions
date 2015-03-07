package com.untamedears.contraptions.utility;

import com.untamedears.contraptions.contraptions.Contraption;

public class Resource {

    int amount;
    Contraption contraption;

    public Resource(int amount, Contraption contraption) {
        this.amount = amount;
        this.contraption = contraption;
    }

    public void set(int amount) {
        this.amount = amount;
        contraption.update(this);
    }

    public void change(int change) {
        set(amount + change);
    }
    /*
     Changes the resource with a floor to which it will not lower the resource below
     */

    public int safeChange(int change, int minimum, int maximum) {
        int oldAmount = amount;
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

    public int get() {
        return amount;
    }
}
