package net.mestobo;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.util.TranslationService;

/** I18N encapsulates internationalization.
 * @author Robert Lichtenberger
 */
public class I18N {
	
	public static final String RESOURCE_BUNDLE_NAME = "net.mestobo.mestobo";
	public static final Locale FALLBACK_LOCALE = Locale.ENGLISH;
	
	public static String get(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			System.err.println("Missing / wrong i18n key: " + key);
			return key;
		}
	}	
	
	public static TranslationService getTranslationService() {
		return new ResourceBundleService(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME));
	}
}
