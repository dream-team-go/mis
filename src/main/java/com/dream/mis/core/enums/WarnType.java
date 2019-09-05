package com.dream.mis.core.enums;
/**
 * 消息提醒类型
 * @author by
 *
 */
public enum WarnType {
	LOG("重要日志提醒",1);
	//成员变量
	private int index;
	private String name;
	//构造方法
	private WarnType(String name,int index) {
		this.index=index;
		this.name=name;
	}
	//根据index 获取 name
	private String getName(int index) {
		for(WarnType i:WarnType.values() ) {
			if(i.index==index) {
				return i.name;
			}
		}
		return null;
	}
	//get set 方法
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
