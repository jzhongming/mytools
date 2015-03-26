package com.github.jzhongming.mytools.scanner;

import java.lang.reflect.Method;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * Class操作委托工具类
 * 
 * @author zach.J
 *
 */
public class ClassDelegater {

	/**
	 * 得到方法参数名称数组 由于java没有提供获得参数名称的api，利用了javassist来实现
	 *
	 * @return 参数名称数组
	 */
	public static String[] getMethodParamNames(Class<?> clazz, Method method) {

		String[] paramNames;
		try {
			ClassPool clazzPool = ClassPool.getDefault();
			clazzPool.insertClassPath(new ClassClassPath(clazz));
			String[] paramTypeNames = new String[method.getParameterTypes().length];
			Class<?>[] paramTypes = method.getParameterTypes();
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypeNames[i] = paramTypes[i].getName();
			}

			CtClass cc = clazzPool.get(clazz.getName());
			CtMethod cm = cc.getDeclaredMethod(method.getName(), clazzPool.get(paramTypeNames));
			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			if (attr == null) {
                throw new RuntimeException("class:" + clazz.getName()
                        + ", have no LocalVariableTable, please use javac -g:{vars} to compile the source file");
            }
			int startIndex = getStartIndex(attr);
            paramNames = new String[cm.getParameterTypes().length];
            
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++)
                paramNames[i] = attr.variableName(startIndex + i + pos);
            
		} catch (Exception e) {
			e.printStackTrace();
			paramNames = new String[0];
		}

		return paramNames;
	}
	
	private static int getStartIndex(LocalVariableAttribute attr) {
		int index = 0;
		while(index < attr.length()) {
			if("this".equals(attr.variableName(index))) {
				return index;
			}
			++index;
		}
		return 0;
	}
}
