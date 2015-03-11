package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import org.json.JSONObject;

/**
 * A Gadget which bound a resource to a minimum and maximum value Format of JSON
 * object should be as follows:
 * <pre>
 * {
 *   "resource _id": "RESOURCE_ID"
 *   "min": "MINIMUM"
 *   "max": "MAXIMUM"
 * }
 * </pre>
 */
public class MinMaxGadget {

    //The name of the which is being MinMaxed
    String resourceID;
    double min;
    double max;

    /**
     * Creates a MinMax Gadget
     *
     * @param resourceID String of the resource which is bounded
     * @param min        Minimum value for the resource
     * @param max        Maximum value for the resource
     */
    public MinMaxGadget(String resourceID, double min, double max) {
        this.resourceID = resourceID;
        this.min = min;
        this.max = max;
    }

    /**
     * Imports a MinMaxGadget from a JSONObject
     * 
     * @param jsonObject The JSONObject containing the information
     * @return A MinMaxGadget with the properties contained in the JSONObject
     */
    public static MinMaxGadget fromJSON(JSONObject jsonObject) {
        String resourceID = jsonObject.getString("resource_id");
        double min = jsonObject.getDouble("min");
        double max = jsonObject.getDouble("max");
        return new MinMaxGadget(resourceID, min, max);
    }

    /**
     * Update the resource associated with this gadget
     *
     * @param contraption Contraption to update
     * @return The amount the resource was corrected
     */
    public double update(Contraption contraption) {
        Resource resource = contraption.getResource(resourceID);
        double resoucreAmount = resource.get();
        if (resoucreAmount < min) {
            resource.setUnsafe(min);
            return min - resoucreAmount;
        } else if (resoucreAmount > max) {
            resource.setUnsafe(max);
            return resoucreAmount - max;
        } else {
            return 0;
        }
    }
}
