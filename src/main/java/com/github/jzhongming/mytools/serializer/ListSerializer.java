package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ListSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		if (null == obj) {
			SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			return;
		}
		int typeId = TypeHelper.getTypeId(List.class);
		outStream.WriteInt32(typeId);
		if (outStream.isRefWrited(obj)) {
			return;
		}
		List<?> list = (List<?>) obj;
		outStream.WriteInt32(list.size());
		for (Object item : list) {
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
		if (type != List.class) {
			throw new ClassNotFoundException("Class must be list!type:" + type.getName());
		}
		int modifier = clazz.getModifiers();
		if (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier) && List.class.isAssignableFrom(clazz)) {
			type = clazz;
		} else {
			type = ArrayList.class; // default list type
			if (!clazz.isAssignableFrom(type)) {
				throw new ClassNotFoundException("Defind type and value type not match !defind type:" + clazz.getName() + ",value type:" + type.getName());
			}
		}
		int len = inStream.ReadInt32();
		if (len > TypeHelper.MAX_DATA_LEN) {
			throw new IllegalArgumentException("Data length overflow.");
		}
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) type.newInstance();
		for (int i = 0; i < len; i++) {
			int itemTypeId = inStream.ReadInt32();
			if (itemTypeId == 0) {
				list.add(null);
			} else {
				Class<?> itemType = TypeHelper.getIdType(itemTypeId);
				if (itemType == null) {
					throw new ClassNotFoundException("Cannot find class with typId,target class:(list[item])" + ",typeId:" + itemTypeId);
				}
				Object value = SerializerFactory.GetSerializer(itemType).ReadObject(inStream, itemType);
				list.add(value);
			}
		}
		inStream.SetRef(refId, list);
		return list;
	}

}
