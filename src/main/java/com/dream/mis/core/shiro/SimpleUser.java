package com.dream.mis.core.shiro;

import java.io.Serializable;

import com.dream.mis.core.web.model.UserV;


/**  
 * @ClassName: SimpleUser  
 * @Description: 简单用户对象，用于在Session中保存用户对象
 */

public class SimpleUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String id;
	private final String username;
	private String roleName;
	private String userCnName;
	public String head_img;
	public UserV user;
	public SimpleUser(String id, String username, String roleName,String userCnName,String head_img, UserV user) {
		super();
		this.id = id;
		this.username = username;
		this.roleName = roleName;
		this.userCnName = userCnName;
		this.head_img=head_img;
		this.user = user;
	}

	public UserV getUser() {
		return user;
	}

	public void setUser(UserV user) {
		this.user = user;
	}

	public final String getId() {
		return id;
	}
	/**
	 * @Title: getUsername  
	 * @Description: 获得用户名 
	 * @return 
	 * @since V1.0.0
	 */
	public final String getUsername() {
		return username;
	}
	/**
	 * @Title: getRoleName  
	 * @Description: 获得角色名称 
	 * @return 
	 * @since V1.0.0
	 */
	public final String getRoleName() {
		return roleName;
	}
	/**
	 * @Title: setRoleName  
	 * @Description: 设定角色名称  
	 * @param roleName 
	 * @since V1.0.0
	 */
	public final void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserCnName() {
		return userCnName;
	}

	public void setUserCnName(String userCnName) {
		this.userCnName = userCnName;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	
	
}
