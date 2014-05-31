package com.github.jzhongming.mytools.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

public class DefaultClassScanner implements ClassScanner {

	@Override
	public Set<Class<?>> getClassList(String packageName) {
		return new DefaultClassFilter(packageName) {
			@Override
			public boolean filterCondition(Class<?> cls) {
				String className = cls.getName();
				String pkgName = className.substring(0, className.lastIndexOf("."));
				return pkgName.startsWith(packageName);

			}
		}.getClassList();
	}

	@Override
	public Set<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
		return new AnnotationClassFilter(packageName, annotationClass) {
			@Override
			public boolean filterCondition(Class<?> cls) {
				return cls.isAnnotationPresent(annotationClass);
			}
		}.getClassList();
	}

	@Override
	public Set<Class<?>> getClassListBySuper(String packageName, Class<?> superClass) {
		return new SupperClassFilter(packageName, superClass) {
			@Override
			public boolean filterCondition(Class<?> cls) { // 这里去掉了内部类
				return superClass.isAssignableFrom(cls) && !superClass.equals(cls);// && !cls.getName().contains("$");
			}

		}.getClassList();
	}

	//Test code
	public static void main(String[] args) {
		ClassScanner csn = new DefaultClassScanner();
		@SuppressWarnings("unused")
		Set<Class<?>> cs = csn.getClassList("com.github.jzhongming.mytools.utils");
		System.out.println("==================================================");
		cs = csn.getClassListByAnnotation("com.github.jzhongming.mytools.scanner", QATest.class);
		System.out.println("==================================================");
		cs = csn.getClassListBySuper("com.github.jzhongming.mytools", DefaultClassFilter.class);
	}
}
