package farm.core.farmgrid.farmitem.plant;

import farm.core.UnableToInteractException;
import farm.inventory.product.Jam;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * Class representing a berry plant, which extends the abstract Plant class.
 */
public class BerryPlant extends Plant {

    /**
     * Constructor for BerryPlant, initializing the plant with default symbol and growth stages.
     */
    public BerryPlant(RandomQuality randomQuality) {
        super(randomQuality);
        this.symbol = '.';
        this.growthStage = 1;
        this.maxGrowthStage = 3;
    }

    @Override
    public char getSymbol() {
        return switch (growthStage) {
            case 1 -> '.';
            case 2 -> 'o';
            case 3 -> '@';
            default -> '.';
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
            return new Jam(quality);
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }
}
