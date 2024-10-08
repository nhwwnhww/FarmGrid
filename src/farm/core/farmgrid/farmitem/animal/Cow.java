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
        this.symbol = '४';
        this.isFed = false;
        this.hasProduced = false;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public Product harvest() throws UnableToInteractException {
        if (!isFed) {
            throw new UnableToInteractException("You have not fed this animal today!");
        }
        if (hasProduced) {
            throw new UnableToInteractException("This animal has produced an item already today!");
        }
        hasProduced = true;
        Quality quality = new RandomQuality().getRandomQuality();
        return new Milk(quality);
    }
}

