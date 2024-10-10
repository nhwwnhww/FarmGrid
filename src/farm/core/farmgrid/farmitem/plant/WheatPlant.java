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

    private final RandomQuality randomQuality;

    /**
     * Constructor for WheatPlant, initializing the plant with default symbol and growth stages.
     */
    public WheatPlant(RandomQuality randomQuality) {
        super(randomQuality);
        setSymbol('ἴ');
        this.setGrowthStage(1);
        this.setMaxGrowthStage(2);
        this.randomQuality = randomQuality;
    }

    @Override
    public char getSymbol() {
        return switch (this.getGrowthStage()) {
            case 1 -> 'ἴ';
            case 2 -> '#';
            default -> 'ἴ';
        };
    }

    @Override
    public Product harvest() throws UnableToInteractException {
        if (this.getGrowthStage() >= this.getMaxGrowthStage()) {
            Quality quality = randomQuality.getRandomQuality();
            this.setGrowthStage(0);
            return new Bread(quality);
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }
}