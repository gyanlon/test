package com.newhope.nlbp.core.report.example;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import com.newhope.nlbp.core.report.excel.ExcelHeadNode;
import com.newhope.nlbp.core.report.excel.ReportExcelManager;

public class CreateSimpleExcelToDisk {
	/**
	 * @功能：手工构建一个简单格式的Excel
	 */
	private static List<Student> getStudent() throws Exception
	{
		List list = new ArrayList();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");

		Student user1 = new Student(1, "张三", 16, df.parse("1997-03-12"), "一班");
		Student user2 = new Student(2, "李四", 17, df.parse("1996-08-12"), "一班");
		Student user3 = new Student(3, "王五", 26, df.parse("1985-11-12"), "二班");
		list.add(user1);
		list.add(user2);
		list.add(user3);

		return list;
	}

	public static void main(String[] args) throws Exception
	{
		
		ReportExcelManager reportExcelManager = ReportExcelManager.getInstance();
		
		
		//模拟数据
		ExcelHeadNode headNode = generentStudentHead();
		reportExcelManager.clear();
		reportExcelManager.push(getStudent(), headNode);
		reportExcelManager.generateReport(new FileOutputStream("C:/students.xls"), "");
		reportExcelManager.clear();
	}
	
	public static ExcelHeadNode generateTestData(){
		ExcelHeadNode excelHeadNode = new ExcelHeadNode();
		excelHeadNode.setValue("表头");
		
		for(int i=0; i<4; i++){
			ExcelHeadNode childNode = new ExcelHeadNode();
			childNode.setKey(i+"");
			childNode.setValue("第二层:" + i);
			
			excelHeadNode.getChildNodes().add(childNode);
			for(int j=0; j<2; j++){
				ExcelHeadNode secondChildNode = new ExcelHeadNode();
				secondChildNode.setKey(j+"");
				secondChildNode.setValue("第三层:" + j);
				
				childNode.getChildNodes().add(secondChildNode);
			}
		}
		
		
		return excelHeadNode;
	}
	
	public static ExcelHeadNode generentStudentHead(){
		ExcelHeadNode excelHeadNode = new ExcelHeadNode();
		excelHeadNode.setValue("学生表头");
	
		ExcelHeadNode excelHeadNode2 = new ExcelHeadNode();
		excelHeadNode2.setKey("id");
		excelHeadNode2.setValue("学号");
		
		ExcelHeadNode excelHeadNode3 = new ExcelHeadNode();
		excelHeadNode3.setKey("name");
		excelHeadNode3.setValue("姓名");
		
		ExcelHeadNode excelHeadNode4 = new ExcelHeadNode();
		excelHeadNode4.setKey("birth");
		excelHeadNode4.setValue("生日");		
		
		ExcelHeadNode excelHeadNode5 = new ExcelHeadNode();
		excelHeadNode5.setKey("className");
		excelHeadNode5.setValue("班级名称");				
		
		excelHeadNode.getChildNodes().add(excelHeadNode2);
		excelHeadNode.getChildNodes().add(excelHeadNode3);
		excelHeadNode.getChildNodes().add(excelHeadNode4);
		excelHeadNode.getChildNodes().add(excelHeadNode5);
		
		return excelHeadNode;
	}	
	
	
	public static List<Student> generentStudent(){
		List<Student> data = new ArrayList<Student>();
		
		for(int i=1; i<4; i++){//班级
			
			for(int j=1; j<10; j++){//学生
				Student s = new Student();
				s.setClassId(i);
				s.setClassName("第" + i +  "班");
				
				s.setId(j);
				s.setAge(j);
				s.setName("学生i=" + i + "  j=" + j);
				s.setBirth(new Date());
				data.add(s);
			}
		}
		
		return data;
	}
}
