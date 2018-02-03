package com.port;

import java.util.*;
import java.util.concurrent.locks.*;

class Port {
	private int maxCapacity;
	private ArrayDeque<Container> containers = new ArrayDeque<>();
	private ArrayList<Pier> piers = new ArrayList<>();
	private ArrayList<Worker> workers = new ArrayList<>();
	private boolean run;
	private Dispatcher dispatcher;
	private Lock portLock;

	Port(int maxCapacity) {
		if(maxCapacity < 0) {
			throw new IllegalArgumentException("There is no legal value for maxCapacity.");
		}

		this.maxCapacity = maxCapacity;
		this.dispatcher = new Dispatcher(this);
		this.portLock = new ReentrantLock();
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setPierAmount(int pierAmount) {
		if(pierAmount <= 0) {
			throw new IllegalArgumentException("Value of pierAmount mustn't be negative.");
		}

		for(int i = 0; i < pierAmount; i++) {
			piers.add(new Pier(this, i+1));
		}
	}

	public void setContainerAmount(int defaultContainerAmount) {
		if(defaultContainerAmount < 0) {
			throw new IllegalArgumentException("Value of defaultContainerAmount mustn't be negative.");
		}

		for(int i = 0; i < defaultContainerAmount; i++) {
			containers.add(new Container());
		}
	}

	public void setWorkerAmount(int defaultWorkerAmount) {
		if(defaultWorkerAmount <= 0) {
			throw new IllegalArgumentException("Value of defaultWorkerAmount mustn't be negative.");
		}

		for(int i = 0; i < defaultWorkerAmount; i++) {
			workers.add(new Worker(this, i+1));
		}
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void run() {
		if(piers.size() == 0)
			setPierAmount(1);

		if(workers.size() == 0)
			setWorkerAmount(1);

		this.run = true;
	}

	public boolean isRun() {
		return run;
	}

	public Pier getPier() throws Exception {
		if(!isRun()) {
			throw new Exception("The port has not opened yet.");
		}

		portLock.lock();

		try {
			Pier pier = getFreePier();

			if(pier == null)
				pier = getRandomPier();

			return pier;
		} finally {
			portLock.unlock();
		}
	}

	private Pier getFreePier() {
		for(Pier pier : piers) {
			if(pier.isFree())
				return pier;
		}

		return null;
	}

	private Pier getRandomPier() {
		return piers.get((int)(Math.random() * piers.size()));
	}

	public Container getContainer() {
		if(containers.size() == 0) {
			return null;
		}

		return containers.pollFirst();
	}

	public void putContainer(Container container) {
		containers.addFirst(container);
	}

	public Worker getFreeWorker() {

		for(Worker worker : workers) {
			if(worker.isFree())
				return worker;
		}

		return null;
	}

	public ArrayList<Worker> getFreeWorkers() {

		ArrayList<Worker> freeWorkes = new ArrayList<Worker>();

		for (Worker worker : workers) {
			if(worker.isFree())
				freeWorkes.add(worker);
		}

		return freeWorkes;
	}
}
