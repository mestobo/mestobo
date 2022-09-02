package net.mestobo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** I18NTest tests Internationalization of mestobo.
 * @author Robert Lichtenberger
 */
public class I18NTest {

	@Test
	public void testStringsAvailableInFallbackLocale() throws IOException {
		Set<String> i18nkeys = getI18NStringsFromSourceCode();
		Set<String> bundleKeys = getBundleKeys(I18N.FALLBACK_LOCALE);
		for (String i18nkey : i18nkeys) {
			Assertions.assertTrue(bundleKeys.contains(i18nkey), "Missing key '" + i18nkey + "' in fallback translation bundle.");
		}		
	}

	private Set<String> getI18NStringsFromSourceCode() throws IOException {
		File sourceDir = TestUtils.getSourceDirectory();
		Set<String> i18nkeys = Files.walk(sourceDir.toPath())
			.map(path -> path.toFile())
			.filter(file -> file.isFile() && file.getName().endsWith(".java"))
			.flatMap(this::getI18NStrings)
			.collect(Collectors.toSet())
		;
		return i18nkeys;
	}
	
	private Stream<String> getI18NStrings(File file){
		List<String> i18nstrings = new ArrayList<>();
		Pattern pattern = Pattern.compile("I18N.get[^\"]*\"([^\"]*)\"");
		try {
			String fileContent = Files.readString(file.toPath());
			Matcher m = pattern.matcher(fileContent);
			while (m.find()) {
				i18nstrings.add(m.group(1));
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return i18nstrings.stream();
	}
	
	private Set<String> getBundleKeys(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(I18N.RESOURCE_BUNDLE_NAME, locale);
		Set<String> bundleKeys = new HashSet<>(Collections.list(bundle.getKeys()));
		return bundleKeys;
	}
}
