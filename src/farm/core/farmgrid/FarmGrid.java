package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

import java.util.ArrayList;
import java.util.List;


/**
 * The FarmGrid class represents a grid-based simulation of a farm, either
 * plant-based or animal-based, where the user can plant crops or place animals
 * on the grid. The class supports interaction with farm elements like feeding
 * animals, harvesting crops, and managing farm states on a daily basis.
 *
 * <p>FarmGrid implements the {@code Grid} interface and provides the functionality
 * to manage the farm's size, contents, and interactions with crops and animals.</p>
 *
 * <p>This class uses a {@code RandomQuality} object to determine the quality of
 * products harvested from the farm.</p>
 */
public class FarmGrid implements Grid {

    private List<List<String>> farmState;
    private final int rows;
    private final int columns;
    private final String farmType;
    // randomQuality is used to help you generate quality for products
    private final RandomQuality randomQuality;

    /**
     * Constructor for the FarmGrid, creating a farm of specified type.
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns, String farmType) {
        this.rows = rows;
        this.columns = columns;
        this.farmType = farmType;
        this.randomQuality = new RandomQuality();
        this.farmState = new ArrayList<>();
        initializeFarm();
    }

    // Private method to initialize the farm with empty ground
    private void initializeFarm() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                List<String> spotOnGrid = new ArrayList<>();
                spotOnGrid.add("ground");
                spotOnGrid.add(" ");
                farmState.add(spotOnGrid);
            }
        }
    }

    /**
     * Sets the state of the farm grid.
     *
     * @param farmState the 2D list representing the state of each cell in the farm grid.
     */
    public void setFarmState(List<List<String>> farmState) {
        this.farmState = farmState;
    }

    /**
     * Default constructor for the FarmGrid, creating a plant farm.
     * NOTE: whatever class you implement that extends Grid *must* have a constructor
     * with this signature for testing purposes.
     *
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns) {
        this(rows, columns, "plant");
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        int positionIndex = (row * this.columns) + (column);

        // Correct the Row and Column Bounds Check
        if (row < 0 || column < 0 || row >= this.rows || column >= this.columns) {
            return false;
        }


        // get the name of the item based on the character
        String itemName;
        if (symbol == '.') {
            itemName = "berry";
        } else if (symbol == ':') {
            itemName = "coffee";
        } else if (symbol == 'ἴ') {
            itemName = "wheat";
        } else if (symbol == '৬') {
            itemName = "chicken";
        } else if (symbol == '४') {
            itemName = "cow";
        } else if (symbol == 'ඔ') {
            itemName = "sheep";
        } else {
            return false;
        }


        String symbolString = Character.toString(symbol);

        try {

            if (!this.farmState.get(positionIndex).getFirst().equals("ground")) {
                throw new IllegalStateException("Something is already there!");
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }


        // if the item to place is an animal
        if (this.farmType.equals("animal")) {
            if (itemName.equals("chicken") || itemName.equals("cow") || itemName.equals("sheep")) {
                List<String> newPositionInfo = List.of(
                        itemName, symbolString, "Fed: false", "Collected: false");
                this.farmState.set(positionIndex, newPositionInfo);
                return true;
            } else {
                throw new IllegalArgumentException("You cannot place that on a animal farm!");
            }
        } else if (this.farmType.equals("plant")) {
            if (itemName.equals("berry") || itemName.equals("coffee") || itemName.equals("wheat")) {
                List<String> newPositionInfo = List.of(itemName, symbolString, "Stage: 1");
                this.farmState.set(positionIndex, newPositionInfo);
                return true;
            } else {
                throw new IllegalArgumentException("You cannot place that on a plant farm!");
            }
        }
        throw new IllegalArgumentException("Something went wrong while placing");
    }

    @Override
    public int getRows() {

        return this.rows;

    }

    @Override
    public int getColumns() {
        // not this.columns - 1
        return this.columns;
    }

    /**
     * Removes the item (plant or animal) at the specified position in the farm grid.
     * The position is reset to "ground".
     *
     * @param row the row index of the item to be removed.
     * @param column the column index of the item to be removed.
     */
    public void remove(int row, int column) {
        int positionIndex = (row * this.columns) + column;
        if (positionIndex > farmState.size()) {
            return;
        }
        if (row > this.rows - 1) {
            return;
        }
        if (column > this.columns - 1) {
            return;
        }

        // replace the spot with empty ground
        List<String> spotOnGrid = new ArrayList<>();
        spotOnGrid.add("ground");
        spotOnGrid.add(" ");
        farmState.set(positionIndex, spotOnGrid);
    }

