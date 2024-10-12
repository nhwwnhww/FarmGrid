package farm.core.farmgrid.farmitem.animal;

import farm.core.UnableToInteractException;
import farm.inventory.product.Wool;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * Class representing a sheep, which extends the abstract Animal class.
 */
public class Sheep extends Animal {

    /**
     * Constructor for Sheep, initializing the sheep with the default symbol ('ඔ')
     * and resetting the fed and produced states.
     */
    public Sheep(RandomQuality randomQuality) {
        super(randomQuality);
        this.setFed(false);
        this.setProduced(false);
        this.setSymbol('ඔ');
    }

    @Override
    public Product harvest() throws UnableToInteractException {
        if (!isFed()) {
            throw new UnableToInteractException("You have not fed this animal today!");
        }
        if (hasProduced()) {
            throw new UnableToInteractException("This animal has produced an item already today!");
        }
        this.setProduced(true);
        Quality quality = this.getRandomQuality().getRandomQuality();
        return new Wool(quality);
    }
}
