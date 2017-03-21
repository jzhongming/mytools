/**
 * Copyright (C) 2011 Asterios Raptis
 *
 * This program is open source software; you can redistribute it and/or modify
 * it under the terms of the Apache License V2.0 as published by
 * the Apache Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 */
package com.github.jzhongming.mytools.utils.geohash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GeoHashUtils is based on http://en.wikipedia.org/wiki/Geohash
 * 
 */
public class GeoHashUtils {

	/**
	 * The Constant char map BASE_32.
	 * */
	private final static char[] BASE_32 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	/** The Constant DECODE_MAP. */
	private final static Map<Character, Integer> DECODE_MAP = new HashMap<Character, Integer>();
	static {
		int counter = 0;
		for (char c : BASE_32) {
			DECODE_MAP.put(c, counter++);
		}
	}

	/** The precision. */
	private static int precision = 12;

	/** The bits. */
	private static int[] bits = { 16, 8, 4, 2, 1 };

	/**
	 * Gets the adjacent from the given direction. For instance if direction is
	 * 'top' then the top adjacent geohash code will be returned.
	 * 
	 * @param geohash
	 *            the geohash
	 * @param direction
	 *            the direction
	 * @return the geohash code for the adjacent from the given direction.
	 */
	public static String getAdjacent(final String geohash, final String direction) {
		if (geohash == null || geohash.isEmpty()) {
			throw new IllegalArgumentException("Argument geohash should not be null or empty.");
		}
		if (direction == null || direction.isEmpty()) {
			throw new IllegalArgumentException("Argument direction should not be null or empty.");
		}
		String lowerCaseGeohash = geohash.toLowerCase();
		char lastChar = lowerCaseGeohash.charAt(lowerCaseGeohash.length() - 1);
		String type = (lowerCaseGeohash.length() % 2) == 0 ? Adjacent.ODD : Adjacent.EVEN;
		String base = lowerCaseGeohash.substring(0, lowerCaseGeohash.length() - 1);
		if (Adjacent.Borders.borders.get(direction).get(type).indexOf(lastChar) != -1) {
			base = getAdjacent(base, direction);
		}
		int i = Adjacent.Neighbors.neighbors.get(direction).get(type).indexOf(lastChar);
		char r = BASE_32[i];
		return base + r;
	}

	/**
	 * Decode the given geohash into a latitude and longitude.
	 * 
	 * @param geohash
	 *            the geohash
	 * @return the double[]
	 */
	public static double[] decode(final String geohash) {
		if (geohash == null || geohash.isEmpty()) {
			throw new IllegalArgumentException("Argument geohash should not be null.");
		}

		boolean even = true;
		double latitudeError = 90.0;
		double longitudeError = 180.0;
		double latitude;
		double longitude;
		double[] latitudeInterval = { -90.0, 90.0 };
		double[] longitudeInterval = { -180.0, 180.0 };
		for (int i = 0; i < geohash.length(); i++) {

			int cd = DECODE_MAP.get(geohash.charAt(i));

			for (int j = 0; j < bits.length; j++) {
				int mask = bits[j];
				if (even) {
					longitudeError /= 2;
					if ((cd & mask) != 0) {
						longitudeInterval[0] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
					} else {
						longitudeInterval[1] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
					}

				} else {
					latitudeError /= 2;

					if ((cd & mask) != 0) {
						latitudeInterval[0] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
					} else {
						latitudeInterval[1] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
					}
				}

				even = !even;
			}

		}
		latitude = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
		longitude = (longitudeInterval[0] + longitudeInterval[1]) / 2D;

		return new double[] { latitude, longitude, latitudeError, longitudeError };
	}

	/**
	 * Decodes the given geohash into a latitude and longitude.
	 * 
	 * @param geohash
	 *            the geohash
	 * @return the double[]
	 */
	public static double[] decodeAndRound(final String geohash) {
		double[] ge = decode(geohash);
		double latitude = ge[0];
		double longitude = ge[1];
		double latitudeError = ge[2];
		double longitudeError = ge[3];

		double latitudePrecision = Math.max(1, Math.round(-Math.log10(latitudeError))) - 1;
		double longitudePrecision = Math.max(1, Math.round(-Math.log10(longitudeError))) - 1;

		latitude = getPrecision(latitude, latitudePrecision);
		longitude = getPrecision(longitude, longitudePrecision);

		return new double[] { latitude, longitude };
	}

