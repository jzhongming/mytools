package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class TypeInfo {

	private final int TypeId;
	private final List<Field> Fields;

	public TypeInfo(int typeId) {
		TypeId = typeId;
		Fields = new ArrayList<Field>();
	}

	public int getTypeId() {
		return TypeId;
	}

	public List<Field> getFields() {
		return Fields;
	}

	@Override
	public String toString() {
		return "TypeInfo [TypeId=" + TypeId + ", Fields=" + Fields + "]";
	}

}