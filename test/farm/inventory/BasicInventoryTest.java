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
    public void testAddProduct() {
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        assertTrue(inventory.existsProduct(Barcode.EGG));
    }

    @Test
    public void testAddProductThrowsException() {
        try {
            inventory.addProduct(Barcode.EGG, Quality.REGULAR, 2);
            fail("Expected InvalidStockRequestException to be thrown");
        } catch (InvalidStockRequestException e) {
            assertEquals("Current inventory is not fancy enough. Please supply products one at a time.", e.getMessage());
        }
    }

    @Test
    public void testExistsProduct() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        assertTrue(inventory.existsProduct(Barcode.MILK));
        assertFalse(inventory.existsProduct(Barcode.BREAD));
    }

    @Test
    public void testRemoveProduct() {
        inventory.addProduct(Barcode.JAM, Quality.REGULAR);
        List<Product> removed = inventory.removeProduct(Barcode.JAM);
        assertEquals(1, removed.size());
        assertEquals(Barcode.JAM, removed.getFirst().getBarcode());
        assertFalse(inventory.existsProduct(Barcode.JAM));
    }

    @Test
    public void testRemoveProductThrowsException() {
        try {
            inventory.removeProduct(Barcode.JAM, 3);
            fail("Expected FailedTransactionException to be thrown");
        } catch (FailedTransactionException e) {
            assertEquals("Current inventory is not fancy enough. Please purchase products one at a time.", e.getMessage());
        }
    }

    @Test
    public void testGetAllProducts() {
        inventory.addProduct(Barcode.WOOL, Quality.REGULAR);
        inventory.addProduct(Barcode.COFFEE, Quality.SILVER);

        List<Product> products = inventory.getAllProducts();
        assertEquals(2, products.size());
    }
}
