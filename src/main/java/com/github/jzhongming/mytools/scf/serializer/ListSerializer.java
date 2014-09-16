package com.github.jzhongming.mytools.scf.serializer;

import java.util.List;

public class ListSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		if (null == obj) {
			SerializerFactory.GetSerializer(null).WriteObject(obj, outStream);
		}
		int typeId = TypeHelper.getTypeId(List.class);
		outStream.WriteInt32(typeId);
		if (outStream.WriteRef(obj)) {
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
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return null;
	}

}
