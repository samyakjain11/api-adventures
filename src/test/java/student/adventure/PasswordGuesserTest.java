package student.adventure;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordGuesserTest
{

	//here i am testing a method that is not currently being used in the game engine, HOWEVER since the method
	//may be useful in the future i think it is valid to still test this method
	@Test
	public void passwordGuesserStringRepeatLetters() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("__ll_", p.takeStringInput("l"));
	}

	@Test
	public void passwordGuesserStringPassString() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("_ell_", p.takeStringInput("el"));
	}

	@Test
	public void passwordGuesserStringGuessWholeWord() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("hello", p.takeStringInput("hello"));
	}

	@Test
	public void passwordGuesserStringNullInput() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("_____", p.takeStringInput(null));
	}

	@Test
	public void passwordGuesserStringEmptyString() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("_____", p.takeStringInput(""));
	}

	@Test
	public void passwordGuesserStringMultipleWords() {
		PasswordGuesser p = new PasswordGuesser("hello and hi");
		assertEquals("h____ ___ h_", p.takeStringInput("h"));
	}

	@Test
	public void passwordGuesserStringMultipleWordsGuessWholeWord() {
		PasswordGuesser p = new PasswordGuesser("hello and hi");
		assertEquals("hello and hi", p.takeStringInput("hello and hi"));
	}

	@Test
	public void passwordGuesserStringCapitals() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("h____", p.takeStringInput("H"));
	}


	//testing the method that only takes a single char as input
	@Test
	public void passwordGuesserCharRepeatingLetter() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("__ll_", p.takeCharInput('l'));
	}

	@Test
	public void passwordGuesserCharSpaceInput() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("_____", p.takeCharInput(' '));
	}

	@Test
	public void passwordGuesserCharMultipleWords() {
		PasswordGuesser p = new PasswordGuesser("hello and hi");
		assertEquals("h____ ___ h_", p.takeCharInput('h'));
	}

	@Test
	public void passwordGuesserCharCapitals() {
		PasswordGuesser p = new PasswordGuesser("hello");
		assertEquals("h____", p.takeCharInput('H'));
	}
}
