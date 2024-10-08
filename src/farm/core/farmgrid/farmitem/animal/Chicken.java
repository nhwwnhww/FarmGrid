package farm.core.farmgrid.farmitem.animal;

import farm.core.UnableToInteractException;
import farm.inventory.product.Egg;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * Class representing a chicken, which extends the abstract Animal class.
 */
public class Chicken extends Animal {

    /**
     * Constructor for Chicken, initializing the chicken with the default symbol ('৬')
     * and resetting the fed and produced states.
     */
    public Chicken(RandomQuality randomQuality) {
        super(randomQuality);
        this.symbol = '৬';
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
        return new Egg(quality);
    }
}