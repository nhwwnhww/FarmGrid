package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class FileSaver {

    /**
     * Constructor for the FileSaver class.
     */
    public FileSaver() {

    }

    /**
     * Saves the contents of the provided grid and farmtype to a file with specified name.
     * @param filename
     * @param grid the grid instance to save to a file
     * @throws IOException
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
