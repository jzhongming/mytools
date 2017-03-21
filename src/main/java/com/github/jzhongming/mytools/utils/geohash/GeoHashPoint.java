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

import java.math.BigDecimal;

/**
 * The Class GeoHashPoint.
 */
public class GeoHashPoint extends Point {

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 6426300356817687239L;

	/** The Constant GEOHASH_KEY. */
	public static final String GEOHASH_KEY = "GEOHASH_KEY";

	/**
	 * Instantiates a new geo hash point.
	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 */
	public GeoHashPoint(final double latitude, final double longitude) {
		super(latitude, longitude);
	}

	/**
	 * Instantiates a new geo hash point.
	 * 
	 * @param geohash
	 *            the Geohash code.
	 */
	public GeoHashPoint(String geohash) {
		super(GeoHashUtils.decode(geohash)[0], GeoHashUtils.decode(geohash)[1]);
	}

	/**
	 * Gets the geohash.
	 * 
	 * @return the geohash
	 */
	public String getGeohash() {
		return GeoHashUtils.encode(getLatitude(), getLongitude());
	}

	/**
	 * Gets the lat.
	 * 
	 * @return the lat
	 */
	public BigDecimal getLat() {
		return BigDecimal.valueOf(getLatitude());
	}

	/**
	 * Gets the lng.
	 * 
	 * @return the lng
	 */
	public BigDecimal getLng() {
		return BigDecimal.valueOf(getLongitude());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.sem.base.utils.geocoding.Point#clone()
	 */
	public Object clone() {
		return new GeoHashPoint(getLatitude(), getLongitude());
	}

	/**
	 * Returns <code>true</code> if this <code>GeoHashPoint</code> is the same
	 * as the o argument.
	 * 
	 * @param o
	 *            the o
	 * @return <code>true</code> if this <code>GeoHashPoint</code> is the same
	 *         as the o argument.
	 */
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		GeoHashPoint castedObj = (GeoHashPoint) o;
		return ((this.getLatitude() == castedObj.getLatitude()) && (this.getLongitude() == castedObj.getLongitude()));
	}

	/**
	 * Override hashCode.
	 * 
	 * @return the Objects hashcode.
	 */
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + (int) (+serialVersionUID ^ (serialVersionUID >>> 32));
		hashCode = 31 * hashCode + (GEOHASH_KEY == null ? 0 : GEOHASH_KEY.hashCode());
		return hashCode;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.sem.base.utils.geocoding.Point#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[GeoHashPoint:");
		buffer.append(super.toString());
		buffer.append("   Geohash : ");
		buffer.append(getGeohash());
		buffer.append("]");
		return buffer.toString();
	}

}
