package com.github.jzhongming.mytools.scf.serializer;

public class CharSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		byte[] bs = ByteHelper.GetBytesFromChar((Character) obj);
		outStream.write(bs);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		short data = inStream.ReadInt16();
		byte[] buffer = ByteHelper.GetBytesFromInt16(data);
		return ByteHelper.getCharFromBytes(buffer);
	}

	public static void main(String[] args) throws Exception {
		Character c = 'D';
		CharSerializer cs = new CharSerializer();
		SCFOutStream scfO = new SCFOutStream();
		cs.WriteObject(c, scfO);
		SCFInStream scfI = new SCFInStream(scfO.toByteArray());
		System.out.println(cs.ReadObject(scfI, Character.class));
	}

}
