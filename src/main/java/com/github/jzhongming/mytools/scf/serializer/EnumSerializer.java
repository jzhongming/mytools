package com.github.jzhongming.mytools.scf.serializer;

public class EnumSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		int typeId = TypeHelper.getTypeId(obj.getClass());
		outStream.WriteInt32(typeId);
		String value = obj.toString();
		SerializerFactory.GetSerializer(String.class).WriteObject(value, outStream);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		Class type = TypeHelper.getType(typeId);
		if (type == null) {
			throw new ClassNotFoundException("Cannot find class with typId,target class:" + clazz.getName() + ",typeId:" + typeId);
		}
		String value = (String) SerializerFactory.GetSerializer(String.class).ReadObject(inStream, clazz);
		return Enum.valueOf(type, value);
	}

}
