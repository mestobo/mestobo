package net.mestobo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.inject.Singleton;

@Singleton
public class BackgroundTaskExecutor extends ThreadPoolExecutor {
	
	public BackgroundTaskExecutor() {
		super(1, 128, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
	}	
}
