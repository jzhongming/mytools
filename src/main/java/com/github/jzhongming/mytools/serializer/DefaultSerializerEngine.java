package com.github.jzhongming.mytools.serializer;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jzhongming.mytools.scanner.ClassScanner;
import com.github.jzhongming.mytools.scanner.DefaultClassScanner;
import com.github.jzhongming.mytools.serializer.annotation.CCSerializable;

public final class DefaultSerializerEngine implements ISerializerEngine {
	private static final Logger logger = LoggerFactory.getLogger(DefaultSerializerEngine.class);

	private void initEngine() {
		logger.info("init Serializer engine ...");
		long start = System.currentTimeMillis();
		String mode = System.getProperty("Serializerengine.Mode");
		if (mode != null && mode.equals("asyn")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					lodingLocalePackage();
				}
			}).start();
		} else {
			lodingLocalePackage();
		}
		logger.info("Serializer init cost time: {} ms", (System.currentTimeMillis() - start));
	}

	private void lodingLocalePackage() {
		ClassScanner csc = new DefaultClassScanner();
		final Set<Class<?>> classes = csc.getClassListByAnnotation("com.github.jzhongming", CCSerializable.class);
		for (Class<?> c : classes) {
			logger.info("scaning {}.{}", c.getPackage().getName(), c.getName());
			try {
				CCSerializable ann = c.getAnnotation(CCSerializable.class);
				if (null == ann) {
					continue;
				}
				String name = ann.name();
				if (name.isEmpty()) {
					name = c.getSimpleName();
				}
				int typeId = TypeHelper.makeTypeId(name);
				TypeHelper.setTypeMap(c, typeId);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public DefaultSerializerEngine() {
		initEngine();
	}

	@Override
	public byte[] Serialize(Object obj) throws Exception {
		CCOutStream stream = new CCOutStream();
		try {
			if (obj == null) {
				SerializerFactory.GetSerializer(null).WriteObject(null, stream);
			} else {
				Class<?> type = obj.getClass();
				SerializerFactory.GetSerializer(type).WriteObject(obj, stream);
			}
			return stream.toByteArray();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	@Override
	public Object Derialize(byte[] buffer, Class<?> type) throws Exception {
		CCInStream stream = new CCInStream(buffer);
		try {
			return SerializerFactory.GetSerializer(type).ReadObject(stream, type);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

}
