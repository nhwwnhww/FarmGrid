package farm.core.farmgrid.farmitem.plant;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.farmitem.FarmItem;
import farm.inventory.product.data.RandomQuality;

/**
 * Abstract class representing a plant in the farm, which is inherited from FarmItem.
 */
public abstract class Plant extends FarmItem {
    public int growthStage;
    public int maxGrowthStage;

    /**
     * Constructor for FarmItem, initializing the random quality of the item.
     *
     * @param randomQuality - The RandomQuality object representing the quality of the farm item.
     */
    public Plant(RandomQuality randomQuality) {
        super(randomQuality);
    }

    /**
     * Abstract method that represents the plant's growth.
     * The subclasses must provide the implementation of how the plant grows.
     */
    public abstract void grow();

    @Override
    public void endDay() {
        grow();
    }

    @Override
    public void interact(String command) throws UnableToInteractException {
        throw new UnableToInteractException("Unknown command: " + command);
    }
}
