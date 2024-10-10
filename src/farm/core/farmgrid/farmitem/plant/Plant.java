package farm.core.farmgrid.farmitem.plant;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.farmitem.FarmItem;
import farm.inventory.product.data.RandomQuality;

/**
 * Abstract class representing a plant in the farm, which is inherited from FarmItem.
 */
public abstract class Plant extends FarmItem {
    private int growthStage;
    private int maxGrowthStage;

    /**
     * Constructor for FarmItem, initializing the random quality of the item.
     *
     * @param randomQuality - The RandomQuality object representing the quality of the farm item.
     */
    public Plant(RandomQuality randomQuality) {
        super(randomQuality);
    }

    /**
     * Returns the current growth stage of the plant.
     * @return growthStage
     */
    public int getGrowthStage() {
        return growthStage;
    }

    /**
     * Sets the current growth stage of the plant.
     */
    public void setGrowthStage(int growthStage) {
        this.growthStage = growthStage;
    }

    /**
     * Returns the maximum growth stage that the plant can reach.
     * @return maxGrowthStage
     */
    public int getMaxGrowthStage() {
        return maxGrowthStage;
    }

    /**
     * Sets the maximum growth stage for the plant.
     */
    public void setMaxGrowthStage(int maxGrowthStage) {
        this.maxGrowthStage = maxGrowthStage;
    }

    /**
     * Abstract method that represents the plant's growth.
     * The subclasses must provide the implementation of how the plant grows.
     */
    public void grow() {
        if (this.getGrowthStage() < this.getMaxGrowthStage()) {
            this.setGrowthStage(this.getGrowthStage() + 1);
        }
    }

    @Override
    public void endDay() {
        grow();
    }

    @Override
    public void interact(String command) throws UnableToInteractException {
        throw new UnableToInteractException("Unknown command: " + command);
    }

}
