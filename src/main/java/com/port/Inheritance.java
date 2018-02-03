package com.port;

class Inheritance {
	private String name;

	Inheritance(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		InheritanceNew in = new InheritanceNew("test");
		System.out.println(in.getName());
	}
}

class InheritanceNew extends Inheritance {
	private String name;

	InheritanceNew(String name) {
		super(name);
	}
	/*
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	*/

}
