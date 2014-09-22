package com.github.jzhongming.mytools.serializer;

public class EnumSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		int typeId = TypeHelper.getTypeId(obj.getClass());
		outStream.WriteInt32(typeId);
		String value = obj.toString();
		SerializerFactory.GetSerializer(String.class).WriteObject(value, outStream);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		Class type = TypeHelper.getIdType(typeId);
		if (type == null) {
			throw new ClassNotFoundException("Can not find class with typId,target class:" + clazz.getName() + ",typeId:" + typeId);
		}
		String value = (String) SerializerFactory.GetSerializer(String.class).ReadObject(inStream, clazz);
		return Enum.valueOf(type, value);
	}

}
