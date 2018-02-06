package com.goks;

import java.io.FileInputStream;

import org.apache.poi.POIDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class AccessWord {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String path = "C:/Goks/mh/em/Emergency Medicine 2014.docx";
		
		System.out.println("***Start***");
		
		
	}
	
	   public static void readMyDocument(String fileName){
	        POIFSFileSystem fs = null;
	        try {
	            fs = new POIFSFileSystem(new FileInputStream(fileName));
//	            POIDocument doc = new POIDocument(fs);
	            
//	            POIDocument
	 
	            /** Read the content **/
//	            readParagraphs(doc);
	 
	            int pageNumber=1;
	 
	            /** We will try reading the header for page 1**/
//	            readHeader(doc, pageNumber);
	 
	            /** Let's try reading the footer for page 1**/
//	            readFooter(doc, pageNumber);
	 
	            /** Read the document summary**/
//	            readDocumentSummary(doc);
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } 
}