	/**
	 * Encodes the given latitude and longitude into a geohash code.
	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @return The generated geohash from the given latitude and longitude.
	 */
	public static String encode(final double latitude, final double longitude) {
		StringBuffer geohash = new StringBuffer();
		boolean even = true;
		int bit = 0;
		int ch = 0;

		double[] latitudeInterval = { -90.0, 90.0 };
		double[] longitudeInterval = { -180.0, 180.0 };

		while (geohash.length() < precision) {
			double mid = 0.0;
			if (even) {
				mid = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
				if (longitude > mid) {
					ch |= bits[bit];
					longitudeInterval[0] = mid;
				} else {
					longitudeInterval[1] = mid;
				}

			} else {
				mid = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
				if (latitude > mid) {
					ch |= bits[bit];
					latitudeInterval[0] = mid;
				} else {
					latitudeInterval[1] = mid;
				}
			}

			even = !even;

			if (bit < 4) {
				bit++;
			} else {
				geohash.append(BASE_32[ch]);
				bit = 0;
				ch = 0;
			}
		}

		return geohash.toString();
	}

	/**
	 * Gets the precision.
	 * 
	 * @param x
	 *            the x
	 * @param precision
	 *            the precision
	 * @return the precision
	 */
	private static double getPrecision(final double x, final double precision) {
		double base = Math.pow(10, -precision);
		double diff = x % base;
		return x - diff;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		String alterTeichwegGeohash = "u1x0v54rmjwej";
		String gc1 = GeoHashUtils.encode(30.0, -90.0);
		String gc2 = GeoHashUtils.encode(51.4797, -0.0124);
		String geohash = GeoHashUtils.encode(53.5526394, 10.0067103);
		System.out.println("geohash:" + geohash);

		System.out.println("gc1:" + gc1);
		System.out.println("gc2:" + gc2);

		double[] gd1 = GeoHashUtils.decodeAndRound(gc1);
		double[] gd2 = GeoHashUtils.decodeAndRound(gc2);

		System.out.println(gd1[0] + ", " + gd1[1]);
		System.out.println(gd2[0] + ", " + gd2[1]);

		gd1 = GeoHashUtils.decode(gc1);
		gd2 = GeoHashUtils.decode(gc2);
		double[] decoded = GeoHashUtils.decode(geohash);
		System.out.println(gd1[0] + ", " + gd1[1]);
		System.out.println(gd2[0] + ", " + gd2[1]);
		System.out.println(decoded[0] + ", " + decoded[1]);

		String geohashTest = alterTeichwegGeohash;

		String top = GeoHashUtils.getAdjacent(geohashTest, "top");
		System.out.println("top:\t\t" + top);
		String bottom = GeoHashUtils.getAdjacent(geohashTest, "bottom");
		System.out.println("bottom:\t\t" + bottom);
		String right = GeoHashUtils.getAdjacent(geohashTest, "right");
		System.out.println("right:\t\t" + right);
		String left = GeoHashUtils.getAdjacent(geohashTest, "left");
		System.out.println("left:\t\t" + left);
		String topleft = GeoHashUtils.getAdjacent(left, "top");
		System.out.println("topleft:\t" + topleft);
		String topright = GeoHashUtils.getAdjacent(right, "top");
		System.out.println("topright:\t" + topright);
		String bottomright = GeoHashUtils.getAdjacent(right, "bottom");
		System.out.println("bottomright:\t" + bottomright);
		String bottomleft = GeoHashUtils.getAdjacent(left, "bottom");
		System.out.println("bottomleft:\t" + bottomleft);

		String subGeohash = geohash.substring(0, 7);
		Map<String, String> adjacentAreas = GeoHashUtils.getAllAdjacentAreasMap(subGeohash);
		System.out.println(adjacentAreas);
		System.out.println("=======================================");
		List<String> aa = GeoHashUtils.getAllAdjacentAreasList(subGeohash);
		System.out.println(aa);
		System.out.println("=======================================");
		GeoHashPoint geohashPoint1 = new GeoHashPoint(geohash);
		GeoHashPoint geohashPoint2 = new GeoHashPoint(geohash);
		System.out.println(geohashPoint1);
		System.out.println(geohashPoint2);
		int c = geohashPoint1.compareTo(geohashPoint2);
		System.out.println(c);

		double[] dd = decode("u008nb00j8nb");
		System.out.println(String.format("%f,%f", dd[0], dd[1]));
	}

