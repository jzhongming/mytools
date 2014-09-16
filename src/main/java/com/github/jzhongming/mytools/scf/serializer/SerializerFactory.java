package com.github.jzhongming.mytools.scf.serializer;

class SerializerFactory {
	private final static SerializerBase arraySerializer = new ArraySerializer();
	private final static SerializerBase boolSerializer = new BooleanSerializer();
	private final static SerializerBase byteSerializer = new ByteSerializer();
	private final static SerializerBase charSerializer = new CharSerializer();
	private final static SerializerBase dateTimeSerializer = new DateTimeSerializer();
	private final static SerializerBase decimalSerializer = new DecimalSerializer();
	private final static SerializerBase doubleSerializer = new DoubleSerializer();
	private final static SerializerBase enumSerializer = new EnumSerializer();
	private final static SerializerBase floatSerializer = new FloatSerializer();
	private final static SerializerBase int16Serializer = new Int16Serializer();
	private final static SerializerBase int32Serializer = new Int32Serializer();
	private final static SerializerBase int64Serializer = new Int64Serializer();
	private final static SerializerBase listSerializer = new ListSerializer();
	private final static SerializerBase mapSerializer = new MapSerializer();
	private final static SerializerBase nullSerializer = new NullSerializer();
	private final static SerializerBase objectSerializer = new ObjectSerializer();
	private final static SerializerBase setSerializer = new SetSerializer();
	private final static SerializerBase stringSerializer = new StringSerializer();

	public static SerializerBase GetSerializer(Class<?> type) throws ClassNotFoundException {
		if (type == null) {
			return nullSerializer;
		} else if (type.isEnum()) {
			return enumSerializer;
		}
		int typeId = TypeHelper.getTypeId(type);
		SerializerBase serializer = null;
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
