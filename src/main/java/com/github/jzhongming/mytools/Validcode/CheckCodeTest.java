package com.github.jzhongming.mytools.Validcode;

import org.junit.Assert;
import org.junit.Test;

/**
 * CheckCodeTest.java
 * 
 * @author King<br/>
 * 
 * @Description 描述一下这个文件
 * @since 1.0.0
 * @Date 2012-2-29下午1:51:13
 */
public class CheckCodeTest {
	@Test
	public void testCheckCodeCreate() {
		CheckCode checkCode = new CheckCode();

		checkCode = checkCode.createCheckCode();
		String checkCodeStr = checkCode.getCheckCodeStr();
		System.out.println(checkCodeStr);
		Assert.assertNotNull(checkCodeStr);
		Assert.assertEquals(4, checkCodeStr.length());

		Assert.assertNotNull(checkCode.getBuffImg());

		for (int i = 0; i < 10; i++) {
			checkCode = checkCode.createCheckCode();
			checkCodeStr = checkCode.getCheckCodeStr();
			System.out.println(checkCodeStr);
			Assert.assertNotNull(checkCodeStr);
			Assert.assertEquals(4, checkCodeStr.length());

			String filePathName = "e:/validcode/high/image"+i+".jpg";
			checkCode.createImgFile(filePathName);
		}
	}
}
