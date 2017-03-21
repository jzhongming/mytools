package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class SetSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		if (obj == null) {
			SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
		}
		Set<?> set = (Set<?>) obj;
		int typeId = TypeHelper.getTypeId(Set.class);
		outStream.WriteInt32(typeId);
		if (outStream.isRefWrited(obj)) {
			return;
		}
		outStream.WriteInt32(set.size());
		for (Object item : set) {
			if (item == null) {
				SerializerFactory.GetSerializer(null).WriteObject(item, outStream);
			} else {
				Class<?> itemType = item.getClass();
				outStream.WriteInt32(TypeHelper.getTypeId(itemType));
				SerializerFactory.GetSerializer(itemType).WriteObject(item, outStream);
			}
		}
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		if (typeId == 0) {
			return null;
		}

		byte isRef = (byte) inStream.read();
		int refId = inStream.ReadInt32();
		if (isRef > 0) {
			return inStream.GetRef(refId);
		}
		Class<?> type = TypeHelper.getIdType(typeId);
		if (type == null) {
			throw new ClassNotFoundException("Cannot find class with typId,target class:" + clazz.getName() + ",typeId:" + typeId);
		}
		if (type != Set.class) {
			throw new ClassNotFoundException("Class must be set!type:" + type.getName());
		}
		int modifier = clazz.getModifiers();
		if (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier) && Set.class.isAssignableFrom(clazz)) {
			type = clazz;
		} else {
			type = HashSet.class; // default set type
			if (!clazz.isAssignableFrom(type)) {
				throw new ClassNotFoundException("Defind type and value type not match !defind type:" + clazz.getName() + ",value type:" + type.getName());
			}
		}
		int len = inStream.ReadInt32();
		if (len > TypeHelper.MAX_DATA_LEN) {
			throw new IllegalArgumentException("Data length overflow.");
		}
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) type.newInstance();
		for (int i = 0; i < len; i++) {
			int itemTypeId = inStream.ReadInt32();
			if (itemTypeId == 0) {
				set.add(null);
			} else {
				Class<?> itemType = TypeHelper.getIdType(itemTypeId);
				if (itemType == null) {
					throw new ClassNotFoundException("Cannot find class with typId,target class:(list[item])" + ",typeId:" + itemTypeId);
				}
				Object value = SerializerFactory.GetSerializer(itemType).ReadObject(inStream, itemType);
				set.add(value);
			}
		}
		inStream.SetRef(refId, set);
		return set;
	}

}
