package com.medhand.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenerateXMLManifestForCals {

	private static String filePath="D:/medhand/content/cals/xml/xml/volume1/volume1.xml";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		startProcess();
	}
	
	public static void startProcess() {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("sli");
			int i=0;
			System.out.println(nList.getLength());
			for (int temp = 0; temp < nList.getLength(); temp++){
				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
				
				//System.out.println(eElement.getTextContent());
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
