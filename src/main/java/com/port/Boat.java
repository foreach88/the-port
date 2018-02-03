package com.port;

class Boat extends AbstractCraft {
	private static int MAX_CARRYING_CAPACITY = 500;

	Boat(String name) {
		super(name, MAX_CARRYING_CAPACITY);
	}
}
