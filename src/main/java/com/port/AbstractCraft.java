package com.port;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

abstract class AbstractCraft implements Craft, Runnable {
	private String name;
	private Container[] containers;
	private int maxCarryingCapacity;
	private Port port;
	private long time;
	private Lock craftLock;
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	private Future<?> threadTask;

	AbstractCraft(String name, int maxCarryingCapacity) {
		if(maxCarryingCapacity < Container.CONTAINER_WEIGHT) {
			System.out.println("Illegal maxCarryingCapacity for boat.");
			System.exit(0);
		}

		this.name = name;
		this.maxCarryingCapacity = maxCarryingCapacity;
		this.craftLock = new ReentrantLock();
		containers = new Container[maxCarryingCapacity / Container.CONTAINER_WEIGHT];
	}

	public void start(Port port, long time) {
		this.port = port;
		this.time = time;

		this.threadTask = threadPool.submit(this);
	}

	public void run() {
		while(true) {
			goToThePort(port);
			sleep(time);
		}
	}

	public void join() {
		try {
			threadTask.get();
		} catch(InterruptedException | ExecutionException e) {
			System.out.println("Поток прерван.");
		}
		
	}

	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			System.out.println("Поток был приостановлен.");
		}
	}

	protected void goToThePort(Port port) {
		Pier pier = null;

		try {
			pier = port.getPier();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		if(pier != null)
			pier.report(port, this);
	}

	public Container getContainer() {
		craftLock.lock();

		try {
			Container container = null;

			for(int i = 0; i < containers.length; i++) {
				Container currentPosition = containers[i];

				if(currentPosition != null) {
					container = currentPosition;
					containers[i] = null;
					break;
				}
			}

			return container;
		} finally {
			craftLock.unlock();
		}
	}

	public void putContainer(Container container) {
		craftLock.lock();

		try {
			for(int i = 0; i < containers.length; i++) {

				Container currentPosition = containers[i];

				if(currentPosition == null) {
					containers[i] = container;
					break;
				}
			}
		} finally {
			craftLock.unlock();
		}
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name + "(" + getCraftCongestion() + "/" + getMaxCarryingCapacity()/Container.CONTAINER_WEIGHT + ")";
	}

	public int getMaxCarryingCapacity() {
		return maxCarryingCapacity;
	}

	public int getCraftCongestion() {
		int existingContainers = 0;

		for(Container currentPosition : containers) {
			if(currentPosition != null)
				existingContainers++;
		}

		return existingContainers;
	}

	public boolean isEmpty() {
		return getCraftCongestion() == 0;
	}
}
