package com.github.jzhongming.mytools.scf.serializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		if (obj == null) {
			SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			return;
		}
		Class<?> type = obj.getClass();
		int typeId = TypeHelper.getTypeId(type);
		outStream.WriteInt32(typeId);
		if (outStream.WriteRef(obj)) {
			return;
		}
		for (Field f : getFileds(type)) {
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
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		if (typeId == 0) {
			return null;
		}

		Class<?> type = TypeHelper.getType(typeId);
		if (type == null) {
			throw new ClassNotFoundException("Cannot find class with typId,target class:" + clazz.getName() + ",typeId:" + typeId);
		}
		if (!clazz.isAssignableFrom(type) && clazz != type) {
			throw new ClassNotFoundException("Class not match!class:" + type.getName() + ",require " + clazz.getName());
		}

		byte isRef = (byte) inStream.read();
		int hashcode = inStream.ReadInt32();
		if (isRef > 0) {
			return inStream.GetRef(hashcode);
		}
		Object obj = type.newInstance();
		for (Field f : getFileds(clazz)) {
			int ptypeId = inStream.ReadInt32();
			if (ptypeId == 0) {
				f.set(obj, null);
				continue;
			}
			Class<?> ptype = TypeHelper.getType(ptypeId);
			if (ptype == null) {
				throw new ClassNotFoundException("Cannot find class with typId,target class: " + f.getType().getName() + ",typeId:" + ptypeId);
			}
			Object value = SerializerFactory.GetSerializer(ptype).ReadObject(inStream, f.getType());
			f.set(obj, value);
		}
		inStream.SetRef(hashcode, obj);
		return obj;
	}
	
	private List<Field> getFileds(Class<?> temType) {
		List<Field> fields = new ArrayList<Field>();
		while (true) {
            Field[] fs = temType.getDeclaredFields();
            for (Field f : fs) {
                fields.add(f);
            }
            Class<?> superClass = temType.getSuperclass();
            if (superClass == null) {
                break;
            }
            temType = superClass;
        }
		return fields;
	}

}