	/**
	 * Gets the all adjacent areas map.
	 * 
	 * @param geohash
	 *            the geohash
	 * @return the all adjacent areas map
	 */
	public static Map<String, String> getAllAdjacentAreasMap(final String geohash) {
		if (geohash == null || geohash.isEmpty()) {
			throw new IllegalArgumentException("Argument geohash should not be null.");
		}

		String top = GeoHashUtils.getAdjacent(geohash, Adjacent.TOP);
		String bottom = GeoHashUtils.getAdjacent(geohash, Adjacent.BOTTOM);
		String right = GeoHashUtils.getAdjacent(geohash, Adjacent.RIGHT);
		String left = GeoHashUtils.getAdjacent(geohash, Adjacent.LEFT);
		String topleft = GeoHashUtils.getAdjacent(left, Adjacent.TOP);
		String topright = GeoHashUtils.getAdjacent(right, Adjacent.TOP);
		String bottomright = GeoHashUtils.getAdjacent(right, Adjacent.BOTTOM);
		String bottomleft = GeoHashUtils.getAdjacent(left, Adjacent.BOTTOM);

		Map<String, String> adjacentAreas = new LinkedHashMap<String, String>();
		adjacentAreas.put(Adjacent.CENTER, geohash);
		adjacentAreas.put(Adjacent.TOP, top);
		adjacentAreas.put(Adjacent.BOTTOM, bottom);
		adjacentAreas.put(Adjacent.RIGHT, right);
		adjacentAreas.put(Adjacent.LEFT, left);
		adjacentAreas.put(Adjacent.TOP_LEFT, topleft);
		adjacentAreas.put(Adjacent.TOP_RIGHT, topright);
		adjacentAreas.put(Adjacent.BOTTOM_RIGHT, bottomright);
		adjacentAreas.put(Adjacent.BOTTOM_LEFT, bottomleft);

		return adjacentAreas;
	}

	/**
	 * Gets the all adjacent areas list.
	 * 
	 * @param geohash
	 *            the geohash
	 * @return the all adjacent areas list
	 */
	public static List<String> getAllAdjacentAreasList(final String geohash) {
		if (geohash == null || geohash.isEmpty()) {
			throw new IllegalArgumentException("Argument geohash should not be null.");
		}

		List<String> adjacentAreas = new ArrayList<String>();
		String top = GeoHashUtils.getAdjacent(geohash, Adjacent.TOP);
		String bottom = GeoHashUtils.getAdjacent(geohash, Adjacent.BOTTOM);
		String right = GeoHashUtils.getAdjacent(geohash, Adjacent.RIGHT);
		String left = GeoHashUtils.getAdjacent(geohash, Adjacent.LEFT);
		String topleft = GeoHashUtils.getAdjacent(left, Adjacent.TOP);
		String topright = GeoHashUtils.getAdjacent(right, Adjacent.TOP);
		String bottomright = GeoHashUtils.getAdjacent(right, Adjacent.BOTTOM);
		String bottomleft = GeoHashUtils.getAdjacent(left, Adjacent.BOTTOM);
		adjacentAreas.add(geohash);
		adjacentAreas.add(top);
		adjacentAreas.add(topright);
		adjacentAreas.add(right);
		adjacentAreas.add(bottomright);
		adjacentAreas.add(bottom);
		adjacentAreas.add(bottomleft);
		adjacentAreas.add(left);
		adjacentAreas.add(topleft);
		return adjacentAreas;
	}

}
