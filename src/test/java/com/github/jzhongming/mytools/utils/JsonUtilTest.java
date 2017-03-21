package com.github.jzhongming.mytools.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonUtilTest {
	
//	@Test
	public void map2JSONStringTest() {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("name", "alex");
		map.put("age", 33);
		map.put("time", Calendar.getInstance().getTime().getTime());
		System.out.println(JsonUtil.map2JSONString(map));
	}
	
//	@Test
	public void beanToJsonTest() {
		TestUserBean user = new TestUserBean();
		user.setAge(23);
		user.setName("alex");
		user.setTime(new Date());
		List<Tt> list = new ArrayList<Tt>();
		Tt tt = new Tt();
		tt.setGid(123);
		tt.setNovelName("斗破苍穹");
		list.add(tt);
		list.add(tt);
		list.add(tt);
		user.setList(list);

		System.out.println(JsonUtil.beanToJson(user));
		System.out.println(JsonUtil.beanToJson(user, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
//	@Test
	public void beanToJsonFieldTest() {
		TestUserBean user = new TestUserBean();
		user.setAge(23);
		user.setName("alex");
		user.setTime(new Date());
		List<Tt> list = new ArrayList<Tt>();
		Tt tt = new Tt();
		tt.setGid(123);
		tt.setNovelName("斗破苍穹");
		list.add(tt);
		list.add(tt);
		list.add(tt);
		user.setList(list);
		
		System.out.println(JsonUtil.beanToJson(user, new String[]{"name", "list"}));
		System.out.println(JsonUtil.beanToJson(user, new String[]{"name", "time"}, "yyyy-MM-dd HH:mm:ss.SSS"));
		System.out.println(JsonUtil.beanToJson(user, new String[]{"name", "time"}, "yyyy年MM月dd日  HH:mm:ss.SSS"));
	}
	
	@Test
	public void jsonToBeanTest() {
		System.out.println(JsonUtil.jsonToBean("{\"name\":\"alex\",\"time\":\"1314450370849\",\"age\":33}", TestUserBean.class));
		System.out.println(JsonUtil.jsonToBean("{\"age\":23,\"name\":\"alex\",\"time\":\"2012-12-01 19:06:10.849\"}", TestUserBean.class, "yyyy-MM-dd HH:mm:ss.SSS"));
		System.out.println(JsonUtil.jsonToBean("{\"age\":23,\"name\":\"alex\",\"time\":\"2011年08月27日 19:06:10.849\"}", TestUserBean.class, "yyyy年MM月dd日 HH:mm:ss.SSS"));
		System.out.println(JsonUtil.jsonToBean("{\"age\":23,\"name\":\"alex\",\"time\":1314454167911,\"list\":[{\"gid\":123,\"novelName\":\"斗破苍穹\",\"id\":0},{\"gid\":123,\"novelName\":\"斗破苍穹\",\"id\":0},{\"gid\":123,\"novelName\":\"斗破苍穹\",\"id\":0}]}", TestUserBean.class));
	}
}
