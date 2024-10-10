package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.farmitem.*;
import farm.core.farmgrid.farmitem.animal.Animal;
import farm.core.farmgrid.farmitem.animal.Chicken;
import farm.core.farmgrid.farmitem.animal.Cow;
import farm.core.farmgrid.farmitem.animal.Sheep;
import farm.core.farmgrid.farmitem.plant.BerryPlant;
import farm.core.farmgrid.farmitem.plant.CoffeePlant;
import farm.core.farmgrid.farmitem.plant.Plant;
import farm.core.farmgrid.farmitem.plant.WheatPlant;
import farm.inventory.product.Product;
import farm.inventory.product.data.RandomQuality;

import java.util.ArrayList;
import java.util.List;

/**
 * The FarmGrid class represents a grid-based farm system where various plants and animals can be placed,
 * interacted with, and harvested. The class supports operations like placing items, removing them,
 * and interacting with the farm through commands.
 */
public class FarmGrid implements Grid {

    private final Cell[][] grid;
    private final int rows;
    private final int columns;
    private final String farmType;
    private final RandomQuality randomQuality;

    /**
     * Constructor for FarmGrid with specified rows, columns, and farm type.
     *
     * @param rows     - The number of rows in the grid.
     * @param columns  - The number of columns in the grid.
     * @param farmType - The type of the farm (either "plant" or "animal").
     */
    public FarmGrid(int rows, int columns, String farmType) {
        this.rows = rows;
        this.columns = columns;
        this.farmType = farmType;
        this.randomQuality = new RandomQuality();
        this.grid = new Cell[rows][columns];
        initializeFarm();
    }

    /**
     * Overloaded constructor for FarmGrid, assuming a default farm type of "plant".
     *
     * @param rows    - The number of rows in the grid.
     * @param columns - The number of columns in the grid.
     */
    public FarmGrid(int rows, int columns) {
        this(rows, columns, "plant");
    }

    private void initializeFarm() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Retrieves the type of the farm (either plant or animal).
     *
     * @return A {@code String} representing the type of the farm.
     *         It will return "plant" for a plant farm or "animal" for an animal farm.
     */
    public String getFarmType() {
        return this.farmType;
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        if (!isValidPosition(row, column)) {
            return false;
        }
        Cell cell = grid[row][column];
        if (!cell.isEmpty()) {
            throw new IllegalStateException("Something is already there!");
        }
        FarmItem item = createItemBySymbol(symbol);
        if (item == null) {
            return false;
        }
        if (isItemTypeAllowed(item)) {
            cell.placeItem(item);
            return true;
        } else {
            throw new IllegalArgumentException("Something went wrong while placing");
        }
    }

    private boolean isValidPosition(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    private FarmItem createItemBySymbol(char symbol) {
        return switch (symbol) {
            case '.' -> new BerryPlant(randomQuality);
            case ':' -> new CoffeePlant(randomQuality);
            case 'ἴ' -> new WheatPlant(randomQuality);
            case '৬' -> new Chicken(randomQuality);
            case '४' -> new Cow(randomQuality);
            case 'ඔ' -> new Sheep(randomQuality);
            default -> null;
        };
    }

    private boolean isItemTypeAllowed(FarmItem item) {
        if ("animal".equals(farmType) && item instanceof Animal) {
            return true;
        }
        return "plant".equals(farmType) && item instanceof Plant;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    /**
     * Removes the FarmItem at the specified row and column from the grid.
     */
    public void remove(int row, int column) {
        if (!isValidPosition(row, column)) {
            return;
        }
        grid[row][column].removeItem();
    }

    @Override
    public String farmDisplay() {
        StringBuilder sb = new StringBuilder();
        String horizontalFence = "-".repeat(columns * 2 + 3);
        sb.append(horizontalFence).append(System.lineSeparator());
        for (int i = 0; i < rows; i++) {
            sb.append("| ");
            for (int j = 0; j < columns; j++) {
                sb.append(grid[i][j].getSymbol()).append(" ");
            }
            sb.append("|").append(System.lineSeparator());
        }
        sb.append(horizontalFence).append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public List<List<String>> getStats() {
        List<List<String>> stats = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = grid[i][j];
                List<String> cellInfo = new ArrayList<>();
                if (cell.isEmpty()) {
                    cellInfo.add("ground");
                    cellInfo.add(" ");
                } else {
                    FarmItem item = cell.getItem();
                    cellInfo.add(getItemName(item));
                    cellInfo.add(String.valueOf(item.getSymbol()));
                    if (item instanceof Plant) {
                        cellInfo.add("Stage: " + ((Plant) item).getGrowthStage());
                    } else if (item instanceof Animal) {
                        cellInfo.add("Fed: " + (((Animal) item).isFed()));
                        cellInfo.add("Collected: " + ((Animal) item).hasProduced());
                    }
                }
                stats.add(cellInfo);
            }
        }
        return stats;
    }

    private String getItemName(FarmItem item) {
        if (item instanceof BerryPlant) {
            return "berry";
        }
        if (item instanceof CoffeePlant) {
            return "coffee";
        }
        if (item instanceof WheatPlant) {
            return "wheat";
        }
        if (item instanceof Cow) {
            return "cow";
        }
        if (item instanceof Chicken) {
            return "chicken";
        }
        if (item instanceof Sheep) {
            return "sheep";
        }
        return "unknown";
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        if (!isValidPosition(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }
        Cell cell = grid[row][column];
        if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }
        FarmItem item = cell.getItem();
        // In the case of plants, the plant needs to be retained after harvest for continued growth
        return item.harvest();
    }

    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        if ("end-day".equalsIgnoreCase(command)) {
            endDay();
            return true;
        }
        if ("remove".equalsIgnoreCase(command)) {
            remove(row, column);
            return true;
        }
        if (!isValidPosition(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }
        Cell cell = grid[row][column];
        if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }
        cell.getItem().interact(command);
        return true;
    }

    /**
     * Advances the farm to the next day.
     */
    public void endDay() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = grid[i][j];
                if (!cell.isEmpty()) {
                    cell.getItem().endDay();
                }
            }
        }
    }

    /**
     * Sets the state of the farm based on the provided farm state data.
     */
    public void setFarmStateFromFile(List<List<String>> farmState) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<String> cellData = farmState.get(i * columns + j);
                Cell cell = new Cell();
                if (!cellData.get(0).equals("ground")) {
                    FarmItem item = createItemByName(cellData.get(0));
                    if (item != null) {
                        cell.placeItem(item);
                        if (item instanceof Plant) {
                            ((Plant) item).setGrowthStage(Integer.parseInt(cellData.get(2)
                                    .split(": ")[1]));
                        } else if (item instanceof Animal) {
                            ((Animal) item).setFed(Boolean
                                    .parseBoolean(cellData.get(2).split(": ")[1]));
                            ((Animal) item).setProduced(Boolean.parseBoolean(cellData.get(3)
                                    .split(": ")[1]));
                        }
                    }
                }
                grid[i][j] = cell;
            }
        }
    }

    private FarmItem createItemByName(String name) {
        return switch (name) {
            case "berry" -> new BerryPlant(randomQuality);
            case "coffee" -> new CoffeePlant(randomQuality);
            case "wheat" -> new WheatPlant(randomQuality);
            case "chicken" -> new Chicken(randomQuality);
            case "cow" -> new Cow(randomQuality);
            case "sheep" -> new Sheep(randomQuality);
            default -> null;
        };
    }
}