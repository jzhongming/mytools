package com.github.jzhongming.mytools.trie.dat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主要负责双数组生成的实现逻辑，些类为实现的核心
 * @author j.zhongming@gmail.com
 *
 */
public class DATWriter {
	private static final Logger logger = LoggerFactory.getLogger(DATWriter.class);

	private int max_columns; // 最大词字数
	private int max_word_ascii; // 词典中最大ASC码
	private int cbs_allocCount; // 已经分配的bc单元数
	private int max_used_pos; // 最大使用位置
	private int next_free_offset = 1; // 下个空闲位置偏移量

	private int[] ruleInfo; // 规则信息，逗号方式,0号位置放规则总数
	private CBS[] cbs;

	private ArrayList<List<Integer>> rules = new ArrayList<List<Integer>>();
	private ArrayList<String> ruleStr = new ArrayList<String>();
	private HashMap<String, List<Integer>> ruleMap = new HashMap<String, List<Integer>>();

	public void init(Set<String> RULES) {
		ruleInfo = new int[RULES.size() + 1];
		ruleInfo[0] = RULES.size();
		rules.add(null); // 第一个位置为空，标注为根
		ruleStr.add("j.zhongming@gmail.com");
		
		int ruleIndex = 0;
		for (String rule : RULES) {
			_fixRuleInfo(++ruleIndex, rule);
		}
		// max_word_ascii = 65536; // =====================
		cbs_allocCount = max_word_ascii * 3 / 2;
		cbs = new CBS[cbs_allocCount];
		for (int i = 0; i < cbs_allocCount; ++i) {
			cbs[i] = new CBS();
		}
		logger.info("init cbs_allocCount: " + cbs_allocCount);
		buildTrie();//构造DAT

		// 优化数组清理未使用空间
		cleanCBS();
		if(logger.isDebugEnabled()) {
			for (int i = 0; i < cbs.length; i++) {
				if (cbs[i].m_check != 0 || cbs[i].m_base != 0) {
					logger.debug("{} : {}",i,cbs[i].toString());
				}
			}
		}
	}

	private void buildTrie() {
		for (int column = 0; column < max_columns; column++) { // 按列进行循环创建Trie树,并压缩进数组
			Map<Trie, Trie> sortTrie = new TreeMap<Trie, Trie>(trieSort);
			createTrie(column, sortTrie);
			// 将Trie树压缩到双数组中
			for (Map.Entry<Trie, Trie> entry : sortTrie.entrySet()) {
				buildCBS(entry.getValue());
			}
			logger.info(".................................... [{}/{}]",(column+1), max_columns);
		}
	}

	private void cleanCBS() {
		if (cbs.length < max_used_pos + 1) {
			throw new IllegalStateException("some error !! please check");
		}
		logger.info(".................................... clean cbs[{} ==> {}]", cbs.length, (max_used_pos+1));
		CBS[] cleanedCBS = new CBS[max_used_pos + 1];
		System.arraycopy(cbs, 0, cleanedCBS, 0, max_used_pos + 1);
		cbs = cleanedCBS;
		cbs_allocCount = cleanedCBS.length;
	}

	private void buildCBS(final Trie trie) {
		// 定位到trie的m_startPos所在的绝对偏移地址
		int curIndex = trie.m_keys[0];
		cbs[curIndex].m_check = -1; // 首字设置为-1
		for (int i = 1; i <= trie.position; i++)
			curIndex = Math.abs(cbs[curIndex].m_base) + trie.m_keys[i];

		if (max_used_pos < curIndex) // 设置最大的值
			max_used_pos = curIndex;

		// 设置后续节点的m_check值和当前节点的m_base值
		if (trie.m_childrenIndex.size() > 0) {
			// 获取其子节点的base值 //这时解决冲突
			int base = getConsistentBasePos(trie);
			// 设置该trie的m_base值
			cbs[curIndex].m_base = (trie.ruleIndex == 0) ? Math.abs(base) : -1 * Math.abs(base); // 是否成词
			for (int asc : trie.m_childrenIndex) { // 安排子节点的的Check位置
				int addr = base + rules.get(asc).get(trie.position + 1);// 这里position+1,设置下一个位置的Check
				cbs[addr].m_check = curIndex; // 设置check值
				if (max_used_pos < addr) {// 设置最大使用位置
					max_used_pos = addr;
				}
			}
		} else { // 成词 使base值为负
			if (cbs[curIndex].m_base > 0)
				cbs[curIndex].m_base *= -1;
			else if (cbs[curIndex].m_base == 0) {
//				cbs[curIndex].m_base = -1 * (trie.ruleIndex);
				cbs[curIndex].m_base = -1*curIndex;
			}
		}
	}

