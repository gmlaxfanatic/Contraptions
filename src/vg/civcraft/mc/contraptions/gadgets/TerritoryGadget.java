package vg.civcraft.mc.contraptions.gadgets;

import java.util.Collection;
import vg.civcraft.mc.contraptions.utility.voronoi.GraphEdge;
import vg.civcraft.mc.contraptions.utility.voronoi.Voronoi;
import vg.civcraft.mc.contraptions.contraptions.Contraption;
import vg.civcraft.mc.contraptions.utility.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates the 2D territory controlled by a registered contraption
 *
 * Territory control of a specific block can be determined simply by asking
 * which registered Contraption is the closest. The territory graph generated is
 * called a Voroni diagram. The algorithm used to calculate territory controled
 * is called Fortune's Algorithm and operate in O(n*log(n)) where n=
 * #contraptions registered
 */
public class TerritoryGadget {

    private Map<Contraption, Double> territory;
    private Map<Contraption, Resource> resources;
    private static double radius = 15000;

    /**
     * Creates an empty Territory Gadget
     */
    public TerritoryGadget() {
        territory = new HashMap<Contraption, Double>();
        resources = new HashMap<Contraption, Resource>();
    }

    /**
     * Registers a contraption with the Gadget
     *
     * @param contraption Contraption to be registered
     * @param resource    Resource representing percent of land controlled
     */
    public void addContraptions(Contraption contraption, Resource resource) {
        territory.put(contraption, 0d);
        resources.put(contraption, resource);
        updateTerritories();
    }

    /**
     * Removes a Contraption from the gadget
     *
     * @param contraption The Contraption to remove
     */
    public void removeContraption(Contraption contraption) {
        territory.remove(contraption);
        resources.remove(contraption);
        updateTerritories();
    }

    /**
     * In 2D calculates the number of blocks the each Contraption controls
     */
    private void updateTerritories() {
        Collection<Contraption> contraptions = resources.keySet();
        if (contraptions.isEmpty()) {
            return;
        }
        //Creates a new Voronoi object with a minimum spacing of 1
        Voronoi v = new Voronoi(1);
        //Gets Contraptions in a nice format and sets territory control to 0
        Contraption[] sitenbrs = new Contraption[contraptions.size()];
        int i = 0;
        double[] latitudes = new double[contraptions.size()];
        double[] longitudes = new double[contraptions.size()];
        for (Contraption contraption : contraptions) {
            sitenbrs[i] = contraption;
            latitudes[i] = contraption.getLocation().x;
            longitudes[i] = contraption.getLocation().z;
            //reset resource to 0
            resources.get(contraption).set(0d);
        }
        //Calculates edges of Voronoi diagram
        List<GraphEdge> graphEdges = v.generateVoronoi(latitudes, longitudes, -radius, radius, -radius, radius);
        //Adds up area of each triangle in the Voronoi diagram
        for (GraphEdge graphEdge : graphEdges) {
            updateArea(sitenbrs[graphEdge.site1], graphEdge);
            updateArea(sitenbrs[graphEdge.site2], graphEdge);
        }
    }

    /**
     * Updates the area controlled by a contraption
     *
     * @param contraption Contraption to update
     * @param graphEdge   Edge to use to calculate area
     */
    private void updateArea(Contraption contraption, GraphEdge graphEdge) {
        double triangularArea = areaTriangle(contraption.getLocation().x,
                contraption.getLocation().z, graphEdge.x1, graphEdge.y1, graphEdge.x1, graphEdge.y2);
        resources.get(contraption).change(triangularArea);
    }

    /**
     * calculates the area of a triangle
     *
     * @return Area of the triangle given the coordinates
     */
    private double areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double area = (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0;
        return area > 0 ? area : -area;
    }
}
