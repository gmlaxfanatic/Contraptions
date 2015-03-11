package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.utility.Resource;
import org.json.JSONObject;

/**
 * A Gadget which bounds a resource to a minimum and maximum value
 *
 * Format of JSON object should be as follows:
 * <pre>
 * {
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
     * @param min Minimum value for the resource
     * @param max Maximum value for the resource
     */
    public MinMaxGadget(double min, double max) {
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
        double min = jsonObject.has("min") ? jsonObject.getDouble("min") : -Double.MAX_VALUE;
        double max = jsonObject.has("max") ? jsonObject.getDouble("max") : Double.MAX_VALUE;
        return new MinMaxGadget(min, max);
    }

    /**
     * Update the resource associated with this gadget
     *
     * @param resource The resource to min/max
     * @return The amount the resource was corrected
     */
    public double update(Resource resource) {
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

    /**
     * Gets the minimum value for this MinMaxGadget
     *
     * @return The minimum value for this MinMaxGadget
     */
    public double getMin() {
        return min;
    }

    /**
     * Gets the maximum value for this MinMaxGadget
     *
     * @return The maximum value for this MinMaxGadget
     */
    public double getMax() {
        return max;
    }

}
