package com.newhope.nlbp.core.report.example;

import java.util.Date;

public class Student {
	private int id;
	private String name;
	private int age;
	private Date birth;
	
	private int classId;
	private String className;

	public Student()
	{
	}

	public Student(int id, String name, int age, Date birth, String className)
	{
		this.id = id;
		this.name = name;
		this.age = age;
		this.birth = birth;
		this.className = className;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public Date getBirth()
	{
		return birth;
	}

	public void setBirth(Date birth)
	{
		this.birth = birth;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
