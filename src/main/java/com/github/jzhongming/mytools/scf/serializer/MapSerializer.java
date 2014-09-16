package com.github.jzhongming.mytools.scf.serializer;

import java.util.Iterator;
import java.util.Map;

public class MapSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		if (null == obj) {
			SerializerFactory.GetSerializer(null).WriteObject(obj, outStream);
		}
		int typeId = TypeHelper.getTypeId(Map.class);
		outStream.WriteInt32(typeId);
		if (outStream.WriteRef(obj)) {
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
			int valueTypeId = TypeHelper.getTypeId(valueType);
			outStream.WriteInt32(valueTypeId);
			SerializerFactory.GetSerializer(valueType).WriteObject(entry.getValue(), outStream);
		}
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		if (typeId == 0) {
			return null;
		}
		return null;
	}

}
