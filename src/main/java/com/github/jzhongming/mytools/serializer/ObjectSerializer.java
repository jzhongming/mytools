package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectSerializer implements ISerializer {
	private static final Logger logger = LoggerFactory.getLogger(ObjectSerializer.class);

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		if (obj == null) {
			SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			return;
		}
		Class<?> type = obj.getClass();
		TypeInfo typeInfo = TypeHelper.getTypeInfo(type);
		outStream.WriteInt32(typeInfo.getTypeId());
		if (outStream.isRefWrited(obj)) {
			return;
		}
		for (Field f : typeInfo.getFields()) {
			Object value = f.get(obj);
			if (value == null) {
				SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			} else {
				Class<?> valueType = value.getClass();
				outStream.WriteInt32(TypeHelper.getTypeId(valueType));
				SerializerFactory.GetSerializer(valueType).WriteObject(value, outStream);
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
		if (!clazz.isAssignableFrom(type) && clazz != type) {
			logger.warn("match {} : {}", type.getSimpleName(), clazz.getSimpleName());
			throw new ClassNotFoundException("Class not match!class: " + type.getName() + " ==> " + clazz.getName());
		}

		byte isRef = (byte) inStream.read();
		int refId = inStream.ReadInt32();
		if (isRef > 0) {
			return inStream.GetRef(refId);
		}
		Object obj = type.newInstance();
		TypeInfo typeInfo = TypeHelper.getTypeInfo(type);
		for (Field f : typeInfo.getFields()) {
			int ptypeId = inStream.ReadInt32();
			if (ptypeId == 0) {
				f.set(obj, null);
				continue;
			}
			Class<?> ptype = TypeHelper.getIdType(ptypeId);
			if (ptype == null) {
				throw new ClassNotFoundException("Cannot find class with typId,target class: " + f.getType().getName() + ",typeId:" + ptypeId);
			}
			Object value = SerializerFactory.GetSerializer(ptype).ReadObject(inStream, f.getType());
			f.set(obj, value);
		}
		inStream.SetRef(refId, obj);
		return obj;
	}

}
