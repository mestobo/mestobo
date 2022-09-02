package net.mestobo;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** TestUtilsTest tests TestUtils.
 * @author Robert Lichtenberger
 */
public class TestUtilsTest {

	@Test
	public void testGetRootDirectory() {
		File root = TestUtils.getRootDirectory();
		Assertions.assertTrue(new File(root, "README.md").exists());
	}
	
	@Test
	public void testGetSourceDirectory() {
		File sourceDir = TestUtils.getSourceDirectory();
		Assertions.assertTrue(new File(sourceDir, "net/mestobo/Mestobo.java").exists());
	}
	
	@Test
	public void testGetTranslationDirectory() {
		File translationDir = TestUtils.getTranslationDirectory();
		Assertions.assertTrue(new File(translationDir, "net/mestobo/mestobo.properties").exists());
	}
	
}
