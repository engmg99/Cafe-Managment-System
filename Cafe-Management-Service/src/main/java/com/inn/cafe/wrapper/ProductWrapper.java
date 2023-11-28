package com.inn.cafe.wrapper;

public class ProductWrapper {
	Long id;
	String name;
	String desc;
	Long price;
	String status;
	Long category_id;
	String categoryName;

	public ProductWrapper(Long id, String name, String desc, Long price, String status, Long category_id,
			String categoryName) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.status = status;
		this.category_id = category_id;
		this.categoryName = categoryName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
