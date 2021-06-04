package be.pxl.auctions.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserGetAgeTest {
	private User user;
	private static final int YEARS = 15;
	private static final int DAYS = 1;

	@BeforeEach
	void init() {
		user = UserBuilder.anUser().build();
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayToday() {
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS));
		assertEquals(YEARS, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).plusDays(DAYS));
		assertEquals(YEARS-1, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenBirthdayWasYesterday() {
		user.setDateOfBirth(LocalDate.now().minusYears(15).minusDays(DAYS));
		assertEquals(YEARS, user.getAge());
	}

}
