package com.codepath.gridimagesearch;

import java.io.Serializable;

public class GoogleImageSearchFilter implements Serializable {
	/**
	 * serialize a filter object
	 */
	private static final long serialVersionUID = -6332917045205607409L;

	private String type, color, size, site;

	public GoogleImageSearchFilter() {
		this.type = "none";
		this.color = "none";
		this.size = "none";
		this.site = "";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

}
