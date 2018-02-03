package com.port;

class Ship extends AbstractCraft {
	private static int MAX_CARRYING_CAPACITY = 20000;

	Ship(String name) {
		super(name, MAX_CARRYING_CAPACITY);
	}
}
