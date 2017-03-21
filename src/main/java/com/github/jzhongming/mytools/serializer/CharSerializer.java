package com.github.jzhongming.mytools.serializer;

public class CharSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		byte[] bs = ByteHelper.GetBytesFromChar((Character) obj);
		outStream.write(bs);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		short data = inStream.ReadInt16();
		byte[] buffer = ByteHelper.GetBytesFromInt16(data);
		return ByteHelper.getCharFromBytes(buffer);
	}

	public static void main(String[] args) throws Exception {
		Character c = 'D';
		CharSerializer cs = new CharSerializer();
		CCOutStream scfO = new CCOutStream();
		cs.WriteObject(c, scfO);
		CCInStream scfI = new CCInStream(scfO.toByteArray());
		System.out.println(cs.ReadObject(scfI, Character.class));
	}

}
