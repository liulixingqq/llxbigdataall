package demo.mr.ser.java;

import java.io.Serializable;

public class Student implements Serializable {

	//属性
	private int stuID;
	private String stuName;
	
	public int getStuID() {
		return stuID;
	}
	public void setStuID(int stuID) {
		this.stuID = stuID;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
}
