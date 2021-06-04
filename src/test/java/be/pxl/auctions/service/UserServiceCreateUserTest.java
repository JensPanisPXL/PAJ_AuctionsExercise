package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.User;
import be.pxl.auctions.model.UserBuilder;
import be.pxl.auctions.model.UserCreateResourceBuilder;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceCreateUserTest {

    private static final String USER_EMAIL = "mark@facebook.com";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String DATE_IN_FUTURE = LocalDate.now().plusDays(1).format(FORMATTER);

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;
    private UserCreateResource userCreateResource;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void init() {
        userCreateResource = UserCreateResourceBuilder.anUserCreateResource().build();
    }

    @Test
    void throwsRequiredFieldExceptionWhenUserCreateResourceHasNoFirstName() {
        userCreateResource.setFirstName("");
        assertThrows(RequiredFieldException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenUserCreateResourceHasNoLastName() {
        userCreateResource.setLastName(null);
        assertThrows(RequiredFieldException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenUserCreateResourceHasNoEmail() {
        userCreateResource.setEmail("         ");
        assertThrows(RequiredFieldException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsInvalidEmailExceptionWhenUserCreateResourceHasInvalidEmail() {
        userCreateResource.setEmail("markzuckerberg.com");
        assertThrows(InvalidEmailException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenUserCreateResourceHasNoDateOfBirth() {
        userCreateResource.setDateOfBirth(null);
        assertThrows(RequiredFieldException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsDuplicateEmailExceptionWhenUserCreateResourceHasAUsedEmail() {
        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        assertThrows(DuplicateEmailException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsInvalidDateExceptionWhenUserCreateResourceHasADateOfBirthInTheFuture() {
        userCreateResource.setDateOfBirth(DATE_IN_FUTURE);

        assertThrows(InvalidDateException.class,
                () -> userService.createUser(userCreateResource));
    }

    @Test
    void mapToUserSavesUserCorrectly() {
        when(userDao.findUserByEmail(UserCreateResourceBuilder.EMAIL)).thenReturn(Optional.empty());
        when(userDao.saveUser(any())).thenAnswer(returnsFirstArg());

        UserDTO createdUser = userService.createUser(userCreateResource);
        assertNotNull(createdUser);

        verify(userDao).saveUser(userCaptor.capture());
        User userSaved = userCaptor.getValue();
        assertEquals(UserCreateResourceBuilder.FIRST_NAME, userSaved.getFirstName());
        assertEquals(UserCreateResourceBuilder.LAST_NAME, userSaved.getLastName());
        assertEquals(UserCreateResourceBuilder.EMAIL, userSaved.getEmail());
        assertEquals(UserCreateResourceBuilder.DATE_OF_BIRTH, userSaved.getDateOfBirth().format(FORMATTER));
    }
}
