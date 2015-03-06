package com.untamedears.contraptions.utility;

public class Resource {

    int amount;

    public Resource(int amount) {
        this.amount = amount;
    }

    public void set(int amount) {
        this.amount = amount;
    }

    public void change(int change) {
        this.amount += change;
    }
    /*
     Changes the resource with a floor to which it will not lower the resource below
     */

    public int safeChange(int change, int minimum, int maximum) {
        int oldAmount = amount;
        if(change >= 0) {
            if(amount>maximum) {
                return 0;
            }
            else {
                amount = amount + change <= maximum ? amount + change : maximum;
                return oldAmount-amount;
                
            }
        }
        else {
            if(amount<minimum) {
                return 0;
            }
            else {
                amount = amount + change >= minimum ? amount + change : maximum;
                return oldAmount-amount;
                
            }
        }
    }

    public int get() {
        return amount;
    }
}
