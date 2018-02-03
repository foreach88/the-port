package com.port;

interface Craft {
	public void start(Port port, long time);
	public Container getContainer();
	public void putContainer(Container container);
	public int getMaxCarryingCapacity();
	public String getName();
	public boolean isEmpty();
	public int getCraftCongestion();
}
