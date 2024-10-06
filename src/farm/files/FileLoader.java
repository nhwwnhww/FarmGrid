package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileLoader {
    private String farmType;

    /**
     * Constructor for the FileLoader
     */
    public FileLoader() {
    }

    /**
     * Loads contents of the specified file into a Grid.
     * @param filename the String filename to read contents from.
     * @return a grid instance.
     * @throws IOException
     */
    public Grid load(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read grid dimensions
            String[] dimensions = reader.readLine().split(",");
            int rows = Integer.parseInt(dimensions[0]);
            int columns = Integer.parseInt(dimensions[1]);

            // Create a new FarmGrid object
            FarmGrid grid = new FarmGrid(rows, columns);

            // Read each cell's state and populate the grid
            List<List<String>> gridState = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> cell = Arrays.asList(line.split(","));
                gridState.add(cell);
            }

            // Set the grid's state
            grid.farmState = gridState;
            return grid;
        } catch (IOException e) {
            System.err.println("Error while loading grid from file: " + e.getMessage());
            return null;
        }
    }
}
