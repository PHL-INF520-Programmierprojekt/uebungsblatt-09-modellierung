package de.phl.programmingproject.onlineshoppingcheckout;

import de.phl.programmingproject.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the 'Online Shopping Checkout' exercise.
 */
public class OnlineShoppingCheckoutTest {

    private static String FILENAME = "online_shopping_checkout";
    private final static String MARKDOWN_FILENAME = FILENAME + ".md";
    private final static String PDF_FILENAME = FILENAME + ".pdf";

    @Test
    void task_4_banking_file_exists_in_root_directory() {
        assertTrue(TestUtils.fileExistsInRootOrSrcDirectory(MARKDOWN_FILENAME)
                || TestUtils.fileExistsInRootOrSrcDirectory(PDF_FILENAME), String.format("The file '%s' (or '%s') does not exist in the root (or './src') directory of the project.", MARKDOWN_FILENAME, PDF_FILENAME));
    }
}
