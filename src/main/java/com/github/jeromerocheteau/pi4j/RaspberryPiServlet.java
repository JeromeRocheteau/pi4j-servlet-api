package com.github.jeromerocheteau.pi4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.pi4j.io.gpio.GpioController;

public abstract class RaspberryPiServlet extends HttpServlet {

	private static final long serialVersionUID = 201907120755001L;
	
	private GpioController gpioController;

	protected GpioController getGpioController() {
		return gpioController;
	}
	
	public abstract void setUp() throws Exception;
	
	public abstract void tearDown() throws Exception;
	
	public abstract void loop() throws Exception;
	
	public void init() throws ServletException {
		RaspberryPiController raspberryPiController = (RaspberryPiController) this.getServletContext().getAttribute(RaspberryPiController.NAME);
		this.gpioController = raspberryPiController.getGpioController();
		raspberryPiController.addRaspberryPiServlet(this);
	}

	public final long millis() {
		return System.currentTimeMillis();
	}
	
	protected boolean hasSpent(long duration, long timestamp) {
		return timestamp + duration < System.currentTimeMillis();
	}
	
}