    @Override
    public String farmDisplay() {
        // create the fence at the top of the farm
        // two lines for each column of the farm, plus two for edges
        // and one for extra space
        String horizontalFence = "-".repeat((this.columns * 2) + 3);

        StringBuilder farmDisplay = new StringBuilder(horizontalFence + System.lineSeparator());

        // start each line with a "|" fence character
        // then display symbols with a space either side

        // note System.lineSeparator() is just \n but ensures it works
        // on all operating systems.

        for (int i = 0; i < this.rows; i++) {
            farmDisplay.append("| ");
            for (int j = 0; j < this.columns; j++) {
                farmDisplay.append(farmState.get(j).get(1)).append(" ");
            }
            farmDisplay.append("|").append(System.lineSeparator());
        }
        farmDisplay.append(horizontalFence).append(System.lineSeparator());
        return farmDisplay.toString();
    }

    @Override
    public List<List<String>> getStats() {
        return getTheFarmStatsList();
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        int positionIndex = row * this.columns + column;

        // check
        if (row < 0 || column < 0 || row >= this.rows || column >= this.columns) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        List<String> positionInfo;
        try {
            positionInfo = farmState.get(positionIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        // throw an exception if you try to harvest empty ground
        if (positionInfo.get(0).equals("ground")) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }


        // if the position contains an animal
        if ((positionInfo.get(0).equals("cow")
                || positionInfo.get(0).equals("chicken")
                || positionInfo.get(0).equals("sheep"))
                && this.farmType.equals("animal")) {
            // check fed and collected status of animal
            if (positionInfo.get(2).equals("Fed: false")) {
                throw new UnableToInteractException("You have not fed this animal today!");
            } else if (positionInfo.get(3).equals("Collected: true")) {
                //animals can only produce once per day
                throw new UnableToInteractException(
                        "This animal has produced an item already today!");
            } else if (positionInfo.get(2).equals("Fed: true")
                    && positionInfo.get(3).equals("Collected: false")) {
                // get a random quality

                Quality quality = randomQuality.getRandomQuality();

                //return product and set collected to true
                switch (positionInfo.get(0)) {
                    case "cow" -> {
                        positionInfo = List.of(
                                positionInfo.get(0),
                                positionInfo.get(1),
                                positionInfo.get(2),
                                "Collected: true");
                        farmState.set(positionIndex, positionInfo);
                        return new Milk(quality);
                    }
                    case "chicken" -> {
                        positionInfo = List.of(
                                positionInfo.get(0),
                                positionInfo.get(1),
                                positionInfo.get(2),
                                "Collected: true");
                        farmState.set(positionIndex, positionInfo);
                        return new Egg(quality);
                    }
                    case "sheep" -> {
                        positionInfo = List.of(
                                positionInfo.get(0),
                                positionInfo.get(1),
                                positionInfo.get(2),
                                "Collected: true");
                        farmState.set(positionIndex, positionInfo);
                        return new Wool(quality);
                    }
                }
            } else {
                throw new UnableToInteractException("You have an animal on a plant farm!");
            }
        } else if ((positionInfo.get(0).equals("wheat")
                || positionInfo.get(0).equals("berry")
                || positionInfo.get(0).equals("coffee"))
                && this.farmType.equals("plant")) {
            // get a random quality
            Quality quality = randomQuality.getRandomQuality();


            // check stage
            switch (positionInfo.get(0)) {
                case "wheat" -> {
                    if (positionInfo.get(1).equals("#")) {
                        positionInfo = List.of(positionInfo.getFirst(), "ἴ", "Stage: 0");
                        farmState.set(positionIndex, positionInfo);
                        return new Bread(quality);
                    } else {
                        throw new UnableToInteractException("The crop is not fully grown!");
                    }
                }
                case "coffee" -> {
                    if (positionInfo.get(1).equals("%")) {
                        positionInfo = List.of(positionInfo.getFirst(), ":", "Stage: 0");
                        farmState.set(positionIndex, positionInfo);
                        return new Coffee(quality);
                    } else {
                        throw new UnableToInteractException("The crop is not fully grown!");
                    }
                }
                case "berry" -> {
                    if (positionInfo.get(1).equals("@")) {
                        positionInfo = List.of(positionInfo.getFirst(), ".", "Stage: 0");
                        farmState.set(positionIndex, positionInfo);
                        return new Jam(quality);
                    } else {
                        throw new UnableToInteractException("The crop is not fully grown!");
                    }
                }
            }
        } else {
            throw new UnableToInteractException("You have a plant on a animal farm!");
        }
        return null;
    }

    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        // if feeding an animal
        switch (command) {
            case "feed" -> {
                if (this.farmType.equals("animal")) {
                    return feed(row, column);
                } else {
                    throw new UnableToInteractException(
                            "You cannot feed something that is not an animal!");
                }
            }
            case "end-day" -> {
                this.endDay();
                return true;
            }
            case "remove" -> {
                this.remove(row, column);
                return true;
            }
        }
        throw new UnableToInteractException("Unknown command: " + command);
    }

