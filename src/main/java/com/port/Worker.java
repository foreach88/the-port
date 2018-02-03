package com.port;

import java.util.*;
import java.util.concurrent.locks.*;

class Worker implements Runnable {
	private Port port;
	private int workerNumber;
	private boolean free;
	private Thread thread;
	private Lock workerLock;

	Worker(Port port, int workerNumber) {
		this.port = port;
		this.workerNumber = workerNumber;
		this.thread = new Thread(this, String.valueOf(this.workerNumber));
		this.free = true;
		this.workerLock = new ReentrantLock();
	}

	public boolean isFree() {
		return free;
	}

	public boolean setFree(boolean status) {
		workerLock.lock();

		try {
			if(free == status)
			return false;

			free = status;
			return true;
		} finally {
			workerLock.unlock();
		}
	}

	public void run() {}

	public void carryToCraft(Craft craft, ArrayDeque<Container> containers) {
		
		free = false;
		Container currentContainer = containers.pollFirst();
		sleep(1000);
		craft.putContainer(currentContainer);
		sleep(1000);
		free = true;

		System.out.println("Загружен контейнер в " + craft);
	}

	public void carryFromCraft(Craft craft, ArrayDeque<Container> containers) {
		
		free = false;
		sleep(1000);
		Container currentContainer = craft.getContainer();
		sleep(1000);

		if(currentContainer != null) {
			containers.addFirst(currentContainer);
			System.out.println("Выгружен контейнер из " + craft);
		}
		
		free = true;
	}

	public int getWorkerNumber() {
		return workerNumber;
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			System.out.println("Поток был приостановлен.");
		}
	}
}
