package farm.core.farmgrid.farmitem.animal;

import farm.core.UnableToInteractException;
import farm.inventory.product.Milk;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * Class representing a cow, which extends the abstract Animal class.
 */
public class Cow extends Animal {

    /**
     * Constructor for Cow, initializing the cow with the default symbol ('४')
     * and resetting the fed and produced states.
     */
    public Cow(RandomQuality randomQuality) {
        super(randomQuality);
        this.setFed(false);
        this.setProduced(false);
        this.setSymbol('४');
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
        return new Milk(quality);
    }
}

