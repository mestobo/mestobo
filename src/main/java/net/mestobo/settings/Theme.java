package net.mestobo.settings;

public class Theme {

	private String name;
	private String stylesheet;

	public Theme(String name, String stylesheet) {
		this.name = name;
		this.stylesheet = stylesheet == null ? "" : stylesheet;
	}

	public String getStylesheet() {
		return stylesheet;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (obj == this) {
			result = true;
		} else if (obj == null || getClass() != obj.getClass()) {
			result = false;
		} else {
			Theme theme = (Theme)obj;
			result = name == null ? theme.name == null : name.equals(theme.name);
			result = result && (stylesheet == null ? theme.stylesheet == null : stylesheet.equals(theme.stylesheet));
		}
		return result;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (name == null ? 0 : name.hashCode());
		hash = 31 * hash + (stylesheet == null ? 0 : stylesheet.hashCode());
		return hash;
	}
}
