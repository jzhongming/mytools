package com.github.jzhongming.mytools.Validcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * CheckCode.java
 * 
 * @author King<br/>
 * 
 * @Description 验证码生成类
 * @since 1.0.0
 * @Date 2012-2-29下午1:50:25
 */
public class CheckCode {
	private int width = 180;
	private int height = 75;
	private int codeCount = 4;

	private Random random = new Random();

	/**
	 * 验证码图片
	 */
	private BufferedImage buffImg;
	/**
	 * 验证码字符串
	 */
	private String checkCodeStr;

	/**
	 * 
	 * @Description:创建验证码对象
	 * @since 1.0.0
	 * @Date:2012-3-1 上午10:26:20
	 * @return CheckCode
	 */
	public CheckCode createCheckCode() {
		CheckCode checkCode = new CheckCode();
		checkCode.setCheckCodeStr(createRandomCode());
		checkCode.setBuffImg(disturb());
		return checkCode;
	}

	/**
	 * 
	 * @Description:随机产生的验证码
	 * @since 1.0.0
	 * @Date:2012-3-1 上午10:20:05
	 * @return String
	 */
	private String createRandomCode() {
		StringBuffer randomCode = new StringBuffer();

		String strRand = null;
		int xx = width / (codeCount + 1);
		int codeY = height - 4;
		char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

		Graphics2D graphics = graphicsInit();
		graphics.setColor(createColor());

		for (int i = 0; i < codeCount; i++) {
			strRand = String.valueOf(codeSequence[random.nextInt(32)]);
			randomCode.append(strRand);

			graphics.drawString(strRand, (i + 1) * xx, codeY);
		}

		return randomCode.toString();
	}

	/**
	 * 
	 * @Description:创建颜色
	 * @since 1.0.0
	 * @Date:2012-2-29 下午4:47:14
	 * @return Color
	 */
	private Color createColor() {
		Color color[] = new Color[10];
		color[0] = new Color(113, 31, 71);
		color[1] = new Color(37, 0, 37);
		color[2] = new Color(111, 33, 36);
		color[3] = new Color(0, 0, 112);
		color[4] = new Color(14, 51, 16);
		color[5] = new Color(1, 1, 1);
		color[6] = new Color(72, 14, 73);
		color[7] = new Color(65, 67, 29);
		color[8] = new Color(116, 86, 88);
		color[9] = new Color(41, 75, 71);

		return color[random.nextInt(10)];
	}

	/**
	 * 
	 * @Description:绘制类初始化
	 * @since 1.0.0
	 * @Date:2012-3-1 上午10:17:52
	 * @return Graphics2D
	 */
	private Graphics2D graphicsInit() {
		Graphics2D graphics = buffImgInit().createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setFont(new Font("Fixedsys", Font.BOLD, height - 2));
		graphics.drawRect(0, 0, width - 1, height - 1);
		return graphics;
	}

	/**
	 * 
	 * @Description:BufferedImage初始化
	 * @since 1.0.0
	 * @Date:2012-3-1 上午11:07:18
	 * @return BufferedImage
	 */
	private BufferedImage buffImgInit() {
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		return buffImg;
	}

	/**
	 * 
	 * @Description:绘制干扰特性
	 * @since 1.0.0
	 * @Date:2012-3-1 上午11:45:32
	 * @return BufferedImage
	 */
	private BufferedImage disturb() {
		drawDisturbLine(buffImg.createGraphics());
		return twistImage();
	}

	/**
	 * 
	 * @Description:画干扰线使图象中的认证码不易被其它程序探测到
	 * @since 1.0.0
	 * @Date:2012-2-29 下午4:28:23
	 * @param graphics
	 *            void
	 */
	private void drawDisturbLine(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		int x = 0;
		int y = 0;
		int xl = 0;
		int yl = 0;
		for (int i = 0; i < 15; i++) {
			x = random.nextInt(width);
			y = random.nextInt(height);
			xl = random.nextInt(20);
			yl = random.nextInt(10);
			graphics.drawLine(x, y, x + xl, y + yl);
		}
	}

	/**
	 * 
	 * @Description:正弦曲线Wave扭曲图片
	 * @since 1.0.0
	 * @Date:2012-3-1 下午12:49:47
	 * @return BufferedImage
	 */
	private BufferedImage twistImage() {
		double dMultValue = random.nextInt(7) + 3;// 波形的幅度倍数，越大扭曲的程序越高，一般为3
		double dPhase = random.nextInt(6);// 波形的起始相位，取值区间（0-2＊PI）

		BufferedImage destBi = new BufferedImage(buffImg.getWidth(), buffImg.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < destBi.getWidth(); i++) {
			for (int j = 0; j < destBi.getHeight(); j++) {
				int nOldX = getXPosition4Twist(dPhase, dMultValue, destBi.getHeight(), i, j);
				int nOldY = j;
				if (nOldX >= 0 && nOldX < destBi.getWidth() && nOldY >= 0 && nOldY < destBi.getHeight()) {
					destBi.setRGB(nOldX, nOldY, buffImg.getRGB(i, j));
				}
			}
		}
		return destBi;
	}

	/**
	 * 
	 * @Description:获取扭曲后的x轴位置
	 * @since 1.0.0
	 * @Date:2012-3-1 下午3:17:53
	 * @param dPhase
	 * @param dMultValue
	 * @param height
	 * @param xPosition
	 * @param yPosition
	 * @return int
	 */
	private int getXPosition4Twist(double dPhase, double dMultValue, int height, int xPosition, int yPosition) {
		double PI = 5*3.1415926535897932384626433832799; // 此值越大，扭曲程度越大
		double dx = (double) (PI * yPosition) / height + dPhase;
		double dy = Math.sin(dx);
		return xPosition + (int) (dy * dMultValue);
	}

	/**
	 * 
	 * @Description:将图像进行输出到文件
	 * @since 1.0.0
	 * @Date:2012-3-1 上午11:56:19
	 * @param pathName
	 * @return String
	 */
	public String createImgFile(String pathName) {
		File file = new File(pathName);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		try {
			ImageIO.write(buffImg, "jpeg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pathName;
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}

	public void setBuffImg(BufferedImage buffImg) {
		this.buffImg = buffImg;
	}

	public String getCheckCodeStr() {
		return checkCodeStr;
	}

	public void setCheckCodeStr(String checkCodeStr) {
		this.checkCodeStr = checkCodeStr;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

}
