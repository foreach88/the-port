package com.port;

class PortDemo {
	public static void main(String[] args) {
		Port port = new Port(700);
		port.setPierAmount(3);
		port.setContainerAmount(100);
		port.setWorkerAmount(3);
		port.run();

		Boat boat1 = new Boat("Малыш");
		boat1.start(port, 3000);

		Boat boat2 = new Boat("Бригантина");
		boat2.start(port, 3000);

		Boat boat3 = new Boat("Дельфин");
		boat3.start(port, 3000);

		Ship ship1 = new Ship("Сухогруз");
		ship1.start(port, 10000);

		Ship ship2 = new Ship("Юпитер");
		ship2.start(port, 10000);
		
		
		boat1.join();
		boat2.join();
		boat3.join();
		ship1.join();
		ship2.join();

		System.out.println("Главный поток завершен.");
	}

	
}
