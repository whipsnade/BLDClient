package com.bld.object;

/**
 * Suggest entity. @author MyEclipse Persistence Tools
 */

public class Suggest implements java.io.Serializable {

	// Fields

	private String id;
	private String imgPath;
	private String title;

	// Constructors

	/** default constructor */
	public Suggest() {
	}

	/** minimal constructor */
	public Suggest(String id) {
		this.id = id;
	}

	/** full constructor */
	public Suggest(String id, String imgPath, String title) {
		this.id = id;
		this.imgPath = imgPath;
		this.title = title;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}