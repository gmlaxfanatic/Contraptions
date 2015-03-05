package com.untamedears.contraption;

public class Resource {
    
    int amount;
    int maximum = Integer.MAX_VALUE;//Default of max int value
    
    public Resource(int amount) {
        this.amount = amount;
    }
    
    public Resource(int amount, int maximum) {
        this(amount);
        this.maximum = maximum;
    }
    
    public void setResource(int amount) { 
        this.amount = amount;
    }
    
    public void changeResource(int amount) { 
        this.amount +=amount;
    }
    
    public int getResource() {
        return amount;
    }
}
