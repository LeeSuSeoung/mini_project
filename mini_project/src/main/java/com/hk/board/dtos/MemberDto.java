package com.hk.board.dtos;

import org.apache.ibatis.type.Alias;

@Alias(value = "memberDto")
public class MemberDto {
	
	private int memberId;
	private String id;
	private String name;
	private String password;
	private String address;
	private String email;
	private String role;
	public MemberDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MemberDto(int memberId, String id, String name, String password, String address, String email, String role) {
		super();
		this.memberId = memberId;
		this.id = id;
		this.name = name;
		this.password = password;
		this.address = address;
		this.email = email;
		this.role = role;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "MemberDto [memberId=" + memberId + ", id=" + id + ", name=" + name + ", password=" + password
				+ ", address=" + address + ", email=" + email + ", role=" + role + "]";
	}
	
}




