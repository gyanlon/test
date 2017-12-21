package com.newhope.nlbp.core.report.excel;

import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.newhope.commons.lang.DateUtils;
@Service
@Scope("prototype")
public class ReportExcelManager {
	
	public ReportExcelManager(){
		init();
	}
	
	//sheet
	private List<Sheet> sheets = new ArrayList<Sheet>();
	
	// 第一步，创建一个webbook，对应一个Excel文件
	private HSSFWorkbook wb = new HSSFWorkbook();
	// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	
	private HSSFCellStyle leftStyle;
	private HSSFCellStyle middelStyle;
	private HSSFCellStyle rightStyle;
	private HSSFCellStyle defaultHeadStyle; //表名
	private HSSFCellStyle defaultHeadCloumnStyle; //头列加粗
	
	//数据格式style
	private HSSFCellStyle cellStyleIntegerZero;
	private HSSFCellStyle cellStyleDoubleZero;

	
	private void init(){
		
		defaultHeadStyle = wb.createCellStyle();
		
		//字体
		Font font = wb.createFont();
	    font.setFontName("宋体");//字体类型
	    font.setFontHeightInPoints((short) 20);//字体大小
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
		
		defaultHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		 //一、设置背景色：

		defaultHeadStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
		defaultHeadStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		// 二、设置边框:

		defaultHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		defaultHeadStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		defaultHeadStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		defaultHeadStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	    defaultHeadStyle.setFont(font);
	    
	    
		defaultHeadCloumnStyle = wb.createCellStyle();
		//字体
		Font fontHeadCloumnStyle = wb.createFont();
	    fontHeadCloumnStyle.setFontName("宋体");//字体类型
	    fontHeadCloumnStyle.setFontHeightInPoints((short) 11);//字体大小
	    fontHeadCloumnStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
		
		defaultHeadCloumnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		 //一、设置背景色：

		defaultHeadCloumnStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
		defaultHeadCloumnStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		// 二、设置边框:

		defaultHeadCloumnStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		defaultHeadCloumnStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		defaultHeadCloumnStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		defaultHeadCloumnStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

	    defaultHeadCloumnStyle.setFont(fontHeadCloumnStyle);	    
	    
	    //数据格式style
		cellStyleIntegerZero = wb.createCellStyle();  
		cellStyleIntegerZero.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		cellStyleDoubleZero = wb.createCellStyle(); 
		cellStyleDoubleZero.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static ReportExcelManager getInstance(){
		return new ReportExcelManager();
	}
	
	/**
	 * 清空报表数据
	 */
	public void clear(){
		wb = new HSSFWorkbook();
		sheets.clear();
		init();
	}
	
	public <T> void push(List<T> rowData, ExcelHeadNode headNode){
		
		//初始化sheet
		HSSFSheet sheet = wb.createSheet(headNode.getValue());
		sheets.add(sheet);
		Map<Integer, List<ExcelHeadNode>> levelMap = this.calcHead(sheet, headNode);
		if(levelMap.keySet().size()>0
				&& rowData != null){
			this.constructureCellRowData(sheet, levelMap, rowData);
		}
		
	}
	
	/**
	 * 生成报表
	 * @param <HttpServletResponse>
	 */
	public void generateReport(OutputStream outputStream, String fileName){
		try
		{
			if(outputStream == null){
				outputStream = new FileOutputStream("C:/" + fileName + ".xls");
			}
			wb.write(outputStream);
			outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void generateReport(HttpServletResponse  response, String fileName){
		try
		{
			
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
	        // 传送给浏览器utf-8编码的文字
	        response.setContentType("text/html;charset=utf-8");
	        //请求字符编码，如果有中文才不会乱码
	        response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
			OutputStream outputStream = response.getOutputStream();
			if(outputStream == null){
				outputStream = new FileOutputStream("C:/" + fileName+".xls");
			}
			wb.write(outputStream);
			outputStream.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		this.clear();
	}	
	
	private Map<Integer, List<ExcelHeadNode>> calcHead(Sheet sheet, ExcelHeadNode headNode){
		
		//组合层级
		Map<Integer, List<ExcelHeadNode>> levelMap = new HashMap<Integer, List<ExcelHeadNode>>();
		Integer currentLevel = 0;
		
		//组建表名
		List<ExcelHeadNode> headList = new ArrayList<ExcelHeadNode>();
		headList.add(headNode);
		levelMap.put(currentLevel, headList);
		
		boolean dataIntegrity = this.calcLevels(currentLevel, headNode, levelMap);
		
		if(dataIntegrity){
			for(int i=0; i<levelMap.keySet().size(); i++){
				//----------------------第几层-------------------------------------
				List<ExcelHeadNode> levelModels = levelMap.get(i);
				//创建行
				HSSFRow row = (HSSFRow) sheet.createRow(i);
				
				int columnIndex = 0;
				for(ExcelHeadNode column : levelModels){
					
					HSSFCell cell=null;
					
					int startCol = columnIndex;
					int endCol = 0;
					
					if(startCol == 0){
						endCol = column.getCellColSpan()-1;
					}else{
						endCol = startCol + column.getCellColSpan()-1;
					}
					
					if(column.getCellColSpan()>1){
						CellRangeAddress cellAddress = new CellRangeAddress(i, i, startCol,endCol);// row开始， row结束， column开始， column结束
						sheet.addMergedRegion(cellAddress);
					}
					
					for(int x=startCol; x<=endCol; x++){
						cell = row.createCell(x);
						//创建列
						if(i==0){
							cell.setCellStyle(defaultHeadStyle);
						}else{
						    cell.setCellStyle(defaultHeadCloumnStyle);
						}
						
						if(x == startCol){
							cell.setCellValue(column.getValue());
						}
					}
					
					//处理下一个
					columnIndex = endCol + 1;
				}
				
			}
			
			//设置冻结行
			int levelSize = levelMap.keySet().size();
			sheet.createFreezePane( 0, levelSize, 0, levelSize );
			
		}else{
			throw new RuntimeException("excel 头部数据结构不完整");
		}
		
		return levelMap;
	}
	
	/**
	 * 生成明细行
	 * @param sheet
	 * @param levelMap
	 * @param dataList
	 */
	private <T> void constructureCellRowData(Sheet sheet, Map<Integer, List<ExcelHeadNode>> levelMap, List<T> dataList){
		
		int maxlevel = levelMap.keySet().size();
		
		List<ExcelHeadNode> headColums = levelMap.get(maxlevel - 1);
		
		
		for(int i=0; i<dataList.size(); i++){
			
			
			T dataObj = dataList.get(i);
			HSSFRow row = (HSSFRow) sheet.createRow(i + maxlevel);
			
			//处理每一行数据
			for(int j=0; j<headColums.size(); j++){
				ExcelHeadNode excelHeadNode = headColums.get(j);
				
				Object columValue = null;
				
				// 区分 <T> 是 bean 还是map
				if(dataObj instanceof Map){
					columValue = ((Map)dataObj).get(excelHeadNode.getKey());
				}else{
					columValue = getDynamicValue(dataObj, excelHeadNode.getKey());
				}
				
				HSSFCell cell = row.createCell(j);
				
				// TODO 这里要处理 数据类型
				if(columValue instanceof Date){
					cell.setCellValue(DateUtils.formatDate((Date) columValue));
				}else if(columValue instanceof Long
						|| columValue instanceof Integer
						|| columValue instanceof Double
						|| columValue instanceof Short){
					
					Double columDoubleValue = Double.valueOf(columValue==null?"0":String.valueOf(columValue));
					
					cell.setCellValue(columDoubleValue);
				    if(columValue instanceof Double && columDoubleValue != 0){
				    	cell.setCellStyle(cellStyleDoubleZero); 
				    }else{
				    	cell.setCellStyle(cellStyleIntegerZero); 
				    }
				    
				    
				}else{
					cell.setCellValue(columValue==null?"":String.valueOf(columValue));
				}
				
				// TODO 设置列宽
				if(i==dataList.size()-1){
					int cellStyleWidth = excelHeadNode.getCellStyleWidth() * 256;
					sheet.setColumnWidth(j, cellStyleWidth);
				}
			}
		}
	}
	
	/**
	 * 递归计算头level数据完整性
	 * @param currentLevel
	 * @param currentNode
	 * @param levelMap
	 * @return
	 */
	
	private boolean calcLevels(Integer currentLevel, ExcelHeadNode currentNode, Map<Integer, List<ExcelHeadNode>> levelMap){
		
		//如果没有子节点则返回
		if(currentNode.getChildNodes().size() == 0){
			currentNode.setCellColSpan(1);
			return false;
		}else{
			
			//初始化下一层----------start-------------
			//下一层级
			Integer nextLevel = currentLevel + 1;
			
			List<ExcelHeadNode> nextLevelNodes = levelMap.get(nextLevel);
			
			if(nextLevelNodes == null){
				nextLevelNodes = new ArrayList<ExcelHeadNode>();
				levelMap.put(nextLevel, nextLevelNodes);
			}
			//初始化下一层----------end-------------
			
			//处理本层
			nextLevelNodes.addAll(currentNode.getChildNodes());
			
			//处理本层 子节点下一层
			//判断数据完整性， 是最后一层 这都没有子节点
			boolean hasNext = true;
			
			for(int i=0; i<currentNode.getChildNodes().size(); i++){
				boolean result = this.calcLevels(nextLevel, currentNode.getChildNodes().get(i), levelMap);
				if(i==0){
					hasNext = result;
				}else{
					//判断是否完成
					if(hasNext != result){
						return false;
					}
				}
			}
			
			//这里处理当前行的cell width
			int cellColSpan = 0;
			for(ExcelHeadNode childNode : currentNode.getChildNodes()){
				cellColSpan += childNode.getCellColSpan();
			}
			
			currentNode.setCellColSpan(cellColSpan);
			
		}
		
		return true;
	}

	/**
	 * 动态获取对象属性
	 * @param <T>
	 * @param srcObj
	 * @param propertyName
	 * @return
	 */
	public static <T> T getDynamicValue(Object srcObj, String propertyName){
		try {
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(srcObj.getClass(), propertyName);
			
			Method readMethod = pd.getReadMethod();
			Object propertyValue = readMethod.invoke(srcObj, readMethod.getParameterTypes());
			
			return (T) propertyValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
