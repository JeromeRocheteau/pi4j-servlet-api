package com.github.jeromerocheteau.pi4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class RaspberryPiController implements ServletContextListener {

	public static final String NAME = RaspberryPiController.class.getCanonicalName();
	
	private ExecutorService executorService;
	
	private GpioController gpioController;
	
	public GpioController getGpioController() {
		return gpioController;
	}
	
	public void addRaspberryPiServlet(RaspberryPiServlet servlet) {
		this.executorService.execute(new RaspberryPiThread(servlet));
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.gpioController = GpioFactory.getInstance();
		this.executorService = Executors.newSingleThreadExecutor(); 
		sce.getServletContext().setAttribute(RaspberryPiController.NAME, this);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.executorService.shutdownNow();
		this.gpioController.shutdown();
	}

	private class RaspberryPiThread implements Runnable {
	
		private RaspberryPiServlet servlet;
		
		private RaspberryPiThread(RaspberryPiServlet servlet) {
			this.servlet = servlet;
		}
		
		@Override
		public final void run() {
			try {
				servlet.setUp();
				while (Thread.currentThread().isInterrupted() == false) {
					servlet.loop();
				}
				servlet.tearDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	
}
