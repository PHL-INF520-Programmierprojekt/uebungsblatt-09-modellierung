package de.phl.programmingproject.banking;

import de.phl.programmingproject.TestUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class BankingTest {

    private static String FILENAME = "banking";
    private final static String MARKDOWN_FILENAME = FILENAME + ".md";
    private final static String PDF_FILENAME = FILENAME + ".pdf";

    @Test
    void task_3_banking_file_exists_in_root_directory() {
        assertTrue(TestUtils.fileExistsInRootOrSrcDirectory(MARKDOWN_FILENAME)
                || TestUtils.fileExistsInRootOrSrcDirectory(PDF_FILENAME), String.format("The file '%s' (or '%s') does not exist in the root (or './src') directory of the project.", MARKDOWN_FILENAME, PDF_FILENAME));
    }

    @Test
    void task_5_Bank_class_refactored() {
        Class<?> bankClass = TestUtils.getClassForName("Bank", "de.phl.programmingproject.banking");
        Class<?> bankAccountClass = TestUtils.getClassForName("BankAccount", "de.phl.programmingproject.banking");
        Class<?> holderClass = TestUtils.getClassForName("Holder", "de.phl.programmingproject.banking");

        // assert that the 'addAccount'  method was removed
        assertThrows(NoSuchMethodException.class, () -> {
            bankClass.getDeclaredMethod("addAccount", bankAccountClass);
        }, "The constructor of the Bank class does not throw an IllegalArgumentException if the name is null.");

        // assert that either a 'openAccount' or 'openSavingsAccount' or 'openCheckingAccount' method was added
        try {
            Method openAccountMethod = bankClass.getDeclaredMethod("openAccount", holderClass, bankAccountClass);
        } catch (Exception e) {
            try {
                Method openSavingsAccountMethod = bankClass.getDeclaredMethod("openSavingsAccount", holderClass, double.class, double.class);
            } catch (Exception e1) {
                try {
                    Method openCheckingAccountMethod = bankClass.getDeclaredMethod("openCheckingAccount", holderClass, double.class, double.class);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    fail("The Bank class does not have a method 'openAccount' or 'openSavingsAccount' or 'openCheckingAccount'.");
                }
            }
        }
    }
}
