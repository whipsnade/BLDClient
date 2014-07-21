package com.bld.object;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */

public class Product implements java.io.Serializable {

	// Fields

	private String id;
	private String classId;
	private String name;
	private String code;
	private double price;
	private String img;
	private String description;
	private int num;

	// Constructors

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	/** default constructor */
	public Product() {
	}

	/** minimal constructor */
	public Product(String id) {
		this.id = id;
	}

	/** full constructor */
	public Product(String id, String classId, String name, String code,
			double price, String img, String description,int num) {
		this.id = id;
		this.classId = classId;
		this.name = name;
		this.code = code;
		this.price = price;
		this.img = img;
		this.description = description;
		this.num=num;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassId() {
		return this.classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}