package com.medhand.util;
import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class ExcelToManifextXMLUtil {
	private String inputFile;
	
	public static void main(String[] args) {
		System.out.println("START");
		ExcelToManifextXMLUtil excelUtil = new ExcelToManifextXMLUtil();
		excelUtil.setInputFile("D:/Workspace/dyna2/DynaMedTitleList04-22-2015.xls");
		try {
			excelUtil.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		ExcelToManifextXMLUtil excelUtil = new ExcelToManifextXMLUtil();
		excelUtil.setInputFile("D:/Workspace/dyna2/DynaMedTitleList04-22-2015.xls");
		try {
			excelUtil.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void process() throws IOException  {
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder;			
	         docBuilder = dbfac.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element root = doc.createElement("manifest");
	         root.setAttribute("title", "Dynamed");
	         doc.appendChild(root);
			
	         Element manifestFile = null;
	         
	         String title = null;
	         String target = null;
	         String type = null;
	         String id=null;
	         String path=null;
	         String link=null;
			for (int i = 1; i < 5541; i ++) {
					title = sheet.getCell(0, i).getContents();
					target = sheet.getCell(1, i).getContents();
					type = sheet.getCell(2, i).getContents();
					link = sheet.getCell(3, i).getContents();
					id = "T"+link.substring(link.lastIndexOf('=')+1);
					path= "xml\\"+id+".xml";
					String filePath="D:/Workspace/dyna2/xml/"+path;
					File file=new File(filePath);
					if(file.exists()) {
						manifestFile=doc.createElement("manifest-file");
						manifestFile.setAttribute("id", id);
						manifestFile.setAttribute("title", "Main-Index");
				        manifestFile.setAttribute("sub-title", title);
				        manifestFile.setAttribute("target", target);
				        manifestFile.setAttribute("type", type);
				        manifestFile.setAttribute("src", path);
				        if(type.equalsIgnoreCase("Synonym"))
				        	root.appendChild(manifestFile);
					} else {
						System.out.println(id+" - file doesn't exist");
					}
					
			       // System.out.println("id="+id+" added");
			}
			readXML(doc);
		} catch (Exception e) { 	e.printStackTrace();	}
		System.out.println("COMPLETED!!!");
	}
	
	public void readXML(Document doc){
		try{
	           TransformerFactory transfac = TransformerFactory.newInstance();
	            Transformer trans = transfac.newTransformer();
	            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            trans.setOutputProperty(OutputKeys.INDENT, "yes");
	            //create string from xml tree
	            StringWriter sw = new StringWriter();
	            StreamResult result = new StreamResult(sw);
	            DOMSource source = new DOMSource(doc);
	            trans.transform(source, result);
	            String xmlString = sw.toString();
	            //print xml
	            writeFile(xmlString);

		} catch(Exception e){}
		
	}
	
	public void writeFile(String file){
		//System.out.println(file);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("D:/Workspace/dyna2/xml/dynamed.xml"));
			out.write(file);
			out.close();
			} catch (Exception e) { e.printStackTrace(); }	
	}
	
}
