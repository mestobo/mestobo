package net.mestobo;

import java.io.File;
import java.net.URL;

/** TestUtils provides auxiliary functions for test execution.
 * @author Robert Lichtenberger
 */
public class TestUtils {
	
	/** Get the directory the project is checked out in
	 * @return The root directory of the project.
	 */
	public static File getRootDirectory() {
		URL url = I18N.class.getProtectionDomain().getCodeSource().getLocation();
		File root = new File(url.getFile());
		while (!new File(root, "LICENSE").exists() && root.getParentFile() != null) {
			root = root.getParentFile();
		}
		return root;
	}

	
	public static File getSourceDirectory() {
		File root = getRootDirectory();
		return new File(root, "src/main/java");
	}

	public static File getTranslationDirectory() {
		File root = getRootDirectory();
		return new File(root, "src/main/resources");
	}

}
