package com.port;

import java.util.*;
import java.util.concurrent.locks.*;

class Pier {
	private Port port;
	private int pierNumber;
	private volatile boolean free;
	private ArrayDeque<Container> portContainersArea = null;
	private ArrayDeque<Container> craftContainersArea = new ArrayDeque<>();
	private Lock pierLock;

	Pier(Port port, int pierNumber) {
		this.port = port;
		this.pierNumber = pierNumber;
		this.free = true;
		this.pierLock = new ReentrantLock();
	}

	public boolean isFree() {
		return free;
	}

	private void setFree(boolean status) {
		free = status;
	}

	public void report(Port port, Craft craft) {
		pierLock.lock();

		try {
			setFree(false);
			System.out.println("Судно " + craft.getName() + " зашло в порт на причал №" + pierNumber + ".");

			port.getDispatcher().serveCraft(craft, this);

			setFree(true);
			System.out.println("Судно " + craft.getName() + " покидает порт. Причал №" + pierNumber + " свободен.\n");
		} finally {
			pierLock.unlock();
		}
	}

	public int getPierNumber() {
		return pierNumber;
	}

	public void setPortContainers(ArrayDeque<Container> containers) {
		portContainersArea = containers;
	}

	public ArrayDeque<Container> getPortContainers() {
		return portContainersArea;
	}

	public void setCraftContainers(ArrayDeque<Container> containers) {
		craftContainersArea = containers;
	}

	public ArrayDeque<Container> getCraftContainers() {
		return craftContainersArea;
	}

	public boolean isPortContainersAreaEmpty() {
		return portContainersArea.isEmpty();
	}

	public int getPortContainersAmount() {
		return (portContainersArea != null) ? portContainersArea.size() : 0;
	}

}
