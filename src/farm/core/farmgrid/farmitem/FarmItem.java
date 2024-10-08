package farm.core.farmgrid.farmitem;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.RandomQuality;

/**
 * Abstract class representing a generic item in the farm, which can be extended
 * by specific items such as plants or animals.
 */
public abstract class FarmItem {
    protected char symbol;
    protected RandomQuality randomQuality;

    /**
     * Constructor for FarmItem, initializing the random quality of the item.
     *
     * @param randomQuality - The RandomQuality object representing the quality of the farm item.
     */
    public FarmItem(RandomQuality randomQuality) {
        this.randomQuality = randomQuality;
    }

    /**
     * Abstract method to return the symbol representing the farm item.
     *
     * @return the character symbol representing the farm item.
     */
    public abstract char getSymbol();

    /**
     * Abstract method for harvesting the farm item, producing a Product.
     *
     * @return a Product based on the farm item's state and quality.
     * @throws UnableToInteractException if the item cannot be harvested at the current time.
     */
    public abstract Product harvest() throws UnableToInteractException;

    /**
     * Abstract method for interacting with the farm item via a command.
     *
     * @param command - A string representing the interaction command.
     * @throws UnableToInteractException if the command is not recognized or not supported.
     */
    public abstract void interact(String command) throws UnableToInteractException;

    /**
     * Abstract method called at the end of the day, allowing the farm item to perform
     * any necessary state updates or actions.
     */
    public abstract void endDay();
}
