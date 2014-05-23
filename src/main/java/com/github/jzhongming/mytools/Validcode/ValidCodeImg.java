package com.github.jzhongming.mytools.Validcode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * simple introduction
 * 
 * <p>
 * detailed comment
 * </p>
 * 
 * @author Administrator 2013-12-24
 * @see
 * @since 1.0
 */
public class ValidCodeImg implements IValidImag {

	private final int width, height;
	private BufferedImage image;
	private Graphics2D graphics;

	public ValidCodeImg(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private void resetGraphics() {
		image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.setBackground(Color.WHITE);
		graphics.fillRect(0, 0, this.width, this.height);// 画背景
	}

	public byte[] drawValidCode(String value) throws IOException {
		resetGraphics();
		ByteArrayOutputStream os = null;
		try {
			char[] chars = value.toCharArray();
			for (int i = 0; i < chars.length; i++) {

			}
			graphics.setColor(Color.BLUE);
			// graphics.setFont(Font.createFont(Font.TRUETYPE_FONT, new
			// File("D:\\comic.ttf")).deriveFont(Font.BOLD, 45));
			graphics.setFont(new Font("Fixedsys", Font.BOLD, height - 30));
			graphics.drawString(value, 5, height - height / 3);
			// drawPoints(Color.BLUE);
			drawArcs(Color.WHITE);
			drawLine(Color.WHITE);
			twistImage();
			// drawDistort(graphics);
			os = new ByteArrayOutputStream();
			ImageIO.write(this.image, "jpg", os);
		} catch (Exception e) {
			throw new IOException("验证码生成失败", e);
		} finally {
			graphics.dispose();
			image = null;
			os.close();
		}
		return (null != os) ? os.toByteArray() : null;
	}

	private void drawDistort(Graphics g) {
		int period = 8;
		int frames = 2;
		int phase = 3;
		for (int i = 0, c = Math.min(width, height); i < c; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(0, i, c, 1, (int) d, 0);
			g.copyArea(i, 0, c, 1, (int) d, 0);
		}
	}

	/**
	 * 
	 * @Description:正弦曲线Wave扭曲图片
	 * @since 1.0.0
	 * @Date:2012-3-1 下午12:49:47
	 * @return BufferedImage
	 */
	private void twistImage() {
		Random random = new Random();
		double dMultValue = random.nextInt(7) + 3;// 波形的幅度倍数，越大扭曲的程序越高，一般为3
		double dPhase = random.nextInt(6);// 波形的起始相位，取值区间（0-2＊PI）
		BufferedImage destBi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		 Graphics2D g= destBi.createGraphics();
		 g.setColor(Color.WHITE);
		 g.fillRect(0, 0, width, height);
		 g.dispose();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int nOldX = getXPosition4Twist(dPhase, dMultValue, height, i, j);
				int nOldY = j;
				if (nOldX >= 0 && nOldX < width && nOldY >= 0 && nOldY < height) {
					destBi.setRGB(nOldX, nOldY, image.getRGB(i, j));
				}
			}
		}
		
		image = destBi;
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
		double PI = 3.1415926535897932384626433832799*3; // 此值越大，扭曲程度越大
		double dx = (double) (PI * yPosition) / height + dPhase;
		double dy = Math.sin(dx);
		return xPosition + (int) (dy * dMultValue);
	}

	/**
	 * 画干扰弧线
	 * 
	 * @param color
	 */
	private void drawArcs(Color color) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(2));
		graphics.drawArc(0, height / 2, width / 2, width / 5, 0, 180);
		graphics.drawArc(width / 4, height / 2, width / 2, width / 5, 0, -180);
	}

	/**
	 * 画干扰弧线
	 * 
	 * @param color
	 */
	private void drawLine(Color color) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(2));
		graphics.drawLine(0, height / 2, width / 2, width / 5);
	}

	/**
	 * 画干扰点
	 * 
	 * @param color
	 */
	private void drawPoints(Color color) {
		graphics.setColor(color);
		for (int i = 0; i < 1024; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			graphics.drawLine(x, y, x, y);
		}
	}

	// private void mm(OutputStream os) {
	// // 得到指定Format图片的writer
	// Iterator<ImageWriter> iter =
	// ImageIO.getImageWritersByFormatName("jpeg");// 得到迭代器
	// ImageWriter writer = (ImageWriter) iter.next(); // 得到writer
	// // 得到指定writer的输出参数设置(ImageWriteParam )
	// ImageWriteParam iwp = writer.getDefaultWriteParam();
	// iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
	// iwp.setCompressionQuality(0.0001f); // 设置压缩质量参数
	// iwp.setProgressiveMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
	//
	// IIOImage iIamge = new IIOImage(image, null, null);
	// try {
	// // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
	// // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
	// writer.setOutput(ImageIO.createImageOutputStream(os));
	// writer.write(null, iIamge, iwp);
	// } catch (IOException e) {
	// System.out.println("write errro");
	// e.printStackTrace();
	// }
	//
	// }

	@Override
	public String toString() {
		return "ValidCodeImg [width=" + width + ", height=" + height + "]";
	}

	public static void main(String[] args) throws Exception {
		IValidImag img = new ValidCodeImg(150, 75);
		long t = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			byte[] bytes = img.drawValidCode("abc" + i);

			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
			ImageIO.write(bufferedImage, "jpg", new File("e:/validcode/high/image" + i + ".jpg"));
		}
		System.out.println((System.currentTimeMillis() - t) + "ms");
	}

}
