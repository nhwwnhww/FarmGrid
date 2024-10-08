package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class BasicInventoryTest {
    private BasicInventory inventory;

    @Before
    public void setUp() {
        inventory = new BasicInventory();
    }

    @Test
    public void testAddSingleProductDoesNotThrowException() {
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        assertTrue("Product EGG should exist in inventory", inventory.existsProduct(Barcode.EGG));
    }

    @Test
    public void testRemoveSingleProductDoesNotThrowException() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        List<Product> removed = inventory.removeProduct(Barcode.MILK);
        assertEquals("Should remove one product", 1, removed.size());
        assertEquals("Removed product should be MILK", Barcode.MILK, removed.get(0).getBarcode());
    }

    @Test
    public void testAddProductWithQuantityOneThrowsException() {
        try {
            inventory.addProduct(Barcode.EGG, Quality.REGULAR, 1);
            fail("Expected InvalidStockRequestException to be thrown");
        } catch (InvalidStockRequestException e) {
            assertEquals(
                    "Current inventory is not fancy enough. Please supply products one at a time.",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testRemoveProductWithQuantityOneThrowsException() {
        try {
            inventory.removeProduct(Barcode.MILK, 1);
            fail("Expected FailedTransactionException to be thrown");
        } catch (FailedTransactionException e) {
            assertEquals(
                    "Current inventory is not fancy enough. Please purchase products one at a time.",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testAddAndRemoveMultipleSingleProducts() {
        for (int i = 0; i < 5; i++) {
            inventory.addProduct(Barcode.EGG, Quality.REGULAR);
            assertTrue("Product EGG should exist in inventory", inventory.existsProduct(Barcode.EGG));
            List<Product> removed = inventory.removeProduct(Barcode.EGG);
            assertEquals("Should remove one product", 1, removed.size());
        }
    }

    @Test
    public void testAddProductDoesNotThrowException() {
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        assertTrue("Product EGG should exist in inventory", inventory.existsProduct(Barcode.EGG));
    }

    @Test
    public void testRemoveProductDoesNotThrowException() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        List<Product> removed = inventory.removeProduct(Barcode.MILK);
        assertEquals("Should remove one product", 1, removed.size());
        assertEquals("Removed product should be MILK", Barcode.MILK, removed.get(0).getBarcode());
    }

    @Test
    public void testExistsProductDoesNotThrowException() {
        inventory.addProduct(Barcode.BREAD, Quality.SILVER);
        assertTrue("Product BREAD should exist in inventory", inventory.existsProduct(Barcode.BREAD));
        assertFalse("Product JAM should not exist in inventory", inventory.existsProduct(Barcode.JAM));
    }

    @Test
    public void testGetAllProductsReturnsAllProducts() {
        inventory.addProduct(Barcode.WOOL, Quality.REGULAR);
        inventory.addProduct(Barcode.COFFEE, Quality.SILVER);
        List<Product> products = inventory.getAllProducts();
        assertEquals("Inventory should contain 2 products", 2, products.size());
        assertTrue("Inventory should contain WOOL product",
                products.stream().anyMatch(p -> p.getBarcode() == Barcode.WOOL && p.getQuality() == Quality.REGULAR));
        assertTrue("Inventory should contain COFFEE product",
                products.stream().anyMatch(p -> p.getBarcode() == Barcode.COFFEE && p.getQuality() == Quality.SILVER));
    }

    @Test
    public void testGetAllProductsWhenEmpty() {
        List<Product> products = inventory.getAllProducts();
        assertTrue("Inventory should be empty", products.isEmpty());
    }

    @Test
    public void testRemoveProductRemovesOnlySpecifiedBarcode() {
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        inventory.addProduct(Barcode.MILK, Quality.GOLD);
        inventory.addProduct(Barcode.BREAD, Quality.SILVER);

        List<Product> removed = inventory.removeProduct(Barcode.EGG);
        assertEquals("Should remove one product", 1, removed.size());
        assertEquals("Removed product should be EGG", Barcode.EGG, removed.get(0).getBarcode());

        assertTrue("Product MILK should still exist", inventory.existsProduct(Barcode.MILK));
        assertTrue("Product BREAD should still exist", inventory.existsProduct(Barcode.BREAD));
        assertFalse("Product EGG should no longer exist", inventory.existsProduct(Barcode.EGG));
    }

    @Test
    public void testRemoveNonExistentProductDoesNotAffectInventory() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        inventory.addProduct(Barcode.BREAD, Quality.GOLD);

        List<Product> removed = inventory.removeProduct(Barcode.JAM);
        assertTrue("No products should be removed", removed.isEmpty());

        assertTrue("Product MILK should still exist", inventory.existsProduct(Barcode.MILK));
        assertTrue("Product BREAD should still exist", inventory.existsProduct(Barcode.BREAD));
        assertFalse("Product JAM should not exist", inventory.existsProduct(Barcode.JAM));
    }

    @Test
    public void testRemoveProductWhenInventoryIsEmptyDoesNotThrowException() {
        List<Product> removed = inventory.removeProduct(Barcode.EGG);
        assertTrue("No products should be removed from an empty inventory", removed.isEmpty());
    }

    @Test
    public void testAddAndRemoveMultipleSameProducts() {
        inventory.addProduct(Barcode.COFFEE, Quality.REGULAR);
        inventory.addProduct(Barcode.COFFEE, Quality.SILVER);

        assertEquals("Inventory should contain 2 products", 2, inventory.getAllProducts().size());

        List<Product> removedFirst = inventory.removeProduct(Barcode.COFFEE);
        assertEquals("Should remove one product", 1, removedFirst.size());
        assertEquals("Removed product should be COFFEE", Barcode.COFFEE, removedFirst.get(0).getBarcode());

        assertTrue("Product COFFEE should still exist", inventory.existsProduct(Barcode.COFFEE));
        assertEquals("Inventory should contain 1 product", 1, inventory.getAllProducts().size());

        List<Product> removedSecond = inventory.removeProduct(Barcode.COFFEE);
        assertEquals("Should remove one product", 1, removedSecond.size());
        assertEquals("Removed product should be COFFEE", Barcode.COFFEE, removedSecond.get(0).getBarcode());

        assertFalse("Product COFFEE should no longer exist", inventory.existsProduct(Barcode.COFFEE));
        assertTrue("Inventory should be empty", inventory.getAllProducts().isEmpty());
    }
}
