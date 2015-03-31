package com.github.jzhongming.mytools.serializer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.jzhongming.mytools.scanner.DefaultClassScanner;
import com.github.jzhongming.mytools.serializer.annotation.CCSerializable;

import org.apache.commons.lang.IllegalClassException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DefaultSerializerEngine implements ISerializerEngine {
	private static final Logger logger = LoggerFactory.getLogger(DefaultSerializerEngine.class);
	private final ConcurrentHashMap<String, String> clash = new ConcurrentHashMap<String, String>();

	private void initEngine() {
		logger.info("init Serializer engine ...");
		long start = System.currentTimeMillis();
		lodingLocalePackage();
		logger.info("Serializer init cost time: {} ms", (System.currentTimeMillis() - start));
	}

	private void lodingLocalePackage() {
		final Set<Class<?>> classes = DefaultClassScanner.getInstance().getClassListByAnnotation("com.github.jzhongming", CCSerializable.class);
		for (Class<?> c : classes) {
			logger.info("scaning {}", c.getName());
			try {
				CCSerializable ann = c.getAnnotation(CCSerializable.class);
				if (null == ann) {
					continue;
				}
				String name = ann.name();
				if (name.isEmpty()) {
					name = c.getSimpleName();
				}
				if (null != clash.putIfAbsent(name, "")) {
					throw new IllegalClassException("class @CCSerializable( name= " + name + ") is clash");
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
