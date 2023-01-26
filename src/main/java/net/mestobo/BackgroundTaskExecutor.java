package net.mestobo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.inject.Singleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

@Singleton
public class BackgroundTaskExecutor extends ThreadPoolExecutor {
	
	private ObservableList<Task<?>> tasks = FXCollections.observableArrayList();
	
	public BackgroundTaskExecutor() {
		super(1, 128, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
	}	
	
	public void submitTask(Task<?> task) {
		tasks.add(task);
		submit(task);
	}
	
	public ObservableList<Task<?>> getTasks() {
		return tasks;
	}
}
