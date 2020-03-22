package com.easychat;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class User implements Serializable {
	private String id;
	private String nick;
	private String password;
	private String introduce;
	private Date signupTime;
	public User() {
		super();
	}
	public User(String id, String nick, String password, String introduce, Date signUpTime) {
		super();
		this.id = id;
		this.nick = nick;
		this.password = password;
		this.introduce = introduce;
		signupTime = signUpTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Date getSignUpTime() {
		return signupTime;
	}
	public void setSignUpTime(Date signUpTime) {
		signupTime = signUpTime;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", nick=" + nick + ", password=" + password + ", introduce=" + introduce
				+ ", SignUpTime=" + signupTime + ", friends=" + "]";
	}
	
}
