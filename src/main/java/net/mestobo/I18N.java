package net.mestobo;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** I18N encapsulates internationalization.
 * @author Robert Lichtenberger
 */
public class I18N {
	
	public static final String RESOURCE_BUNDLE_NAME = "net.mestobo.mestobo";
	public static final Locale FALLBACK_LOCALE = Locale.ENGLISH;
	
	public static String get(String key, String ... arguments) {
		ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
		try {
			return MessageFormat.format(bundle.getString(key), arguments);
		} catch (MissingResourceException e) {
			System.err.println("Missing / wrong i18n key: " + key);
			return key;
		}
	}		
}
