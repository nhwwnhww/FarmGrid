package farm.core.farmgrid.farmitem;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;

/**
 * Interface representing a generic item in the farm.
 */
public interface FarmItem {

    /**
     * Sets the symbol representing the farm item.
     */
    void setSymbol(char symbol);

    /**
     * Returns the symbol representing the farm item.
     */
    char getSymbol();

    /**
     * Harvests the farm item, producing a product.
     */
    Product harvest() throws UnableToInteractException;

    /**
     * Interacts with the farm item based on a command.
     */
    void interact(String command) throws UnableToInteractException;

    /**
     * Performs necessary actions at the end of the day.
     */
    void endDay();

}
