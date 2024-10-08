package farm.core.farmgrid.farmitem.plant;

import farm.core.UnableToInteractException;
import farm.inventory.product.Bread;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * Class representing a wheat plant, which extends the abstract Plant class.
 */
public class WheatPlant extends Plant {

    /**
     * Constructor for WheatPlant, initializing the plant with default symbol and growth stages.
     */
    public WheatPlant(RandomQuality randomQuality) {
        super(randomQuality);
        this.symbol = 'ἴ';
        this.growthStage = 1;
        this.maxGrowthStage = 2;
    }

    @Override
    public char getSymbol() {
        return switch (growthStage) {
            case 1 -> 'ἴ';
            case 2 -> '#';
            default -> 'ἴ';
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
            return new Bread(quality);
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }
}