	private int getConsistentBasePos(final Trie trie) {
		// int cbs_allocCount; // 已经分配的bc单元数
		// int next_free_pos; // 下一个可用空位
		// int max_used_pos; // 最大使用位置
		// 移动到第一个可以存放空间的位置
		int cbs_position = max_word_ascii + next_free_offset;
		int next_position = 0;
		while (cbs_position < cbs_allocCount && cbs[cbs_position].m_check != 0)
			cbs_position++;

		if (cbs_position >= cbs_allocCount) {
			reallocCBSCount(cbs_position);
		}

		Set<Integer> chiled = trie.m_childrenIndex;
		boolean found_flag = false;
		while (!found_flag) {
			found_flag = true;
			for (int asc : chiled) {
				next_position = cbs_position + rules.get(asc).get(trie.position + 1);
				if (next_position >= cbs_allocCount) {
					reallocCBSCount(next_position);
				}
				if (cbs[next_position].m_check != 0) { // 占用
					cbs_position++;
					found_flag = false;
					break;
				}
			}
		}
		while (cbs[max_word_ascii + next_free_offset].m_check != 0) {
			next_free_offset++;
		}

		return cbs_position;
	}

	private void reallocCBSCount(final int capacity) {
		CBS[] newCbs = new CBS[capacity * 3 / 2];
		System.arraycopy(cbs, 0, newCbs, 0, cbs_allocCount);
		for (int i = cbs_allocCount; i < newCbs.length; ++i) {
			newCbs[i] = new CBS();
		}
		cbs = newCbs;
		cbs_allocCount = newCbs.length;
		if(logger.isDebugEnabled())
			logger.debug("realloc cbs_allocCount: {},{}", capacity, cbs_allocCount);
	}

	private Map<String, Trie> createTrie(int column, Map<Trie, Trie> sortTrie) {
		Map<String, Trie> uniqMap = new TreeMap<String, Trie>();
		Trie trie = null;
		for (int index = 1, c = rules.size(); index < c; index++) {
			List<Integer> rule = rules.get(index);
			if (column > rule.size() - 1)
				continue;

			String key = ruleStr.get(index).substring(0, column + 1);
			if (uniqMap.containsKey(key)) {
				trie = uniqMap.get(key);
			} else {
				trie = new Trie(0, column, rules.get(index)); // 未成词，ruleIndex 为 0
				uniqMap.put(key, trie);
			}
			if (column != rule.size() - 1) // 如果不是最后一个
				trie.m_childrenIndex.add(index);
			else {
				trie.ruleIndex = index; // 是词，标注上ruleIndex，表示成词
			}
		}
		for (Map.Entry<String, Trie> entry : uniqMap.entrySet()) {
			sortTrie.put(entry.getValue(), entry.getValue());
		}

		return uniqMap;
	}

	private void _fixRuleInfo(final int index, final String rule) {
		String[] words = rule.split(",");
		for (String w : words) {
			ruleStr.add(w);
			if (max_columns < w.length()) {
				max_columns = w.length();
			}
			List<Integer> subRule = new ArrayList<Integer>();
			for (int c : w.toCharArray()) { // 将123 ---> 49 50 51
				subRule.add(c);
				if (max_word_ascii < c) {
					max_word_ascii = c;
				}
			}
			rules.add(subRule);
			
			List<Integer> ruleList = ruleMap.get(w);
			if(null == ruleList) {
				ruleList = new ArrayList<Integer>();
				ruleList.add(index);
				ruleMap.put(w, ruleList);
			} else {
				ruleList.add(index);
			}
		}
		ruleInfo[index] = 0xFFFFFFFF << (32 - words.length);
		if (logger.isDebugEnabled())
			logger.debug("ruleInfo {} = {}", index, Integer.toBinaryString(ruleInfo[index]));
	}

	public CBS[] getCBS() {
		return cbs;
	}

	public void dumpMMap(File file) throws IOException {
		DataOutputStream dos = null;
		long t = System.currentTimeMillis();
		try {
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			dos.writeInt(this.cbs_allocCount);
			dos.writeInt(this.max_columns);
			dos.writeInt(this.max_used_pos);
			dos.writeInt(this.max_word_ascii);
			dos.writeInt(this.next_free_offset);
			for (int info : ruleInfo) {
				dos.writeInt(info);
			}
			dos.writeInt(cbs.length);
			for (CBS c : cbs) {
				dos.writeInt(c.m_base);
				dos.writeInt(c.m_check);
			}
			byte[] byteRuleMap = serialize(this.ruleMap);
			dos.writeInt(byteRuleMap.length);
			dos.write(byteRuleMap);
			byte[] byteRules = serialize(this.rules);
			dos.writeInt(byteRules.length);
			dos.write(byteRules);
			byte[] byteRuleStr = serialize(this.ruleStr);
			dos.writeInt(byteRuleStr.length);
			dos.write(byteRuleStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != dos) {
				dos.close();
			}
		}
		logger.info("MMap file dump success. Time: {}(ms) path: {}", (System.currentTimeMillis() - t), file.getPath());
	}

