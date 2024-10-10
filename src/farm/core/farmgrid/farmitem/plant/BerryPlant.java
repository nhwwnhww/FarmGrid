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

    private final RandomQuality randomQuality;

    /**
     * Constructor for BerryPlant, initializing the plant with default symbol and growth stages.
     */
    public BerryPlant(RandomQuality randomQuality) {
        super(randomQuality);
        setSymbol('.');
        this.setGrowthStage(1);
        this.setMaxGrowthStage(3);
        this.randomQuality = randomQuality;
    }

    @Override
    public char getSymbol() {
        return switch (this.getGrowthStage()) {
            case 1 -> '.';
            case 2 -> 'o';
            case 3 -> '@';
            default -> '.';
        };
    }

    @Override
    public Product harvest() throws UnableToInteractException {
        if (this.getGrowthStage() >= this.getMaxGrowthStage()) {
            Quality quality = randomQuality.getRandomQuality();
            this.setGrowthStage(0);
            return new Jam(quality);
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }
}
