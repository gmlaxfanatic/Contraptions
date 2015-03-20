package vg.civcraft.mc.contraptions.utility;

import vg.civcraft.mc.contraptions.ContraptionsPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vg.civcraft.mc.contraptions.utility.org.json.JSONArray;
import vg.civcraft.mc.contraptions.utility.org.json.JSONObject;

public class InventoryHelpers {

    public static Map<ItemStack, String> prettyNames;

    public static void loadPrettyNames(File file) {
        prettyNames = new HashMap<ItemStack, String>();
        // Read Items
        try {
            BufferedReader CSVFile = new BufferedReader(new FileReader(file));
            String dataRow = CSVFile.readLine();
            while (dataRow != null) {
                String[] dataArray = dataRow.split(",");
                prettyNames.put(new ItemStack(Material.getMaterial(Integer.valueOf(dataArray[2]).intValue()), 1, Short.valueOf(dataArray[3])), dataArray[0]);
                dataRow = CSVFile.readLine();
            }
            CSVFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            ContraptionsPlugin.toConsole("Failed to load materials.csv");
        }
    }

    /**
     * Generates an ItemSet from a JSONArray
     *
     * The JSON object should have the following format [{ "material":
     * "MATERIAL_NAME", "amount": 1, "durability": 0, "name": "DISPLAY_NAME",
     * "lore": "LORE" },... }]
     *
     * @param jsonItemSet An array of ItemStacks represented as JSON objects
     * @return A set of parsed ItemStacks
     */
    public static Set<ItemStack> fromJSON(JSONArray jsonItemSet) {
        Set<ItemStack> items = new HashSet<ItemStack>();
        for (int i = 0; i < jsonItemSet.length(); i++) {
            JSONObject jsonItemStack = jsonItemSet.getJSONObject(i);
            String materialName = jsonItemStack.getString("material");
            Material material = Material.getMaterial(materialName);
            if (material == null) {
                throw new IllegalArgumentException("Illegal Material name");
            }
            int amount = jsonItemStack.has("amount") ? jsonItemStack.getInt("amount") : 1;
            short durability = jsonItemStack.has("durability") ? (short) jsonItemStack.getInt("durability") : (short) 0;
            String displayName = jsonItemStack.has("name") ? jsonItemStack.getString("name") : null;
            String lore = jsonItemStack.has("lore") ? jsonItemStack.getString("lore") : null;
            ItemStack itemStack = new ItemStack(material, amount, durability);
            if (displayName != null || lore != null) {
                ItemMeta meta = itemStack.getItemMeta();
                if (displayName != null) {
                    meta.setDisplayName(displayName);
                }
                if (lore != null) {
                    List<String> loreArray = new ArrayList<String>();
                    loreArray.add(lore);
                    meta.setLore(loreArray);
                }
                itemStack.setItemMeta(meta);
            }
            items.add(itemStack);
        }
        return items;
    }

