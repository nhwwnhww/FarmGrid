package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FancyInventoryTest {
    private FancyInventory inventory;

    @Before
    public void setUp() {
        inventory = new FancyInventory();
    }

    @Test
    public void testAddSingleProduct() {
        // Test adding a single product
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        assertTrue("Product EGG should exist in the inventory", inventory.existsProduct(Barcode.EGG));
        assertEquals("The quantity of EGG should be 1", 1, inventory.getStockedQuantity(Barcode.EGG));
    }

    @Test
    public void testAddMultipleProducts() {
        // Test adding multiple products
        try {
            inventory.addProduct(Barcode.MILK, Quality.REGULAR, 3);
        } catch (InvalidStockRequestException e) {
            fail("InvalidStockRequestException should not be thrown");
        }
        assertTrue("Product MILK should exist in the inventory", inventory.existsProduct(Barcode.MILK));
        assertEquals("The quantity of MILK should be 3", 3, inventory.getStockedQuantity(Barcode.MILK));
    }

    @Test
    public void testExistsProduct() {
        // Test existence of products
        inventory.addProduct(Barcode.JAM, Quality.REGULAR);
        assertTrue("Product JAM should exist", inventory.existsProduct(Barcode.JAM));
        assertFalse("Product BREAD should not exist", inventory.existsProduct(Barcode.BREAD));
    }

    @Test
    public void testRemoveHighestQualityProduct() {
        // Test removing the highest quality product
        inventory.addProduct(Barcode.WOOL, Quality.REGULAR);
        inventory.addProduct(Barcode.WOOL, Quality.GOLD);
        inventory.addProduct(Barcode.WOOL, Quality.SILVER);

        List<Product> removedProducts = inventory.removeProduct(Barcode.WOOL);

        assertEquals("Only one product should be removed", 1, removedProducts.size());
        assertEquals("The removed product should be the GOLD Wool", Quality.GOLD, removedProducts.getFirst().getQuality());
        assertEquals("The remaining quantity of Wool should be 2", 2, inventory.getStockedQuantity(Barcode.WOOL));
    }

    @Test
    public void testRemoveMultipleProducts() throws FailedTransactionException {
        // Test removing multiple products
        try {
            inventory.addProduct(Barcode.COFFEE, Quality.REGULAR, 5);
        } catch (InvalidStockRequestException e) {
            fail("InvalidStockRequestException should not be thrown");
        }

        List<Product> removedProducts = inventory.removeProduct(Barcode.COFFEE, 3);
        assertEquals("Three products should be removed", 3, removedProducts.size());
        assertEquals("The remaining quantity of COFFEE should be 2", 2, inventory.getStockedQuantity(Barcode.COFFEE));
    }

    @Test
    public void testRemoveMoreProductsThanAvailable() throws FailedTransactionException {
        // Test attempting to remove more products than available
        try {
            inventory.addProduct(Barcode.BREAD, Quality.REGULAR, 2);
        } catch (InvalidStockRequestException e) {
            fail("InvalidStockRequestException should not be thrown");
        }

        List<Product> removedProducts = inventory.removeProduct(Barcode.BREAD, 5);
        assertEquals("Only 2 products should be removed since that's all that was added", 2, removedProducts.size());
        assertEquals("There should be no BREAD left", 0, inventory.getStockedQuantity(Barcode.BREAD));
    }

    @Test
    public void testGetAllProducts() {
        // Test getting all products
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        inventory.addProduct(Barcode.WOOL, Quality.REGULAR);

        List<Product> products = inventory.getAllProducts();

        assertEquals("There should be 3 products in total", 3, products.size());
        assertEquals("First product should be EGG", Barcode.EGG, products.get(0).getBarcode());
        assertEquals("Second product should be MILK", Barcode.MILK, products.get(1).getBarcode());
        assertEquals("Third product should be WOOL", Barcode.WOOL, products.get(2).getBarcode());
    }

    @Test
    public void testGetStockedQuantity() {
        // Test getting the stocked quantity
        inventory.addProduct(Barcode.JAM, Quality.SILVER);
        inventory.addProduct(Barcode.JAM, Quality.REGULAR);
        assertEquals("The stocked quantity of JAM should be 2", 2, inventory.getStockedQuantity(Barcode.JAM));
    }
}
