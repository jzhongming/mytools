package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		if (null == obj) {
			SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			return;
		}
		int typeId = TypeHelper.getTypeId(Map.class);
		outStream.WriteInt32(typeId);
		if (outStream.isRefWrited(obj)) { // set isRef and Set RefId
			return;
		}
		Map<?, ?> map = (Map<?, ?>) obj;
		outStream.WriteInt32(map.size());
		Iterator<?> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();
			Class<?> keyType = entry.getKey().getClass();
			int keyTypeId = TypeHelper.getTypeId(keyType);
			outStream.WriteInt32(keyTypeId);
			SerializerFactory.GetSerializer(keyType).WriteObject(entry.getKey(), outStream);

			Class<?> valueType = entry.getValue().getClass();
			if (valueType == null) {
				SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			} else {
				int valueTypeId = TypeHelper.getTypeId(valueType);
				outStream.WriteInt32(valueTypeId);
				SerializerFactory.GetSerializer(valueType).WriteObject(entry.getValue(), outStream);
			}
		}
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		if (typeId == 0) {
			return null;
		}

		Class<?> type = TypeHelper.getIdType(typeId);
		if (type == null) {
			throw new ClassNotFoundException("Cannot find class with typId,target class:" + clazz.getName() + ",typeId:" + typeId);
		}
		if (type != Map.class) {
			throw new ClassNotFoundException("Class must be map!type:" + type.getName());
		}

		int modifier = clazz.getModifiers();
		if (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier) && Map.class.isAssignableFrom(clazz)) {
			type = clazz;
		} else {
			type = HashMap.class; // default map type
			if (!clazz.isAssignableFrom(type)) {
				throw new ClassNotFoundException("Defind type and value type not match !defind type:" + clazz.getName() + ",value type:" + type.getName());
			}
		}
		byte isRef = (byte) inStream.read(); // get isRef
		int refId = inStream.ReadInt32(); // get refId
		if (isRef > 0) {
			return inStream.GetRef(refId);
		}
		int len = inStream.ReadInt32();
		if (len > TypeHelper.MAX_DATA_LEN) {
			throw new IllegalArgumentException("Data length overflow.");
		}
		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) type.newInstance();
		for (int i = 0; i < len; i++) {
			int keyTypeId = inStream.ReadInt32();
			Class<?> keyType = TypeHelper.getIdType(keyTypeId);
			if (keyType == null) {
				throw new ClassNotFoundException("Cannot find class with typId,target class:map[key]" + ",typeId:" + keyTypeId);
			}
			Object key = SerializerFactory.GetSerializer(keyType).ReadObject(inStream, keyType);
			int valueTypeId = inStream.ReadInt32();
			Class<?> valueType = TypeHelper.getIdType(valueTypeId);
			Object value = null;
			if (valueType != null) {
				value = SerializerFactory.GetSerializer(valueType).ReadObject(inStream, valueType);
			}
			map.put(key, value);
		}
		inStream.SetRef(refId, map);
		return map;
	}

}
