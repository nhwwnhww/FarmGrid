package farm.core.farmgrid.farmitem;

/**
 * Class representing a Cell on the farm grid, which can hold a FarmItem.
 */
public class Cell {
    private FarmItem item;

    /**
     * Default constructor initializing the cell as empty.
     */
    public Cell() {
        this.item = null;
    }

    /**
     * Checks if the cell is empty.
     *
     * @return true if the cell has no item, false otherwise.
     */
    public boolean isEmpty() {
        return item == null;
    }

    /**
     * Places a FarmItem in the cell.
     *
     * @param item - The FarmItem to place in the cell.
     */
    public void placeItem(FarmItem item) {
        this.item = item;
    }

    /**
     * Removes the item from the cell, making the cell empty.
     */
    public void removeItem() {
        this.item = null;
    }

    /**
     * Retrieves the current item in the cell.
     *
     * @return the FarmItem currently in the cell, or null if the cell is empty.
     */
    public FarmItem getItem() {
        return item;
    }

    /**
     * Returns the symbol representing the item in the cell.
     *
     * @return the symbol of the FarmItem if present, otherwise returns a space character (' ').
     */
    public char getSymbol() {
        return isEmpty() ? ' ' : item.getSymbol();
    }
}

