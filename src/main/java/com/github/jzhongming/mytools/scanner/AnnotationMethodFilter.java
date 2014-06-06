package com.github.jzhongming.mytools.scanner;

import java.lang.annotation.Annotation;

public abstract class AnnotationMethodFilter extends DefaultMethodFilter {

	protected final Class<? extends Annotation> annotationType;
	protected AnnotationMethodFilter(Class<?> clazz, Class<? extends Annotation> annotationType) {
		super(clazz);
		this.annotationType = annotationType;
	}

}
