package be.pxl.auctions.dao.impl;

import be.pxl.auctions.model.User;
import be.pxl.auctions.model.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class UserDaoImplTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserDaoImpl userDao;

	@Test
	public void userCanBeSavedAndRetrievedById() {
		User user = UserBuilder.anUser().build();
		long newUserId = userDao.saveUser(user).getId();
		//Wegschrijven naar de DB
		entityManager.flush();
		//EntityManager clearen
		entityManager.clear();

		Optional<User> retrievedUser = userDao.findUserById(newUserId);
		assertTrue(retrievedUser.isPresent());

		assertEquals(UserBuilder.FIRST_NAME, retrievedUser.get().getFirstName());
		assertEquals(UserBuilder.LAST_NAME, retrievedUser.get().getLastName());
		assertEquals(UserBuilder.EMAIL, retrievedUser.get().getEmail());
		assertEquals(UserBuilder.DATE_OF_BIRTH, retrievedUser.get().getDateOfBirth());
	}
	@Test
	public void userCanBeSavedAndRetrievedByEmail() {
		User user = UserBuilder.anUser().build();
		String newUserEmail = userDao.saveUser(user).getEmail();

		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userDao.findUserByEmail(newUserEmail);
		assertTrue(retrievedUser.isPresent());

		assertEquals(UserBuilder.FIRST_NAME, retrievedUser.get().getFirstName());
		assertEquals(UserBuilder.LAST_NAME, retrievedUser.get().getLastName());
		assertEquals(UserBuilder.EMAIL, retrievedUser.get().getEmail());
		assertEquals(UserBuilder.DATE_OF_BIRTH, retrievedUser.get().getDateOfBirth());
	}

	@Test
	public void EmptyOptionalWhenNoUserFoundWithGivenEmail() {
		Optional<User> user = userDao.findUserByEmail("Micky@disney.com");

		assertTrue(user.isEmpty());
	}

	@Test
	public void allUsersCanBeRetrieved() {
		int currentAmountOfUsers = userDao.findAllUsers().size();

		// create and save one user
		User user = UserBuilder.anUser().build();
		userDao.saveUser(user);
		entityManager.flush();
		entityManager.clear();

		// retrieve all users
		List<User> allUsersPlusAdded = userDao.findAllUsers();
		// make sure there is at least 1 user in the list
		assertFalse(allUsersPlusAdded.isEmpty());
		assertEquals(currentAmountOfUsers + 1, allUsersPlusAdded.size());
		// make sure the newly created user is in the list (e.g. test if a user with this email address is in the list)
		Optional<User> retrievedUser = userDao.findUserByEmail(UserBuilder.EMAIL);
		assertTrue(retrievedUser.isPresent());
		assertEquals(UserBuilder.FIRST_NAME, retrievedUser.get().getFirstName());
		assertEquals(UserBuilder.LAST_NAME, retrievedUser.get().getLastName());
		assertEquals(UserBuilder.EMAIL, retrievedUser.get().getEmail());
		assertEquals(UserBuilder.DATE_OF_BIRTH, retrievedUser.get().getDateOfBirth());

		// Check if there is only one with that email
		assertEquals(1, allUsersPlusAdded.stream().filter(u -> u.getEmail().equals(UserBuilder.EMAIL)).count());
	}


}
