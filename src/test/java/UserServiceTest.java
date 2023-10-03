import dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");
    private UserService userService;

     @BeforeAll
     void init(){
         System.out.println("Before all: " + this);
     }

     @BeforeEach
    void prepare(){
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
    void userEmptyIfNoUserAdded(){
        System.out.println("Test 1: " + this);
        List<User> user = userService.getAll();
        assertTrue(user.isEmpty());
    }

    @Test
     void UserSizeIfUserAdded(){
        System.out.println("Test 2: " + this);
        userService.add(IVAN);
        userService.add(PETR);

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExists(){
         userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @AfterEach
     void deleteDataFromDatabase(){
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool(){
        System.out.println("After all: " + this);
    }
}
