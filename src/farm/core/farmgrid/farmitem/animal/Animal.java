package farm.core.farmgrid.farmitem.animal;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.farmitem.FarmItem;
import farm.inventory.product.data.RandomQuality;

/**
 * Abstract class representing an animal on the farm, which is inherited from FarmItem.
 */
public abstract class Animal extends FarmItem {
    private boolean isFed;
    private boolean hasProduced;

    /**
     * Constructor for FarmItem, initializing the random quality of the item.
     *
     * @param randomQuality - The RandomQuality object representing the quality of the farm item.
     */
    public Animal(RandomQuality randomQuality) {
        super(randomQuality);
    }

    /**
     * Feeds the animal by setting isFed to true.
     */
    public void feed() {
        isFed = true;
    }

    /**
     * Returns whether the animal has been fed.
     * @return isFed
     */
    public boolean isFed() {
        return isFed;
    }

    /**
     * Sets the fed status of the animal.
     */
    public void setFed(boolean isFed) {
        this.isFed = isFed;
    }

    /**
     * Returns whether the animal has produced a product today.
     * @return hasProduced
     */
    public boolean hasProduced() {
        return hasProduced;
    }

    /**
     * Sets the produced status of the animal.
     */
    public void setProduced(boolean hasProduced) {
        this.hasProduced = hasProduced;
    }

    @Override
    public void endDay() {
        isFed = false;
        hasProduced = false;
    }

    @Override
    public void interact(String command) throws UnableToInteractException {
        if ("feed".equalsIgnoreCase(command)) {
            feed();
        } else {
            throw new UnableToInteractException("Unknown command: " + command);
        }
    }
}