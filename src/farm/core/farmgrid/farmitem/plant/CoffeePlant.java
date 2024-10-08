package farm.core.farmgrid.farmitem.plant;

import farm.core.UnableToInteractException;
import farm.inventory.product.Coffee;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * Class representing a coffee plant, which extends the abstract Plant class.
 */
public class CoffeePlant extends Plant {

    /**
     * Constructor for CoffeePlant, initializing the plant with default symbol and growth stages.
     */
    public CoffeePlant(RandomQuality randomQuality) {
        super(randomQuality);
        this.symbol = ':';
        this.growthStage = 1;
        this.maxGrowthStage = 4;
    }

    @Override
    public char getSymbol() {
        return switch (growthStage) {
            case 1 -> ':';
            case 2 -> ';';
            case 3 -> '*';
            case 4 -> '%';
            default -> ':';
        };
    }

    @Override
    public void grow() {
        if (growthStage < maxGrowthStage) {
            growthStage++;
        }
    }

    @Override
    public Product harvest() throws UnableToInteractException {
        if (growthStage >= maxGrowthStage) {
            Quality quality = new RandomQuality().getRandomQuality();
            growthStage = 1;
            return new Coffee(quality);
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }
}
