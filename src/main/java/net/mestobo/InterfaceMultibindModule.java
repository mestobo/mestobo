package net.mestobo;

import java.io.IOException;
import java.lang.reflect.Modifier;

import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/** InterfaceMultibindModule allows to bind all implementers of a defined interface. */
public class InterfaceMultibindModule extends AbstractModule {
	
	public <I> InterfaceMultibinder<I> multibind(Class<I> iface) {
		return new InterfaceMultibinder<>(iface);
	}
	
	class InterfaceMultibinder<I> {
        
		private Multibinder<I> multibinder;
        private Class<I> iface;
        
		public InterfaceMultibinder(Class<I> iface) {
            this.iface = iface;
            multibinder = Multibinder.newSetBinder(binder(), iface);			
		}
		
		@SuppressWarnings("unchecked")
		public Multibinder<I> toAllImplementers() {
			try {
				ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClasses().stream()
					.filter(clazzInfo -> clazzInfo.getPackageName().startsWith("net.mestobo"))
					.map(clazzInfo -> clazzInfo.load())
					.filter(clazz -> !clazz.isInterface())
					.filter(clazz -> iface.isAssignableFrom(clazz))
					.filter(clazz -> ! Modifier.isAbstract(clazz.getModifiers()))
					.forEach(clazz -> multibinder.addBinding().to((Class<I>) clazz)
				);
				;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}			
			return multibinder;
		}		
	}
}