	@SuppressWarnings("unchecked")
	public DATWriter loadMMap(File file) {
		DataInputStream dis = null;
		long t = System.currentTimeMillis();
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			this.cbs_allocCount = dis.readInt();
			this.max_columns = dis.readInt();
			this.max_used_pos = dis.readInt();
			this.max_word_ascii = dis.readInt();
			this.next_free_offset = dis.readInt();
			int ruleInfoSize = dis.readInt();
			this.ruleInfo = new int[ruleInfoSize+1];
			this.ruleInfo[0] = ruleInfoSize;
			for(int i=1; i<=ruleInfoSize; i++) {
				ruleInfo[i] = dis.readInt();
			}
			int cbsLength = dis.readInt();
			this.cbs = new CBS[cbsLength];
			for (int i=0; i< cbsLength; i++) {
				cbs[i] = new CBS(dis.readInt(), dis.readInt());
			}
			int byteRuleMapSize = dis.readInt();
			byte[] byteRuleMap = new byte[byteRuleMapSize];
			dis.readFully(byteRuleMap);
			this.ruleMap = (HashMap<String, List<Integer>>) deserialize(byteRuleMap);
			int byteRulesSize = dis.readInt();
			byte[] byteRules = new byte[byteRulesSize];
			dis.readFully(byteRules);
			this.rules = (ArrayList<List<Integer>>) deserialize(byteRules);
			int byteRuleStrSize = dis.readInt();
			byte[] byteRuleStr = new byte[byteRuleStrSize];
			dis.readFully(byteRuleStr);
			this.ruleStr = (ArrayList<String>) deserialize(byteRuleStr);
		} catch (Exception e) {
			logger.error("加载索引文件出错",e);
		} finally {
			if (null != dis) {
				try {
					dis.close();
				} catch (IOException e) {
				}
			}
		}
		logger.info("MMap file load success. Time: {}(ms) path: {}", (System.currentTimeMillis() - t), file.getPath());
		return this;
	}
	
	public List<String> getRuleStr() {
		return ruleStr;
	}
	
	public int[] getRuleInfo() {
		return ruleInfo;
	}
	
	public Map<String, List<Integer>> getRuleMap() {
		return ruleMap;
	}
	
	public List<Pointer> check(final String str) {
		List<Pointer> result = new ArrayList<Pointer>();
		int position = 0, mark = 0, code = 0, precode = 0, base = 0, len = str.length();
		boolean searching = false;  // 是否进入搜索状态
		while (position < len) {
			code = str.charAt(position);
			if (precode == 0) {
				precode = code;
			} 
			if (code <= max_word_ascii) {
				if(base + code > cbs.length) {//超出范围
					position = ++mark;
					searching = false;
					precode = 0;
					base = 0 ;
					continue;
				}
				if (!searching && cbs[precode].m_check == -1) {
					base = cbs[precode].m_base;
					if (base < 0) {
						result.add(new Pointer(mark, position+1));
						mark = ++position;
						searching = false;
						base = 0;
						precode = 0;
						continue;
					}
					mark = position++;
					searching = true;
					continue;
				} else if (searching && cbs[base + code].m_check == precode) {
					precode = base + code;
					base = cbs[precode].m_base;
					if (base < 0) {
						result.add(new Pointer(mark, position+1));
						mark = ++position;
						searching = false;
						precode = 0;
						base = 0;
						continue;
					} else {
						position++;
					}
				} else {
					position = ++mark;
					searching = false;
					precode = 0;
					base = 0 ;
				}

			} else {
				position = ++mark;
				searching = false;
				precode = 0;
				base = 0;
			}
		}
		return result;
	}
	
	private static final byte[] serialize(Serializable data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ObjectOutputStream out = null;
        try {
            // stream closed in the finally
            out = new ObjectOutputStream(baos);
            out.writeObject(data);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return baos.toByteArray();
    }
	
	private static final Object deserialize(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream in = null;
        try {
            // stream closed in the finally
            in = new ObjectInputStream(bais);
            return in.readObject();
            
        } catch (Exception e) {
          	e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }
	
	// /**
	// * Trie排序规则
	// */
	// static Comparator<Trie> trieSort = new Comparator<Trie>() {
	// public int compare(Trie trie1, Trie trie2) {
	// System.out.println(trie1.toString() + "=====" + trie2.toString());
	// if (trie1.m_childrenIndex.size() == trie2.m_childrenIndex.size()) {
	// if (trie1.position == trie2.position)
	// return trie1.ruleIndex > trie2.ruleIndex ? 1 : -1;
	//
	// return trie1.position > trie2.position ? 1 : -1;
	// }
	//
	// return trie1.m_childrenIndex.size() > trie2.m_childrenIndex.size() ? 1 :
	// -1;
	// }
	// };

	/**
	 * Trie排序规则
	 */
	static Comparator<Trie> trieSort = new Comparator<Trie>() {
		public int compare(Trie trie1, Trie trie2) {
			if (trie1.m_childrenIndex.size() == trie2.m_childrenIndex.size()) {
				return trie1.m_keys[trie1.position] > trie2.m_keys[trie2.position] ? 1 : -1;
			}

			return trie1.m_childrenIndex.size() > trie2.m_childrenIndex.size() ? 1 : -1;
		}
	};

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer("\nDAT info ...\n");
		sbf.append("max_columns:  ").append(max_columns).append("\n")
				.append("max_word_ascii: ").append(max_word_ascii).append("\n")
				.append("cbs_allocCount: ").append(cbs_allocCount).append("\n");
		return sbf.toString();
	}

}
