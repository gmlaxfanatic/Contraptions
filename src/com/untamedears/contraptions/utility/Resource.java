package com.untamedears.contraptions.utility;

public class Resource {
    
    int amount;
    
    public Resource(int amount) {
        this.amount = amount;
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
