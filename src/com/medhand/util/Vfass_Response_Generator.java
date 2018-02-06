package com.medhand.util;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Vfass_Response_Generator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		  try {
			  
				File fXmlFile = new File("D:/Medhand/test/xml_list.xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
		 
				NodeList nList = doc.getElementsByTagName("vfass");
				int i=0;
				for (int temp = 0; temp < nList.getLength(); temp++){
					   Node nNode = nList.item(temp);
					   Element eElement = (Element) nNode;
					  String date=getTagValue("lastChangedDate", eElement);
					  int p=date.lastIndexOf('-');
					  int day=Integer.parseInt(date.substring(p+1,p+3));
					  
					  if(date.contains("-10-")&& (day<=9) && (day>=8) ) {
						  i++;
						   System.out.println("<vfass>");
						   System.out.println("<npl-id>" + getTagValue("npl-id", eElement)+"</npl-id>");
						   System.out.println("<lastChangedDate>" + getTagValue("lastChangedDate", eElement)+"</lastChangedDate>");
						   System.out.println("<isPublished>" + getTagValue("isPublished", eElement)+"</isPublished>");
						   System.out.println("</vfass>");
					  }
				}
				System.out.println("\n\n\nCount="+i);
		  } catch (Exception e){}
	}
	
	 private static String getTagValue(String sTag, Element eElement) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		 
		        Node nValue = (Node) nlList.item(0);
		 
			return nValue.getNodeValue();
		  }

}
