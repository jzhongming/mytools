package com.github.jzhongming.mytools.serializer;

/**
 * 
 * @author Zach.J
 * 
 */
class SerializerFactory {
	private final static ISerializer arraySerializer = new ArraySerializer();
	private final static ISerializer boolSerializer = new BooleanSerializer();
	private final static ISerializer byteSerializer = new ByteSerializer();
	private final static ISerializer charSerializer = new CharSerializer();
	private final static ISerializer dateTimeSerializer = new DateTimeSerializer();
	private final static ISerializer decimalSerializer = new DecimalSerializer();
	private final static ISerializer doubleSerializer = new DoubleSerializer();
	private final static ISerializer enumSerializer = new EnumSerializer();
	private final static ISerializer floatSerializer = new FloatSerializer();
	private final static ISerializer int16Serializer = new Int16Serializer();
	private final static ISerializer int32Serializer = new Int32Serializer();
	private final static ISerializer int64Serializer = new Int64Serializer();
	private final static ISerializer listSerializer = new ListSerializer();
	private final static ISerializer mapSerializer = new MapSerializer();
	private final static ISerializer nullSerializer = new NullSerializer();
	private final static ISerializer objectSerializer = new ObjectSerializer();
	private final static ISerializer setSerializer = new SetSerializer();
	private final static ISerializer stringSerializer = new StringSerializer();

	public static ISerializer GetSerializer(Class<?> type) throws ClassNotFoundException {
		if (type == null) {
			return nullSerializer;
		} else if (type.isEnum()) {
			return enumSerializer;
		}
		int typeId = TypeHelper.getTypeId(type);
		ISerializer serializer = null;
		switch (typeId) {
		case 0:
			serializer = nullSerializer;
			break;
		case 1:
			serializer = objectSerializer;
			break;
		case 2:
			serializer = byteSerializer;
			break;
		case 3:
			serializer = boolSerializer;
			break;
		case 4:
			serializer = charSerializer;
			break;
		case 5:
			serializer = int16Serializer;
			break;
		case 6:
			serializer = int32Serializer;
			break;
		case 7:
			serializer = int64Serializer;
			break;
		case 8:
			serializer = floatSerializer;
			break;
		case 9:
			serializer = doubleSerializer;
			break;
		case 10:
			serializer = decimalSerializer;
			break;
		case 11:
			serializer = arraySerializer;
			break;
		case 12:
			serializer = stringSerializer;
			break;
		case 13:
			serializer = listSerializer;
			break;
		case 14:
			serializer = mapSerializer;
			break;
		case 15:
			serializer = setSerializer;
			break;
		case 16:
			serializer = enumSerializer;
			break;
		case 17:
			serializer = dateTimeSerializer;
			break;
		case 18:
		case 19:
		case 20:
		default:
			serializer = objectSerializer;
		}
		return serializer;
	}

}
