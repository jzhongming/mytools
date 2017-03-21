package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

class TypeInfo {

	private final int TypeId;
	private final Set<Field> Fields;

	public TypeInfo(int typeId) {
		TypeId = typeId;
		Fields = new TreeSet<Field>(new SortComp());
	}

	public int getTypeId() {
		return TypeId;
	}

	public Set<Field> getFields() {
		return Fields;
	}

	@Override
	public String toString() {
		return "TypeInfo [TypeId=" + TypeId + ", Fields=" + Fields + "]";
	}

	private static final class SortComp implements Comparator<Field> {
		@Override
		public int compare(Field o1, Field o2) {
			return TypeHelper.makeTypeId(o1.getName()) - TypeHelper.makeTypeId(o2.getName());
		}
	}
}