    /**
     * Checks if all of the itemstacks are available in the inventory
     *
     * @param inventory The inventory to examen
     * @param itemStacks A set of ItemStacks that are requred to be in the
     * inventoyr
     * @return Checks if the ItemStacks are in the inventory
     */
    public static boolean allIn(Inventory inventory, Set<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (amountAvailable(inventory, itemStack) < itemStack.getAmount()) {
                return false;
            }
        }
        return true;
    }

    /*
     * Removes the ItemStack Set from the inventory
     */
    public static boolean remove(Inventory inventory, Set<ItemStack> itemStacks) {
        boolean returnValue = true;
        if (allIn(inventory, itemStacks)) {
            for (ItemStack itemStack : itemStacks) {
                returnValue = returnValue && remove(inventory, itemStack);
            }
        } else {
            returnValue = false;
        }
        return returnValue;
    }
    /*
     * Removes an itemstacks worth of material from an inventory
     */

    public static boolean remove(Inventory inventory, ItemStack itemStack) {
        int materialsToRemove = itemStack.getAmount();
        ListIterator<ItemStack> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            ItemStack currentItemStack = iterator.next();
            if (itemStack.isSimilar(currentItemStack)) {
                if (materialsToRemove <= 0) {
                    break;
                }
                if (currentItemStack.getAmount() == materialsToRemove) {
                    iterator.set(new ItemStack(Material.AIR, 0));
                    materialsToRemove = 0;
                } else if (currentItemStack.getAmount() > materialsToRemove) {
                    ItemStack temp = currentItemStack.clone();
                    temp.setAmount(currentItemStack.getAmount() - materialsToRemove);
                    iterator.set(temp);
                    materialsToRemove = 0;
                } else {
                    int inStack = currentItemStack.getAmount();
                    iterator.set(new ItemStack(Material.AIR, 0));
                    materialsToRemove -= inStack;
                }
            }
        }
        return materialsToRemove == 0;
    }

    /*
     * Removes the same set of ItemStacks from an inventory multiple times
     */
    public static boolean removeMultiple(Inventory inventory, Set<ItemStack> itemStacks, int multiple) {
        boolean sucess = true;
        for (int i = 0; i < multiple; i++) {
            sucess = sucess && remove(inventory, itemStacks);
        }
        return sucess;
    }

    /*
     * Checks how many multiples of the ItemSet at availible
     */
    public static int amountAvailable(Inventory inventory, Set<ItemStack> itemStacks) {
        int amountAvailable = 0;
        for (ItemStack itemStack : itemStacks) {
            int currentAmountAvailable = amountAvailable(inventory, itemStack);
            amountAvailable = amountAvailable > currentAmountAvailable ? amountAvailable : currentAmountAvailable;
        }
        return amountAvailable;
    }

    /*
     * Checks how many multiples of the provided ItemStack
     * are in the inventory
     */
    public static int amountAvailable(Inventory inventory, ItemStack itemStack) {
        int totalMaterial = 0;
        for (ItemStack currentItemStack : inventory) {
            if (currentItemStack != null) {
                /*
                 * For some reason I can't fathom the orientaion of the comparison
                 * of the two ItemStacks in the following statement matters.
                 */
                if (itemStack.isSimilar(currentItemStack)
                        || (itemStack.getType() == Material.NETHER_WARTS && currentItemStack.getType() == Material.NETHER_WARTS)) {
                    totalMaterial += currentItemStack.getAmount();
                }
            }
        }
        return totalMaterial;
    }

    /*
     * Checks to see if the inventory contains exactly
     * the items provided
     */
    public static boolean exactlyContained(Inventory inventory, Set<ItemStack> itemStacks) {
        boolean returnValue = true;
        //Checks that the ItemList ItemStacks are contained in the inventory
        for (ItemStack itemStack : itemStacks) {
            returnValue = returnValue && (amountAvailable(inventory, itemStack) == itemStack.getAmount());
        }
        //Checks that inventory has no ItemStacks in addition to the ones in the itemList
        for (ItemStack invItemStack : inventory.getContents()) {
            if (invItemStack != null) {
                boolean itemPresent = false;
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack.isSimilar(invItemStack)) {
                        itemPresent = true;
                    }
                }
                returnValue = returnValue && itemPresent;
            }
        }
        return returnValue;
    }

    public static void putIn(Inventory inventory, Set<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            int maxStackSize = itemStack.getMaxStackSize();
            int amount = itemStack.getAmount();
            while (amount > maxStackSize) {
                ItemStack itemClone = itemStack.clone();
                itemClone.setAmount(maxStackSize);
                inventory.addItem(itemClone);
                amount -= maxStackSize;
            }
            ItemStack itemClone = itemStack.clone();
            itemClone.setAmount(amount);
            inventory.addItem(itemClone);
        }
    }

    public static void putMultiple(Inventory inventory, Set<ItemStack> itemStacks, int multiple) {
        for (int i = 0; i < multiple; i++) {
            putIn(inventory, itemStacks);
        }
    }

    /**
     * Convert a ItemSet to a pretty string
     *
     * If the item has a custom DisplayName it uses that, then if it has an
     * entry in the pretty name lookup table use that, otherwise use bukkit name
     *
     * @param itemStacks ItemStakcs to convert to string
     * @return Pretty String representing ItemStacks
     */
    public static String toString(Set<ItemStack> itemStacks) {
        String output = "";
        for (ItemStack itemStack : itemStacks) {
            ItemStack key = itemStack.clone();
            key.setAmount(1);

            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                output += itemStack.getAmount() + " " + itemStack.getItemMeta().getDisplayName() + ",";
            } else {
                if (prettyNames.containsKey(key)) {
                    output += itemStack.getAmount() + " " + prettyNames.get(key) + ",";
                } else {
                    output += itemStack.getAmount() + " " + itemStack.getType().name() + ":" + itemStack.getDurability() + ",";
                }
            }
        }
        //Remove trailing ","
        if (output.length() != 0) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }
}
