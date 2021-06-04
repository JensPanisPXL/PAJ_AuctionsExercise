package be.pxl.auctions.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorIsValidTest {

	@Test
	public void returnsTrueWhenValidEmail() {
		String email = "jenspanis@gmail.com";
		assertTrue(EmailValidator.isValid(email));

		//Korter voorbeeld:
		//assertTrue(EmailValidator.isValid("jenspanis@gmail.com"));
	}

	@Test
	public void returnsFalseWhenAtSignMissing() {
		String email = "jenspanisgmail.com";
		assertFalse(EmailValidator.isValid(email));
	}
}
