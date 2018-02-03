package com.port;

import java.util.*;

class Dispatcher {
	private Port port;

	Dispatcher(Port port) {
		this.port = port;
	}

	public void serveCraft(Craft craft, Pier pier) {
		Date start = new Date();

		pier.setPortContainers(deliverContainersToPier(determineContainerAmount(craft)));
		System.out.println("Контейнеры были доставлены на причал №" + pier.getPierNumber() + " и готовы к погрузке в " + craft.getName() + ".");
		
		while(!craft.isEmpty()) {

			int rest = craft.getCraftCongestion();

			ArrayList<Worker> freeWorkers = port.getFreeWorkers();

			if(freeWorkers.size() == 0) {
				synchronized(this) {
					try {
						wait();
						continue;
					} catch(InterruptedException e) {
						System.out.println("Поток прерван");
					}
				}
			}

			for(Worker worker : freeWorkers) {
				if(!worker.setFree(false))
					continue;

				new Thread(() -> {
					worker.carryFromCraft(craft, pier.getCraftContainers());

					synchronized(this) {
						this.notifyAll();
					}
				}).start();
					
				if(rest-- == 0)
					break;
				
			}
		}




		while(!pier.isPortContainersAreaEmpty()) {

			int rest = pier.getPortContainersAmount();

			ArrayList<Worker> freeWorkers = port.getFreeWorkers();

			if(freeWorkers.size() == 0) {
				synchronized(this) {
					try {
						wait();
						continue;
					} catch(InterruptedException e) {
						System.out.println("Поток прерван");
					}
				}
			}

			for(Worker worker : freeWorkers) {
				if(!worker.setFree(false))
					continue;

				new Thread(() -> {
					worker.carryToCraft(craft, pier.getPortContainers());

					synchronized(this) {
						this.notifyAll();
					}
				}).start();
					
				if(rest-- == 0)
					break;
				
			}
		}

		deliverContainersToPort(pier.getCraftContainers());
		System.out.println("Контейнеры, разгруженные из " + craft.getName() + " были доставлены в портовый склад с причала №" + pier.getPierNumber() + ".");
		
		System.out.printf("Судно %s было обслужено за %d секунд.%n", craft.getName(), getTimeDifference(start)/1000);
	}

	private int determineContainerAmount(Craft craft) {

		int containerAmount = 0;

		if(craft.getMaxCarryingCapacity() > Container.CONTAINER_WEIGHT) {
			int halfAnMaxCarryingCapacity = craft.getMaxCarryingCapacity() / 2;
			int randomAmount = (int) (Math.random() * halfAnMaxCarryingCapacity + 1);
			return (halfAnMaxCarryingCapacity + randomAmount) / Container.CONTAINER_WEIGHT;
		} else {
			return 1;
		}
	}

	private synchronized ArrayDeque<Container> deliverContainersToPier(int amount) {
		ArrayDeque<Container> containers = new ArrayDeque<>();

		for(int i = 0; i < amount; i++) {
			Container container = port.getContainer();

			if(container == null) {
				System.out.println("! В порту закончились контейнеры и судно не сможет быть загружено полностью.");
				break;
			}

			containers.add(container);
		}

		return containers;
	}

	private synchronized void deliverContainersToPort(ArrayDeque<Container> containers) {

		for(int i = 0; i < containers.size(); i++) {
			port.putContainer(containers.pollFirst());
		}
	}

	private long getTimeDifference(Date start) {
		return new Date().getTime() - start.getTime();
	}

}
