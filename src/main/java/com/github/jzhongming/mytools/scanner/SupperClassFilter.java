package com.github.jzhongming.mytools.scanner;

public abstract class SupperClassFilter extends DefaultClassFilter {

	protected final Class<?> superClass;

	protected SupperClassFilter(String packageName, Class<?> superClass) {
		super(packageName);
		this.superClass = superClass;
	}

}
