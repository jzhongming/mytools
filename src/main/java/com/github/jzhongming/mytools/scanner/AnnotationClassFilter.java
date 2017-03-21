package com.github.jzhongming.mytools.scanner;

import java.lang.annotation.Annotation;

public abstract class AnnotationClassFilter extends DefaultClassFilter {

	protected final Class<? extends Annotation> annotationClass;

	protected AnnotationClassFilter(String packageName, Class<? extends Annotation> annotationClass) {
		super(packageName);
		this.annotationClass = annotationClass;
	}
	
	public AnnotationClassFilter(String packageName, Class<? extends Annotation> annotationClass, ClassLoader loader) {
		super(packageName, loader);
		this.annotationClass = annotationClass;
	}
}
