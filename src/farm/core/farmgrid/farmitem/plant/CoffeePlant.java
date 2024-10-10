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

    private final RandomQuality randomQuality;

    /**
     * Constructor for CoffeePlant, initializing the plant with default symbol and growth stages.
     */
    public CoffeePlant(RandomQuality randomQuality) {
        super(randomQuality);
        setSymbol(':');
        this.setGrowthStage(1);
        this.setMaxGrowthStage(4);
        this.randomQuality = randomQuality;
    }

    @Override
    public char getSymbol() {
        return switch (this.getGrowthStage()) {
            case 1 -> ':';
            case 2 -> ';';
            case 3 -> '*';
            case 4 -> '%';
            default -> ':';
        };
    }

    @Override
    public Product harvest() throws UnableToInteractException {
        if (this.getGrowthStage() >= this.getMaxGrowthStage()) {
            Quality quality = randomQuality.getRandomQuality();
            this.setGrowthStage(0);
            return new Coffee(quality);
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }
}
