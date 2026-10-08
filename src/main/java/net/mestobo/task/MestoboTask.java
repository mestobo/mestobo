package net.mestobo.task;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public abstract class MestoboTask<V> extends Task<V> {

    private final ObjectProperty<LocalDateTime> started = new SimpleObjectProperty<>(LocalDateTime.now());
    private final ObjectProperty<LocalDateTime> modified = new SimpleObjectProperty<>(LocalDateTime.now());
    
    public MestoboTask(String title) {
    	super();
    	updateTitle(title);
	}
    public final LocalDateTime getStarted() { 
    	return started.get(); 
    }
    
    public final ReadOnlyObjectProperty<LocalDateTime> startedProperty() { 
    	return started; 
    }
    
    public final LocalDateTime getModified() { 
    	return modified.get(); 
    }
    
    public final ReadOnlyObjectProperty<LocalDateTime> modifiedProperty() { 
    	return modified; 
    }
    
    @Override
    protected void updateMessage(String message) {
    	super.updateMessage(message);
    	updateModified();
    }
    
    public void updateModified() {
    	modified.set(LocalDateTime.now());
    }

}
