package com.github.jzhongming.mytools.trie.dat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责配置词典文件和处理规则，例如违禁词去重，排序等
 * @author j.zhongming@gmail.com
 *
 */
public class DATBuilder {
	private static final Logger logger = LoggerFactory.getLogger(DATBuilder.class);
	
	private Set<String> RULES = new TreeSet<String>(); // 合并后的规则列表
	
	private List<File> dictList = new ArrayList<File>();
	private DATWriter datWriter = new DATWriter();
	
	protected DATBuilder() {
	}
	
	public DATBuilder(File dictDir, final String suffix) {
		if(!dictDir.exists()) {
			throw new IllegalArgumentException(dictDir.getPath() + " not found!");
		}
		if(!suffix.startsWith(".")) {
			throw new IllegalArgumentException("suffix must start with [.]; eg. .txt");
		}
		logger.info("start build DAT ...");
		long start = System.currentTimeMillis();
		if(dictDir.isDirectory()) {
			dictList = Arrays.asList(dictDir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getPath().endsWith(suffix);
				}
			}));
		} else {
			dictList.add(dictDir);
		}
		processRules(dictList);
		
		datWriter.init(RULES);
		
		if(logger.isInfoEnabled()) {
			if(logger.isDebugEnabled()) {
				for(String s : RULES) {
					logger.debug(s);
				}
			}
			logger.info("build DAT finished use time {} (ms)", (System.currentTimeMillis()-start));
			logger.info("dict file count:{}", dictList.size());
			logger.info("rule count:{}", RULES.size());
		}
	}
	
	public CBS[] getCBS() {
		return datWriter.getCBS();
	}
	
	public int[] gerRuleInfo() {
		return datWriter.getRuleInfo();
	}
	
	public void writeDATMap(File file) throws IOException {
		datWriter.dumpMMap(file);
	}
	
	public static DATBuilder loadDATMap(File datIndex) {
		DATBuilder dat = new DATBuilder();
		dat.datWriter = new DATWriter().loadMMap(datIndex);
		System.out.println(dat.datWriter);
		return dat;
	}
	
	private void processRules(List<File> files) {
		logger.info("start process files ... ");
		for(File file : dictList) {
			RULES.addAll(readFileByLine(file));
		}
	}
	
	public List<String> check2SpanList(final String content) {
		List<Pointer> plist = datWriter.check(content);
		List<String> slist = new ArrayList<String>(plist.size());
		for(Pointer p : plist) {
			slist.add(content.substring(p.limit,p.size));
		}
		return slist;
	}
	
	public boolean check2IsSpan(final String content) {
		Set<String> plist = new HashSet<String>(check2SpanList(content));
//		int[] ruleInfo = new int[datWriter.getRuleInfo().length];
		Map<Integer, Integer> ruleInfo = new HashMap<Integer, Integer>();
//		System.arraycopy(datWriter.getRuleInfo(), 0, ruleInfo, 0, ruleInfo.length);
		Map<String, List<Integer>> map = datWriter.getRuleMap();
		for(String s : plist) {
			for(int index : map.get(s)) {
				Integer rule = ruleInfo.get(index);
				if(rule == null) {
					rule = datWriter.getRuleInfo()[index];
				}
				rule<<=1;
				if(rule == 0) {
					return true;
				} else {
					ruleInfo.put(index, rule);
				}
			}
		}
		return false;
	}
	
	private Set<String> readFileByLine(File file) {
		Set<String> lines = new TreeSet<String>();
		BufferedReader reader = null;
		try{
			logger.info("prcessing --> {} ...", file.getPath());
			reader = new BufferedReader((new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))));
			///////////////////////////////////////////////////////
			/**
			 * 65279的十六进制表示为FEFF，它是字节顺序标记（英语：byte-order mark，BOM）是位于码点U+FEFF的统一码字符的名称。
			 * 当以UTF-16或UTF-32来将UCS/统一码字符所组成的字串编码时，这个字符被用来标示其字节序。它常被用来当做标示文件是以UTF-8、UTF-16或UTF-32编码的记号.
			 */
//			reader.mark(1); // 处理BOM 65279的十六进制表示为FEFF unitcode:65533 utf-8:65279
			int BOM = reader.read();
			if(logger.isDebugEnabled())
				logger.debug("{} --> BOM: {}", file.getName(), BOM);
			if(BOM != 65279) {
				throw new IllegalArgumentException("file not encode by UTF-8 : " + file.getPath());
//				reader.reset();
			}
			//////////////////////////////////////////////////////
			String line;
			while((line = reader.readLine()) != null) {
				if(!line.trim().isEmpty()) { // 过滤空字符
					line = _wordSort(line);
					lines.add(line);
				}
			}
			
			logger.info("{} --> Rules: {}", file.getName(), lines.size());
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return lines;
	}
	
	private String _wordSort(String rule) {
		String[] words = rule.split(",");
		if(words.length > 32) {
			logger.warn("逗号方式不能超过32个词 ,将截取前32个词做为规则{}", rule);
			String[] nword = new String[32];
			System.arraycopy(words, 0, nword, 0, 32);
			words = nword;
		}
		
		Set<String> sortSet= new TreeSet<String>(Arrays.asList(words));
		StringBuffer sbf = new StringBuffer();
		for(String s : sortSet) {
			sbf.append(s.trim()).append(",");
		}
		return sbf.substring(0, sbf.lastIndexOf(","));
	}
	
	public static void main(String[] args) {
		new DATBuilder(new File("d:/test"), ".txt");
	}
}
