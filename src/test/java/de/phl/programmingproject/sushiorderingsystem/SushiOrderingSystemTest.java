package de.phl.programmingproject.sushiorderingsystem;

import de.phl.programmingproject.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the Sushi Ordering System exercise.
 */
public class SushiOrderingSystemTest {
    private static String FILENAME = "sushi_ordering_system";
    private final static String MARKDOWN_FILENAME = FILENAME + ".md";
    private final static String PDF_FILENAME = FILENAME + ".pdf";

    @Test
    void task_4_sushi_ordering_system_file_exists_in_root_directory() {
        // (i.e. in the root or  'src' directory)
        assertTrue(TestUtils.fileExistsInRootOrSrcDirectory(MARKDOWN_FILENAME)
                || TestUtils.fileExistsInRootOrSrcDirectory(PDF_FILENAME), String.format("The file '%s' (or '%s') does not exist in the root (or './src') directory of the project.", MARKDOWN_FILENAME, PDF_FILENAME));
    }
}