    /**
     * Feed an animal at the specified location.
     * @param row the row coordinate
     * @param col the column coordinate
     * @return true iff the animal was fed, else false.
     */
    public boolean feed(int row, int col) {

        if (row < 0 || col < 0 || row >= this.rows || col >= this.columns) {
            return false;
        }

        int positionIndex = (row * this.columns) + col;
        if (positionIndex > farmState.size()) {
            return false;
        }

        List<String> animalList  = List.of("cow", "chicken", "sheep");
        // if the position coordinate is an animal
        if (animalList.contains(this.farmState.get(positionIndex).getFirst())) {
            List<String> positionInfo = farmState.get(positionIndex);
            // set fed to true
            farmState.set(positionIndex, List.of(positionInfo.get(0), positionInfo.get(1),
                    "Fed: true", positionInfo.get(3)));
            return true;
        }
        return false;
    }


    /**
     * Ends the current day, and moves farm to the next day.
     * For plants, this grows the plant if possible.
     * For animals, this sets fed and collection status to false.
     */
    public void endDay() {
        List<String> plantsThatCanGrow = List.of(".", "o", "ἴ", ":", ";", "*");
        List<String> animalSymbols = List.of("४", "ඔ", "৬");

        int i = 0;
        for (List<String> itemInfo : farmState) {
            if (this.farmType.equals("plant")) {
                // if the plant is not at a final stage, increment stage and symbol
                if (plantsThatCanGrow.contains(itemInfo.get(1))) {
                    // berries
                    switch (itemInfo.get(1)) {
                        case "." -> {
                            if (itemInfo.get(2).equals("Stage: 0")) {
                                farmState.set(i, List.of("berry", ".", "Stage: 1"));
                            } else if (itemInfo.get(2).equals("Stage: 1")) {
                                farmState.set(i, List.of("berry", "o", "Stage: 2"));
                            }
                        }
                        case "o" -> farmState.set(i, List.of("berry", "@", "Stage: 3"));
                        case "ἴ" -> {
                            // wheat
                            if (itemInfo.get(2).equals("Stage: 0")) {
                                farmState.set(i, List.of("wheat", "ἴ", "Stage: 1"));
                            } else {
                                farmState.set(i, List.of("wheat", "#", "Stage: 2"));
                            }
                        }
                        case ":" -> {
                            // coffees
                            if (itemInfo.get(2).equals("Stage: 0")) {
                                farmState.set(i, List.of("coffee", ":", "Stage: 1"));
                            } else if (itemInfo.get(2).equals("Stage: 1")) {
                                farmState.set(i, List.of("coffee", ";", "Stage: 2"));
                            }
                        }
                        case ";" -> farmState.set(i, List.of("coffee", "*", "Stage: 3"));
                        case "*" -> farmState.set(i, List.of("coffee", "%", "Stage: 4"));
                    }
                }
            } else if (this.farmType.equals("animal")) {
                // if animal, reset fed and collected to false
                if (animalSymbols.contains(itemInfo.get(1))) {
                    String symbol = itemInfo.get(1);
                    String name = itemInfo.get(0);
                    farmState.set(i, List.of(name, symbol, "Fed: false", "Collected: false"));
                }
            }

            // increment position in list
            i++;
        }
    }

    /**
     * Method for retrieving the stats for the current farm.
     * @return the list describing the current farm state
     */
    public List<List<String>> getTheFarmStatsList() {
        return farmState;
    }



}
