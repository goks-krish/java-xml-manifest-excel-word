package com.medhand.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenerateXMLManifest {
	static String XMLManifest="D:/Workspace/vfass/xml/vfass.xml";
	static String XMLFolder="D:/Workspace/vfass/xml/xml/";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenerateXMLManifest xmlManifest= new GenerateXMLManifest();
		xmlManifest.processXMLFolder(); 

		//		if (args.length == 2) {
//			XMLFolder=args[0];
//			XMLManifest=args[1];			
//			GenerateXMLManifest xmlManifest= new GenerateXMLManifest();
//			xmlManifest.processXMLFolder(); 
//		} else {
//			System.err.println("Pass valid arguments - path & xml file name");
//			System.exit(1);
//		}
	}
	
	public void processXMLFolder(){
		File content=new File(XMLFolder);
		String[] filesList=content.list();
		Arrays.sort(filesList);
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;	
        Document doc=null;
        try {
			docBuilder = dbfac.newDocumentBuilder();
	        doc = docBuilder.newDocument();
	        Element root = doc.createElement("manifest");
	      //  root.setAttribute("title", "vfass");
	        doc.appendChild(root);
	        Element manifestFile = null;
	        int i=0;
			for(String file : filesList){
				i++;
				manifestFile=doc.createElement("manifest-file");
				String src="xml\\"+file;
				manifestFile.setAttribute("src", src);
				root.appendChild(manifestFile);
				//System.out.println(file);
			}
			System.out.println("Number of files="+i);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readXML(doc);
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
			BufferedWriter out = new BufferedWriter(new FileWriter(XMLManifest));
			out.write(file);
			out.close();
			} catch (Exception e) { e.printStackTrace(); }	
	}
}
