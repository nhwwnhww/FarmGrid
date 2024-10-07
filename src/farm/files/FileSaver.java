package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code FileSaver} class is responsible for saving the state of a {@link Grid} object
 * to a file. It writes the grid's dimensions and the state of each cell to the specified file.
 */
public class FileSaver {

    /**
     * Constructor for the FileSaver class.
     */
    public FileSaver() {

    }

    /**
     * Saves the contents of the provided {@link Grid} to a file with the specified name.
     * The file will contain the grid's dimensions on the first line, followed by the state
     * of each cell on subsequent lines. Each cell's state is written as comma-separated values.
     *
     * @param filename the name of the file to save the grid to.
     * @param grid the {@link Grid} instance whose state will be saved.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    public void save(String filename, Grid grid) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write the grid dimensions first
            writer.write(grid.getRows() + "," + grid.getColumns() + "\n");

            // Write each cell's state
            List<List<String>> gridState = grid.getStats();
            for (List<String> cell : gridState) {
                writer.write(String.join(",", cell) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error while saving grid to file: " + e.getMessage());
        }
    }
}
