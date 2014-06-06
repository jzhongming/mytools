package com.github.jzhongming.mytools.scanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class DefaultMethodFilter {
	protected final Class<?> clazz;
	
	protected DefaultMethodFilter(Class<?> clazz) {
		this.clazz = clazz;
	}

	public abstract boolean filterCondition(Method method);
	
	public List<Method> getMethodList() {
		List<Method> mlist = new ArrayList<Method>();
		for(Method m : clazz.getMethods()) {
			if(filterCondition(m)) {
				mlist.add(m);
			}
		}
		return mlist;
	}

}
