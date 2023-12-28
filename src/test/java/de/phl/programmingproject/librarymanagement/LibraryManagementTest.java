package de.phl.programmingproject.librarymanagement;

import de.phl.programmingproject.TestBase;
import de.phl.programmingproject.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the Library Management exercise {@link Main}.
 */
public class LibraryManagementTest extends TestBase {

    private static String FILENAME = "library_management";
    private final static String MARKDOWN_FILENAME = FILENAME + ".md";
    private final static String PDF_FILENAME = FILENAME + ".pdf";
    private static String mainMethodContent;
    private static String[] lines;

    private static Object createBook() {
        Constructor<?> bookConstructor = getBookConstructor();
        Object book = null;
        try {
            switch (bookConstructor.getParameterCount()) {
                case 3:
                    book = bookConstructor.newInstance("Book Title", "1234567890", 3);
                    break;
                case 4:
                    Set<Object> authors = new TreeSet<>();
                    authors.add(TestUtils.createInstance(TestUtils.getClassForName("Author", "de.phl.programmingproject.librarymanagement"), "John Doe", new Date()));
                    book = bookConstructor.newInstance("Book Title", "1234567890", 3, authors);
                    break;
                case 0:
                    book = bookConstructor.newInstance();
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println("Failed to create an instance of the Book class.");
        }
        return book;
    }
    private static Constructor<?> getBookConstructor() {
        Class<?> bookClass = TestUtils.getClassForName("Book", "de.phl.programmingproject.librarymanagement");
        Constructor<?> bookConstructor = null;
        try {
            bookConstructor = bookClass.getDeclaredConstructor(String.class, String.class, int.class);
        } catch (Exception e) {
            try {
                bookConstructor = bookClass.getDeclaredConstructor(String.class, String.class, int.class, TreeSet.class);
            } catch (Exception e2) {
                try {
                    bookConstructor = bookClass.getDeclaredConstructor(String.class, String.class, int.class, Set.class);
                } catch (Exception e3) {
                }
            }
        }
        return bookConstructor;
    }

    @Test
    void task_1_library_management_file_exists_in_root_directory() {
        // (i.e. in the root or  'src' directory)
        assertTrue(TestUtils.fileExistsInRootOrSrcDirectory(MARKDOWN_FILENAME)
                || TestUtils.fileExistsInRootOrSrcDirectory(PDF_FILENAME), String.format("The file '%s' (or '%s') does not exist in the root (or './src') directory of the project.", MARKDOWN_FILENAME, PDF_FILENAME));
    }

    @Test
    void task_2_Author_class_implemented() {
        Class<?> authorClass = TestUtils.getClassForName("Author", "de.phl.programmingproject.librarymanagement");
        Map<String, Class<?>> expectedProperties = new HashMap() {{
            put("name", String.class);
            put("dob", Date.class);
            put("books", Set.class);
        }};
        TestUtils.assertClassHasFieldsOfType(authorClass, expectedProperties);

        // assert getters are implemented
        TestUtils.assertClassHasMethod(authorClass, "getName", String.class);
        TestUtils.assertClassHasMethod(authorClass, "getDob", Date.class);
        TestUtils.assertClassHasMethod(authorClass, "getBooks", Set.class);
    }

    @Test
    void task_2_Book_class_implemented() throws NoSuchMethodException {
        Class<?> bookClass = TestUtils.getClassForName("Book", "de.phl.programmingproject.librarymanagement");

        Map<String, Class<?>> expectedProperties = new HashMap() {{
            put("title", String.class);
            put("isbn", String.class);
            put("copies", int.class);
            put("authors", Set.class);
        }};

        TestUtils.assertClassHasFieldsOfType(bookClass, expectedProperties);

        Class<?> memberClass = TestUtils.getClassForName("Member", "de.phl.programmingproject.librarymanagement");
        TestUtils.assertClassHasMethod(bookClass, "borrow", void.class, memberClass);
        TestUtils.assertClassHasMethod(bookClass, "returnByMember", void.class, memberClass);

        // assert getters are implemented
        TestUtils.assertClassHasMethod(bookClass, "getTitle", String.class);
        TestUtils.assertClassHasMethod(bookClass, "getIsbn", String.class);
        TestUtils.assertClassHasMethod(bookClass, "getCopies", int.class);
        TestUtils.assertClassHasMethod(bookClass, "getAuthors", Set.class);

        Constructor<?> bookConstructor = getBookConstructor();

        assertTrue(bookConstructor != null, "The Book class does not have a constructor that takes a title, isbn, copies, and - optionally - authors parameter.");

        // assert that borrow and return are correctly implemented
        Object book = createBook();
        Object member = TestUtils.createInstance(memberClass, 1, "John Doe");
        int copies = (int) TestUtils.invokeMethod(book, "getCopies");
        TestUtils.invokeMethod(book, "borrow", member);
        assertEquals(1, ((Set<?>) TestUtils.invokeMethod(member, "getBooks")).size(),
                "The 'borrow' method of the Book class does not add the book to the member's books.");
        assertEquals(copies-1, TestUtils.invokeMethod(book, "getCopies"),
                "The 'borrow' method of the Book class does not decrease the number of copies by 1.");
        TestUtils.invokeMethod(book, "returnByMember", member);
        assertEquals(0, ((Set<?>) TestUtils.invokeMethod(member, "getBooks")).size(),
                "The 'returnByMember' method of the Book class does not remove the book from the member's books.");
        assertEquals(copies, TestUtils.invokeMethod(book, "getCopies"), "The 'returnByMember' method of the Book class does not increase the number of copies by 1.");
    }

    @Test
    void task_2_Member_class_implemented() {
        Class<?> memberClass = TestUtils.getClassForName("Member", "de.phl.programmingproject.librarymanagement");
        Map<String, Class<?>> expectedProperties = new HashMap() {{
            put("id", int.class);
            put("name", String.class);
            put("books", Set.class);
        }};

        TestUtils.assertClassHasFieldsOfType(memberClass, expectedProperties);

        // assert getters are implemented
        TestUtils.assertClassHasMethod(memberClass, "getId", int.class);
        TestUtils.assertClassHasMethod(memberClass, "getName", String.class);
        TestUtils.assertClassHasMethod(memberClass, "getBooks", Set.class);

        // assert borrowBook and returnBook are implemented
        Class<?> bookClass = TestUtils.getClassForName("Book", "de.phl.programmingproject.librarymanagement");
        TestUtils.assertClassHasMethod(memberClass, "borrowBook", void.class, bookClass);
        TestUtils.assertClassHasMethod(memberClass, "returnBook", void.class, bookClass);

        // assert that the constructor is implemented
        Constructor<?> memberConstructor = TestUtils.getConstructor(memberClass, int.class, String.class);

        // assert that borrowBook and returnBook are correctly implemented
        Object member = TestUtils.createInstance(memberClass, 1, "John Doe");
        Object book = createBook();
        TestUtils.invokeMethod(member, "borrowBook", book);
        assertEquals(1, ((Set<?>) TestUtils.invokeMethod(member, "getBooks")).size(),
                "The 'borrowBook' method of the Member class does not add the book to the member's books.");

        TestUtils.invokeMethod(member, "returnBook", book);
        assertEquals(0, ((Set<?>) TestUtils.invokeMethod(member, "getBooks")).size(),
                "The 'returnBook' method of the Member class does not remove the book from the member's books.");
    }

    @Test
    void task_2_Library_class_implemented(){
        Class<?> libraryClass = TestUtils.getClassForName("Library", "de.phl.programmingproject.librarymanagement");

        Map<String, Class<?>> expectedProperties = new HashMap() {{
            put("name", String.class);
            put("books", Set.class);
            put("members", Set.class);
            put("authors", Set.class);
        }};
        TestUtils.assertClassHasFieldsOfType(libraryClass, expectedProperties);

        // assert getters are implemented
        TestUtils.assertClassHasMethod(libraryClass, "getName", String.class);
        TestUtils.assertClassHasMethod(libraryClass, "getBooks", Set.class);
        TestUtils.assertClassHasMethod(libraryClass, "getMembers", Set.class);
        TestUtils.assertClassHasMethod(libraryClass, "getAuthors", Set.class);

        // assert add and remove methods are implemented
        Class<?> memberClass = TestUtils.getClassForName("Member", "de.phl.programmingproject.librarymanagement");
        Class<?> bookClass = TestUtils.getClassForName("Book", "de.phl.programmingproject.librarymanagement");
        Object book = createBook();
        Object member = TestUtils.createInstance(memberClass, 1, "John Doe");
        Object library = TestUtils.createInstance(libraryClass, "PHLohmarkt");
        Object author = TestUtils.createInstance(TestUtils.getClassForName("Author", "de.phl.programmingproject.librarymanagement"), "John Doe", new Date());

        TestUtils.assertClassHasMethod(libraryClass, "addBook", void.class, bookClass);
        TestUtils.invokeMethod(library, "addBook", book);
        assertEquals(1, ((Set<?>) TestUtils.invokeMethod(library, "getBooks")).size(),
                "The 'addBook' method of the Library class does not add the book to the library's books.");
        TestUtils.invokeMethod(library, "removeBook", book);
        assertEquals(0, ((Set<?>) TestUtils.invokeMethod(library, "getBooks")).size(),
                "The 'removeBook' method of the Library class does not remove the book from the library's books.");

        TestUtils.invokeMethod(library, "addAuthor", author);
        assertEquals(1, ((Set<?>) TestUtils.invokeMethod(library, "getAuthors")).size(),
                "The 'addAuthor' method of the Library class does not add the author to the library's authors.");
        TestUtils.invokeMethod(library, "removeAuthor", author);
        assertEquals(0, ((Set<?>) TestUtils.invokeMethod(library, "getAuthors")).size(),
                "The 'removeAuthor' method of the Library class does not remove the author from the library's authors.");

        TestUtils.invokeMethod(library, "addMember", member);
        assertEquals(1, ((Set<?>) TestUtils.invokeMethod(library, "getMembers")).size(),
                "The 'addMember' method of the Library class does not add the member to the library's members.");
        TestUtils.invokeMethod(library, "removeMember", member);
        assertEquals(0, ((Set<?>) TestUtils.invokeMethod(library, "getMembers")).size(),
                "The 'removeMember' method of the Library class does not remove the member from the library's members.");

    }

    @BeforeAll
    static void prepare() {
        mainMethodContent = TestUtils.getFileContentForFileInRootOrSrcDirectory("/main/java/de/phl/programmingproject/librarymanagement/Main.java");
        lines = mainMethodContent.split("\n");
    }
    @Test
    void task_3_main_method_implemented() {
        Class<?> mainClass = TestUtils.getClassForName("Main", "de.phl.programmingproject.librarymanagement");
        TestUtils.assertClassHasMethod(mainClass, "main", void.class, String[].class);

        assertTrue(mainMethodContent.contains("new Author"),
                "The main method does not create an instance of the Author class.");

        assertTrue(mainMethodContent.contains("new Book"),
                "The main method does not create an instance of the Book class.");

        assertTrue(mainMethodContent.contains("new Member"),
                "The main method does not create an instance of the Member class.");

        assertTrue(mainMethodContent.contains("new Library"),
                "The main method does not create an instance of the Library class.");

        assertTrue(mainMethodContent.contains("addBook"),
                "The main method does not call the 'addBook' method of the Library class.");

        assertTrue(mainMethodContent.contains("addAuthor"),
                "The main method does not call the 'addAuthor' method of the Library class.");

        assertTrue(mainMethodContent.contains("addMember"),
                "The main method does not call the 'addMember' method of the Library class.");

        assertTrue(mainMethodContent.contains("borrow"),
                "The main method does not call the 'borrowBook' method of the Member or Book class.");

        assertTrue(mainMethodContent.contains("returnByMember") ||
                mainMethodContent.contains("returnBook"),
                "The main method does not call the 'returnByMember' method of the Book class or the 'returnBook' method of the Member class.");

    }



}
