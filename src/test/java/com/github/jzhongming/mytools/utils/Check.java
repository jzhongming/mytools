package com.github.jzhongming.mytools.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

// welcome to mail to shenjian@58.com

public class Check {
	private DoubleArrayTrie _mainDAT;
	private int[][] _ruleGroup;
	private char[][][] _ruleWords;

	private int[] _base;
	private int[] _check;
	private char[] _tail;
	private int[] _tailRule;

	private boolean[] _bMultiGroup;
	private static final char S_END_CHAR = '\0';

	public void init(Vector<String> dict) {
		try {
			Map<String, int[]> fstWdMap = getFirstWordMap(dict);

			_mainDAT = new DoubleArrayTrie();
			_ruleGroup = new int[fstWdMap.size()][];
			_bMultiGroup = new boolean[fstWdMap.size()];
			int index = 0;
			for (String key : fstWdMap.keySet()) {
				int[] rules = fstWdMap.get(key);
				_mainDAT.Insert(key, index);
				_ruleGroup[index] = rules;
				if (rules.length == 1 && _ruleWords[rules[0]].length == 1) {
					_bMultiGroup[index] = false;
				} else {
					_bMultiGroup[index] = true;
				}
				index++;
			}

			initAllFirstChar(fstWdMap);

			_base = _mainDAT.Base;
			_check = _mainDAT.Check;
			_tail = _mainDAT.Tail;
			_tailRule = _mainDAT.Tail_Rule;

			fstWdMap = null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("Check Starting");
		return;
	}

	private boolean[] _allFirstChar = new boolean[52000];
	private boolean[] _allAllSecondChar = new boolean[52000];
	private boolean[] _allSecondChar = new boolean[52000];
	private byte[] _startCharPos = new byte[52000];
	private TreeSet<Character> chTree = new TreeSet<Character>();

	private void initAllFirstChar(Map<String, int[]> fstWdMap) {
		for (String key : fstWdMap.keySet()) {
			chTree.add(key.toCharArray()[0]);
		}
		for (char a : chTree) {
			_allFirstChar[a] = true;
			_startCharPos[a] |= 1; 
		}
		chTree = null;
		chTree = new TreeSet<Character>();
		for (String key : fstWdMap.keySet()) {
			chTree.add(key.toCharArray()[1]);
		}
		for (char a : chTree) {
			_allSecondChar[a] = true;
			_startCharPos[a] |= 2; 
		}
		chTree = null;

		chTree = new TreeSet<Character>();
		for (String key : fstWdMap.keySet()) {
			chTree.add(key.toCharArray()[0]);
			chTree.add(key.toCharArray()[1]);
		}
		for (char a : chTree) {
			_allAllSecondChar[a] = true;
		}
		chTree = null;
	}

	private Map<String, int[]> getFirstWordMap(Vector<String> dict) {

		int dictSize = dict.size();
		_ruleWords = new char[dictSize][][];
		Map<String, int[]> firstWdAtRules = new HashMap<String, int[]>();
		for (int i = 0; i < dictSize; i++) {
			String[] tmpWords = dict.get(i).split(",");
			_ruleWords[i] = new char[tmpWords.length][];
			for (int j = 0; j < tmpWords.length; j++) {
				_ruleWords[i][j] = tmpWords[j].toCharArray();
			}

			String firstWd = tmpWords[0];
			if (firstWdAtRules.containsKey(firstWd)) {
				int[] tmpRls = firstWdAtRules.get(firstWd);
				int[] flRls = new int[tmpRls.length + 1];
				flRls[0] = i;
				for (int n = 0; n < tmpRls.length; n++) {
					flRls[n + 1] = tmpRls[n];
				}
				firstWdAtRules.put(firstWd, flRls);
			} else {
				firstWdAtRules.put(firstWd, new int[] { i });
			}
		}

		return firstWdAtRules;
	}

	private final int indexOf(int ruleIdx, int wdIdx, char[] source,
			int sourceCount, char[] target, int targetCount) {
		char first = target[0];
		int max = sourceCount - targetCount;

		for (int i = 0; i <= max; i++) {

			if (source[i] != first) {
				while (++i <= max && source[i] != first)
					;
			}

			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = 1; j < end && source[j] == target[k]; j++, k++)
					;

				if (j == end) {
					return i;
				}
			}
		}
		return -1;
	}

	private final int TailMatchStringNew(final int start, final char[] s2,
			final int len, int idx) {
		int index = start;

		if (_tail[index] == S_END_CHAR)
			return index;

		if (s2[idx++] != _tail[index++]) {
			return -1;
		}

		while (_tail[index] != S_END_CHAR) {
			if (s2[idx++] != _tail[index++]) {
				return -1;
			}
		}

		return index;
	}

	private final boolean deapCheck(final char[] chTiezi, final int len,
			final int endIdx) {

		int gpId = _tailRule[endIdx];
		if (!_bMultiGroup[gpId]) {
			return true;
		}

		int[] rules = _ruleGroup[gpId];
		for (int j = 0; j < rules.length; j++) {
			char[][] wds = _ruleWords[rules[j]];
			int ret = 0;
			for (int k = 1; k < wds.length; k++) {
				if (indexOf(rules[j], k, chTiezi, len, wds[k], wds[k].length) == -1) {
					ret = 1;
					break;
				}
			}
			if (ret == 0) {
				return true;
			}
		}

		return false;
	}

	private final static int RANGE = 100;
	private final static int STUDY_COST = 95;
	private final static int NUM_FILTER = 0;
	private final static int NUM_HUNDRED = 100;

	public int check(final char[] chTiezi, final int len) {
		int start = 0;
		int end = len - 1;
		if (exp_len > STUDY_COST) {
			start = _start;
			if (end > _end) {
				end = _end;
			}
		} else if (exp_len == STUDY_COST) {
			initWindows();
			start = _start;
			end = _end;
			exp_len++;
		} else {
			exp_len++;
		}

		return checkRan(chTiezi, start, end, len);
	}

	private int _start = 0;
	private int _end = 0;

	private final void initWindows() {
//		int total = 0;
//		for (int i = 0; i < findIdx.length; i++) {
//			total += findIdx[i];
//		}
		int flag = 0;
		int idx = 0;
		while (true) {
			if (flag == 0) {
				if (findIdx[idx++] > NUM_FILTER) {
					flag = 1;
					_start = NUM_HUNDRED * (idx - 1);
					idx = RANGE;
				}
			} else if (flag == 1) {
				if (findIdx[--idx] > NUM_FILTER) {
					flag = 2;
					_end = NUM_HUNDRED * idx;
				}
			} else {
				break;
			}
		}
	}

	private int[] findIdx = new int[RANGE];
	private int exp_len = 0;

	// boolean[] b = new boolean[2704000000];

	private final int checkRan(final char[] chTiezi, final int start,
			final int lenX, final int len) {

		label1: for (int i = start; i < lenX; i++) {
			final int ch2 = chTiezi[i + 1];
			int ch1;
			byte pos2 = _startCharPos[ch2];
			//不匹配前两个
			if(pos2==0){
				i++;
				continue;
			//只匹配第一个
			}else if(pos2==1){
				i = i + 1;
				ch1 = ch2;
			}else{
			    ch1 = chTiezi[i];
				if (!_allFirstChar[ch1]) {
					continue;
				}
			}
			
//			if (!_allAllSecondChar[ch2]) {
//				i++;
//				continue label1;
//			}
//
//			if (!_allSecondChar[ch2]) {
//				continue label1;
//			}
			

			int pre_p = 1;
			int cur_p = _base[1] + ch1;
			int idx = i;
			int basrpp = _base[cur_p];
			do {
				if (basrpp < 0) {

					int endIdx = TailMatchStringNew(-basrpp, chTiezi,
							len, idx + 1);
					if (endIdx != -1) {
						if (deapCheck(chTiezi, len, endIdx)) {
							findIdx[i / NUM_HUNDRED]++;
							return 1;
						}
					}
					continue label1;
				}

				pre_p = cur_p;
				idx++;

				ch1 = chTiezi[idx];
				cur_p = basrpp + ch1;
				basrpp = _base[cur_p];
				if (_check[cur_p] != pre_p) {
					continue label1;
				}

			} while (idx < len);
		}

		return 0;
	}

	private final class DoubleArrayTrie {
		final char END_CHAR = '\0';
		final int DEFAULT_LEN = 1024;
		public int[] Base = new int[DEFAULT_LEN];
		public int[] Check = new int[DEFAULT_LEN];
		public char[] Tail = new char[DEFAULT_LEN];
		public int[] Tail_Rule = new int[DEFAULT_LEN];
		public int[] Tail_Len = new int[DEFAULT_LEN];
		int Pos = 1;

		DoubleArrayTrie() {
			Base[1] = 1;

		}

		private void Extend_Array(int x) {
			Base = Arrays.copyOf(Base, Base.length + x * 2);
			Check = Arrays.copyOf(Check, Check.length + x * 2);
		}

		private void Extend_Tail(int x) {
			Tail = Arrays.copyOf(Tail, Tail.length + x * 2);
			Tail_Rule = Arrays.copyOf(Tail_Rule, Tail_Rule.length + x * 2);
			Tail_Len = Arrays.copyOf(Tail_Len, Tail_Len.length + x * 2);
		}

		private int CopyToTailArray(String s, int p, int ruleIndex) {
			int _Pos = Pos;
			while (s.length() - p + 1 > Tail.length - Pos) {
				Extend_Tail(s.length() - p + 1 - (Tail.length - Pos));
			}
			for (int i = p; i < s.length(); ++i) {
				Tail[_Pos] = s.charAt(i);
				_Pos++;
			}
			Tail_Rule[_Pos - 1] = ruleIndex;
			Tail_Len[Pos] = s.length() - p - 1;
			return _Pos;
		}

		private int x_check(Character[] set) {
			for (int i = 1;; ++i) {
				boolean flag = true;
				for (int j = 0; j < set.length; ++j) {
					int cur_p = i + set[j];
					if (cur_p >= Base.length)
						Extend_Array(cur_p - Base.length + 1);
					if (Base[cur_p] != 0 || Check[cur_p] != 0) {
						flag = false;
						break;
					}
				}
				if (flag)
					return i;
			}
		}

		private ArrayList<Character> GetChildList(int p) {
			ArrayList<Character> ret = new ArrayList<Character>();
			for (int i = 1; i <= 52000; ++i) {
				if (Base[p] + i >= Check.length)
					break;
				if (Check[Base[p] + i] == p) {
					ret.add((char) i);
				}
			}
			return ret;
		}

		private void Insert(String s, int ruleIndex) throws Exception {
			s += END_CHAR;
			int pre_p = 1;
			int cur_p;
			for (int i = 0; i < s.length(); ++i) {
				// 获取状态位置
				cur_p = Base[pre_p] + s.charAt(i);
				// 如果长度超过现有，拓展数组
				if (cur_p >= Base.length)
					Extend_Array(cur_p - Base.length);
				// 空闲状态
				if (Base[cur_p] == 0 && Check[cur_p] == 0) {
					Base[cur_p] = -Pos;
					Check[cur_p] = pre_p;
					Pos = CopyToTailArray(s, i + 1, ruleIndex);
					break;
				} else
				// 已存在状态
				if (Base[cur_p] > 0 && Check[cur_p] == pre_p) {
					pre_p = cur_p;
					continue;
				} else
				// 冲突 1：遇到 Base[cur_p]小于0的，即遇到一个被压缩存到Tail中的字符串
				if (Base[cur_p] < 0 && Check[cur_p] == pre_p) {
					int head = -Base[cur_p];
					if (s.charAt(i + 1) == END_CHAR && Tail[head] == END_CHAR) // 插入重复字符串
					{
						break;
					}
					// 公共字母的情况，因为上一个判断已经排除了结束符，所以一定是2个都不是结束符
					if (Tail[head] == s.charAt(i + 1)) {
						int avail_base = x_check(new Character[] { s
								.charAt(i + 1) });
						Base[cur_p] = avail_base;
						Check[avail_base + s.charAt(i + 1)] = cur_p;
						Base[avail_base + s.charAt(i + 1)] = -(head + 1);
						pre_p = cur_p;
						continue;
					} else {
						// 2个字母不相同的情况，可能有一个为结束符
						int avail_base;
						avail_base = x_check(new Character[] { s.charAt(i + 1),
								Tail[head] });
						Base[cur_p] = avail_base;
						Check[avail_base + Tail[head]] = cur_p;
						Check[avail_base + s.charAt(i + 1)] = cur_p;
						// Tail 为END_FLAG 的情况
						if (Tail[head] == END_CHAR)
							Base[avail_base + Tail[head]] = 0;
						else
							Base[avail_base + Tail[head]] = -(head + 1);
						if (s.charAt(i + 1) == END_CHAR)
							Base[avail_base + s.charAt(i + 1)] = 0;
						else
							Base[avail_base + s.charAt(i + 1)] = -Pos;
						Pos = CopyToTailArray(s, i + 2, ruleIndex);
						break;
					}
				} else // 冲突2：当前结点已经被占用，需要调整pre的base
				if (Check[cur_p] != pre_p) {
					ArrayList<Character> list1 = GetChildList(pre_p);
					int toBeAdjust;
					ArrayList<Character> list = null;
					if (true) {
						toBeAdjust = pre_p;
						list = list1;
					}
					int origin_base = Base[toBeAdjust];
					list.add(s.charAt(i));
					int avail_base = x_check((Character[]) list
							.toArray(new Character[list.size()]));
					list.remove(list.size() - 1);
					Base[toBeAdjust] = avail_base;
					for (int j = 0; j < list.size(); ++j) { // BUG
						int tmp1 = origin_base + list.get(j);
						int tmp2 = avail_base + list.get(j);
						Base[tmp2] = Base[tmp1];
						Check[tmp2] = Check[tmp1]; // 有后续
						if (Base[tmp1] > 0) {
							ArrayList<Character> subsequence = GetChildList(tmp1);
							for (int k = 0; k < subsequence.size(); ++k) {
								Check[Base[tmp1] + subsequence.get(k)] = tmp2;
							}
						}
						Base[tmp1] = 0;
						Check[tmp1] = 0;
					}
					// 更新新的cur_p
					cur_p = Base[pre_p] + s.charAt(i);
					if (s.charAt(i) == END_CHAR)
						Base[cur_p] = 0;
					else
						Base[cur_p] = -Pos;
					Check[cur_p] = pre_p;
					Pos = CopyToTailArray(s, i + 1, ruleIndex);
					break;
				}
			}
		}

	}

	int tail_len = 0;
}
