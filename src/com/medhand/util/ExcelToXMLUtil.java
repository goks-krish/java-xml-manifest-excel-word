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

public class ExcelToXMLUtil {
	private String inputFile;
	
	public void run(){
		ExcelToXMLUtil excelUtil = new ExcelToXMLUtil();
		excelUtil.setInputFile("c:/test/nwl.xls");
		try {
			excelUtil.process();;
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
	         
	         Element root = doc.createElement("bnf");
	         doc.appendChild(root);
			
	         Element chapter = null;
	         Element section = null;
	         Element subSection = null;
	         
	         String chapterContent = null;
	         String sectionContent = null;
	         String subSectionContent = null;
	         
			for (int i = 0; i < 664; i ++) {
					System.out.print("ROW-"+(i+1));
					if(chapterContent != sheet.getCell(0, i).getContents()) {
						chapterContent = sheet.getCell(0, i).getContents();
						chapter = doc.createElement("bnf-chapter");
						Element title= doc.createElement("title");
						Text titleContent = doc.createTextNode(chapterContent);
						title.appendChild(titleContent);
						chapter.appendChild(title);
						//chapter.setAttribute("chapter", chapterContent);
				        root.appendChild(chapter);
					}
					
					if(sectionContent != sheet.getCell(1, i).getContents()){
						sectionContent = sheet.getCell(1, i).getContents();
						section	= doc.createElement("bnf-section");
						Element title= doc.createElement("title");
						Text titleContent = doc.createTextNode(sectionContent);
						title.appendChild(titleContent);
						section.appendChild(title);
						//section.setAttribute("section", sectionContent);
				        chapter.appendChild(section);
					}
					
					if(subSectionContent!=sheet.getCell(2, i).getContents()) {
						subSectionContent=sheet.getCell(2, i).getContents();
						subSection = doc.createElement("bnf-sub-section");
						Element title= doc.createElement("title");
						Text titleContent = doc.createTextNode(subSectionContent);
						title.appendChild(titleContent);
						subSection.appendChild(title);
						subSection.setAttribute("sub-section", subSectionContent);
				        section.appendChild(subSection);
					}
					
					Element drug = doc.createElement("drug");
					subSection.appendChild(drug);
					
					Element drugName = doc.createElement("drug-name");
					//child.setAttribute("title",sheet.getCell(0, i).getContents());
					Text drugNameText = doc.createTextNode(sheet.getCell(3, i).getContents());
					drugName.appendChild(drugNameText);
			        drug.appendChild(drugName);
			        
			        Element formulation = doc.createElement("formulations");
			        Text formulationText = doc.createTextNode(sheet.getCell(4, i).getContents());
			        formulation.appendChild(formulationText);
			        drug.appendChild(formulation);
			        
			        Element order = doc.createElement("order-of-preference");
			        Text orderText = doc.createTextNode(sheet.getCell(6, i).getContents());
			        order.appendChild(orderText);
			        drug.appendChild(order);
			        
			        Element initiate = doc.createElement("initiated-by-");
			        Text initateText = doc.createTextNode(sheet.getCell(8, i).getContents());
			        initiate.appendChild(initateText);
			        drug.appendChild(initiate);
			        
			        Element comments = doc.createElement("comments");
			        Text commentText = doc.createTextNode(sheet.getCell(9, i).getContents());
			        comments.appendChild(commentText);
			        drug.appendChild(comments);
			        System.out.println("--OK");			
			}
			readXML(doc);
		} catch (Exception e) { 	e.printStackTrace();	}
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
			BufferedWriter out = new BufferedWriter(new FileWriter("c:/test/work.xml"));
			out.write(file);
			out.close();
			} catch (Exception e) { e.printStackTrace(); }	
	}
	
}
