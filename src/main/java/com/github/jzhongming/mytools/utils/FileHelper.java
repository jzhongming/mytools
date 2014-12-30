package com.github.jzhongming.mytools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FileHelper {

	/**
	 * create file
	 * 
	 * @param filePath
	 * @param content
	 * @throws IOException
	 */
	public static void createFile(final String filePath, final String content) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(content.getBytes());
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * create multilevel folder
	 * 
	 * @param path
	 */
	public static boolean createFolder(final String path) {
		File file = new File(path);
		if (!file.exists() && file.isDirectory())
			return file.mkdirs();

		return false;
	}

	/**
	 * move file
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean moveFile(final String oldPath, final String newPath) {
		File fileOld = new File(oldPath);
		if (fileOld.exists()) {
			File fileNew = new File(newPath);
			return fileOld.renameTo(fileNew);
		}
		return false;
	}

	/**
	 * delete file
	 * 
	 * @param path
	 */
	public static boolean deleteFile(final String path) {
		File file = new File(path);
		return file.delete();
	}

	/**
	 * get all file which in dir
	 * 
	 * @param dir
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	public static List<File> getUniqueFiles(final String dirPath, boolean recursive, final String... extension) throws IOException {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new IOException("file not exist:" + dir);
		}

		Set<File> fileSet = new TreeSet<File>();
		if (dir.isFile()) {
			checkExtension(fileSet, dir, extension);
		} else {
			for (File f : dir.listFiles()) {
				if (f.isDirectory() && recursive) {
					fileSet.addAll(getUniqueFiles(f.getPath(), recursive, extension));
				}
				checkExtension(fileSet, f, extension);
			}
		}
		List<File> fileList = new ArrayList<File>(fileSet.size());
		fileList.addAll(fileSet);
		return fileList;
	}

	private static void checkExtension(Collection<File> collection, final File file, final String... extension) {
		for (String ext : extension) {
			if (file.getPath().toLowerCase().endsWith(ext.toLowerCase())) {
				collection.add(file);
			}
		}
	}

	/**
	 * get all jar/war/ear which in dir
	 * 
	 * @param dirList
	 * @return
	 * @throws IOException
	 */
	public static List<String> getUniqueLibPath(boolean recursive, String... dirs) throws IOException {
		List<String> jarList = new ArrayList<String>();
		for (String dir : dirs) {
			List<File> fileList = FileHelper.getUniqueFiles(dir, recursive, ".jar");
			if (fileList != null) {
				for (File file : fileList) {
					jarList.add(file.getCanonicalPath());
				}
			}
		}
		return jarList;
	}

	public static boolean copyFile(final String srcPath, final String distPath) {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(srcPath);
			fout = new FileOutputStream(distPath);
			FileChannel fcin = fin.getChannel();
			FileChannel fcout = fout.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(2048);
			for (;;) {
				buffer.clear();
				int r = fcin.read(buffer);
				if (r == -1) {
					break;
				}
				buffer.flip();
				fcout.write(buffer);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != fout) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fin) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 按行读取文件
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static String getContentByLines(final String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			throw new IOException("file not exist:" + path);
		}
		BufferedReader reader = null;
		StringBuilder sbContent = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sbContent.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sbContent.toString();
	}

	public static void main(String[] args) throws IOException {

		FileHelper.copyFile("C:\\Users\\Administrator\\Desktop\\NioClient-master\\README.md", "C:\\Users\\Administrator\\Desktop\\README.md");
	}
}
