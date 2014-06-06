package com.github.jzhongming.mytools.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class DefaultMethodScanner implements MethodScanner {

	@Override
	public List<Method> getMethodList(Class<?> clazz, String methodPattern) {
		return new PatternNameMethodFilter(clazz, methodPattern) {
			@Override
			public boolean filterCondition(Method method) {
				return method.getName().matches(methodPattern);
			}
		}.getMethodList();
	}

	@Override
	public List<Method> getMethodListByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
		return new AnnotationMethodFilter(clazz, annotationType) {
			
			@Override
			public boolean filterCondition(Method method) {
				return method.isAnnotationPresent(annotationType);
			}
		}.getMethodList();
	}

	@Override
	public List<Method> getMethodListByAnnotationInterface(Class<?> clazz, Class<? extends Annotation> annotationType) {
		return new AnnotationMethodFilter(clazz, annotationType) {

			@Override
			public boolean filterCondition(Method method) {
				if(method.isAnnotationPresent(annotationType)) {
					return true;
				}
				Class<?>[] cls = clazz.getInterfaces();
				for (Class<?> c : cls) {
					try {
						Method d = c.getDeclaredMethod(method.getName(), method.getParameterTypes());
						if(d.isAnnotationPresent(annotationType)) {
							return true;
						}
					}catch (Exception e) {
					}
				}
				return false;
			}
		}.getMethodList();
	}
	
}
