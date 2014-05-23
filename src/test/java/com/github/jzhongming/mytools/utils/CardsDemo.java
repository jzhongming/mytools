package com.github.jzhongming.mytools.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	姜中明	E-mail: jiangzhongming@58.com
 * @version	Created ：2013-10-14 下午03:41:34
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public class CardsDemo {
	static class Card {
		public static final String[] colors = new String[]{"黑桃","红桃","梅花","方块","王"};
		public static final String[] ranks = new String[]{"小","大","3","4","5","6","7","8","9","10","J","Q","K","A","2"};

		public static final int SPADE = 1;
		public static final int HEART = 2;
		public static final int CLUBS = 3;
		public static final int DIAMOND = 4;
		public static final int JOKER = 5;
		public static final int BLACK = 1;
		public static final int COLOR = 2;
		public static final int THREE = 3;
		public static final int FOUR = 4;
		public static final int FIVE = 5;
		public static final int SIX = 6;
		public static final int SEVEN = 7;
		public static final int EIGHT = 8;
		public static final int NINE = 9;
		public static final int TEN = 10;
		public static final int JACK = 11;
		public static final int QUEEN = 12;
		public static final int KING = 13;
		public static final int ACE = 14;
		public static final int DUECE = 15;

		/* 卡牌的花色 */
		public int color;
		/* 卡牌的大小 */
		public int rank;

		public Card(int color) {
			this.color = color;
		}

		public Card(int color, int rank) {
			this.color = color;
			this.rank = rank;
		}

		public boolean equals(Object obj) {
			boolean flag = false;
			if (obj != null && obj instanceof Card) {
				Card temp = (Card) obj;
				if ((color == temp.color) && (rank == temp.rank))
					flag = true;
			}
			return flag;
		}

		public String toString() {
			if (color == 5)
				return ranks[rank - 1] + colors[color - 1];
			else
				return colors[color - 1] + ranks[rank - 1];
		}

	}
	
	static class Player {
		private String name;
		private List<Card> cards;
		
		public Player(String name) {
			this.name = name;
		}

		public void getCard(Card card) {
			if(cards == null)
				cards = new ArrayList<Card>();
			cards.add(card);
		}
		
		public String toString() {
			return name + "->" + cards.toString();
		}
	}

	/**
	 * 洗牌问题，模拟洗牌，修正概率问题
	 * @param array
	 */
	public static void Shuffle(List<Card> array) {
		int N = array.size() - 1;
		
		for (int i = 0; i <= N; i++) {
			swap(array, (N - i), (int) (Math.random() * (N - i + 1)));// 修正概率问题
		}
	}
	
	private static void swap(List<Card> array, int i, int j) {
		if (i == j)
			return;
		
		Card tmp = array.get(i);
		array.set(i, array.get(j));
		array.set(j, tmp);
	}
	
	public static void main(String[] args) {
		// 1.创建一副卡牌和玩家
		// 2.打乱卡牌
		// 3.发送给玩家
		// 4.打印各个玩家手上牌信息

		List<Card> cards = createPoker();
		List<Player> players = createPlayer();
		System.out.println(cards.toString());
//		Collections.shuffle(cards);
		CardsDemo.Shuffle(cards);
		System.out.println(cards.toString());
		deal(players, cards);
		for (int i = 0; i < players.size(); i++)
			System.out.println(players.get(i));
		System.out.println("底下3张牌->" + cards);
	}

	/*
	 * 创建一副卡牌
	 */
	public static List<Card> createPoker() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Card.JOKER, Card.BLACK));
		cards.add(new Card(Card.JOKER, Card.COLOR));
		for (int rank = Card.THREE; rank <= Card.DUECE; rank++) {
			cards.add(new Card(Card.SPADE, rank));
			cards.add(new Card(Card.HEART, rank));
			cards.add(new Card(Card.CLUBS, rank));
			cards.add(new Card(Card.DIAMOND, rank));
		}
		return cards;
	}

	/*
	 * 创建3个玩家
	 */
	public static List<Player> createPlayer() {
		List<Player> players = new ArrayList<Player>();
		players.add(new Player("刘备"));
		players.add(new Player("关羽"));
		players.add(new Player("张飞"));
		return players;
	}

	/*
	 * 发送给玩家
	 */
	public static void deal(List<Player> players, List<Card> cards) {
		Iterator<Card> iter = cards.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Card c = iter.next();
			players.get(i++ % 3).getCard(c);
			iter.remove(); // 从cards集合中删除刚刚从迭代器迭代过的牌（刚发的牌）
			if (cards.size() == 3)
				break;
		}
	}
		

}
