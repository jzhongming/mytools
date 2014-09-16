package com.github.jzhongming.mytools.scf.serializer;

public class ClassItem {

	private Class<?>[] Types;
	private int TypeId;

	public ClassItem(int typeids, Class<?>... types) {
		Types = types;
		TypeId = typeids;
	}

	public Class<?> getType() {
		return Types[0];
	}

	public Class<?>[] getTypes() {
		return Types;
	}

	public int getTypeId() {
		return TypeId;
	}
}
