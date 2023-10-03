import dto.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");
    private UserService userService;

    UserServiceTest(TestInfo testInfo){
        System.out.println();
    }

    @BeforeAll
    void init() {
        System.out.println("Before all: " + this);
    }

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
    void userEmptyIfNoUserAdded() {
        System.out.println("Test 1: " + this);
        List<User> user = userService.getAll();
        assertTrue(user.isEmpty());
    }

    @Test
    void UserSizeIfUserAdded() {
        System.out.println("Test 2: " + this);
        userService.add(IVAN);
        userService.add(PETR);

        List<User> users = userService.getAll();
        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }



    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN);
        userService.add(PETR);
        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(PETR, IVAN)
        );
    }



    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After all: " + this);
    }


    @Nested
    @DisplayName("test user login functionality")
    @Tag("login")
    class TestLogin{
        @Test
        void logicFailIfPasswordIsCorrect() {
            userService.add(IVAN);
            var maybeUser = userService.login(IVAN.getUsername(), "qwert");

            assertTrue(maybeUser.isEmpty());
        }

        @Test
        void logicFailIfUserDoesNotExist() {
            userService.add(IVAN);
            var maybeUser = userService.login("qwert", IVAN.getPassword());

            assertTrue(maybeUser.isEmpty());
        }

        @Test
        void loginSuccessIfUserExists() {
            userService.add(IVAN);
            Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

            assertThat(maybeUser).isPresent();
            maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));

//        assertTrue(maybeUser.isPresent());
//        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
        }

        @Test
        void throwExeptionIfUserNameOrPusswordIsNull() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dumn")),
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(IVAN.getUsername(), null))

            );

//         try {
//             userService.login(null, "dumn");
//             Assertions.fail("exeption");
//         } catch (IllegalArgumentException exception){
//             assertTrue(true);
//         }
        }
    }
}


