package com.inn.cafe.wrapper;

public class CafeUserWrapper {
	private long id;
	private String name;
	private String contactNumber;
	private String email;
	private String status;
	private String role;

	@Override
	public String toString() {
		return "CafeUserWrapper [id=" + id + ", name=" + name + ", contactNumber=" + contactNumber + ", email=" + email
				+ ", status=" + status + ", role=" + role + "]";
	}

	public CafeUserWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CafeUserWrapper(long id, String name, String contactNumber, String email, String status, String role) {
		super();
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
		this.email = email;
		this.status = status;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
