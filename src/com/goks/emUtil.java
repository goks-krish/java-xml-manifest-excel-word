package com.goks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class emUtil {

//	String static path = "D:/Workspace/em/doc/";
//	static String path = "original/";
	static String path = "./";
	
	static String pathNew = path+"new/";
	
	static String title= null;
 	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Started:");
		Date date = new Date();

		// Create "new" directory
		if(!new File(pathNew).isDirectory()) {
			new File(pathNew).mkdir();
		}
		

		File folderPath = new File(path);
		FilenameFilter fileFilter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
	              if(name.lastIndexOf('.')>0)
	               {
	                  // get last index for '.' char
	                  int lastIndex = name.lastIndexOf('.');
	                  
	                  // get extension
	                  String str = name.substring(lastIndex);
	                  
	                  // match path name extension
	                  if(str.equals(".xml"))
	                  {
	                     return true;
	                  }
	               }
				return false;
			}
		};
		
		for(File xmlFile : folderPath.listFiles(fileFilter)) {
			
			String fileName = xmlFile.getName();
			System.out.println("Processing "+fileName);
			
			//1. Remove head, Remove span & remove page-break
			xmlCleanRemoveHead(fileName);
			
			//2. Clean P
			xmlCleanP(fileName);
			
			//3. Order List Alpha (3rd level)
			xmlGroupList(fileName);
			
		}
		
		String str = "<p class=\"MsoNormal\" style=\"margin-left:8.7pt;line-height:10.0pt\"><span style=\"font-size:10.0pt;font-family:&quot;Trebuchet MS&quot;,&quot;sans-serif&quot;\"><img width=\"18\" height=\"18\" src=\"section1_files/image007.gif\"> <span style=\"position:relative;top:-5.5pt\"><img width=\"396\" height=\"18\" src=\"section1_files/image011.gif\" alt=\"Text Box: DIAGNOSIS\"> </span><img width=\"18\" height=\"18\" src=\"section1_files/image007.gif\"></span></p>";
//		imgFix(str);
//		xmlParse();

		System.out.println("Start:" + date);
		System.out.println("End:" + new Date());

	}
	
	public static void xmlGroupList(String fileName) {

		try {
			FileReader fr = new FileReader(pathNew+fileName);
	        BufferedReader br = new BufferedReader(fr);
	        String line = null;
	        String regex = "[0-9]+";

	        ArrayList<String> destData = new ArrayList<String>();
	        
	        String prevLine = "";
	        String nextLine = "";
	        line = br.readLine();
	        while (line != null) {
	        	nextLine = br.readLine();
	        	
	        	if(line.contains("<listText") && !nextLine.contains("<listText")
	        			&& nextLine.contains("<par>")){
	        		nextLine = line.replace("</listText>", "\n "+nextLine.substring(nextLine.indexOf("<par>") + 5, nextLine.indexOf("</par>"))+"</listText>");
	        	} else {
	        		destData.add(line+"\n");
	        	}
	        	
	        	prevLine = line;
	        	line = nextLine;
	        }
	        br.close();

	        //1. Clean ListAlpha ##TODO: To BE ENABLED LATER
//	        destData = xmlCleanListAlpha(destData);
//	        //2. Clean ListRoman
//	        destData = xmlCleanListRoman(destData);
//	        //3. Clean ListNumber
//	        destData = xmlCleanListNumber(destData);

	        
	        FileWriter fw = new FileWriter(pathNew+fileName);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();

	        
		} catch(Exception e) { e.printStackTrace();}
		
	}
	
	public static void xmlCleanRemoveHead(String fileName){ 
		try {
			
            FileReader fr = new FileReader(path+fileName);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(pathNew+fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            String line = null;
            int h1Count = 0;
            
            while ((line = br.readLine()) != null) {
            	
            	if(line.contains("<html title=")) {
            		title = line.split("<html title=\"")[1].split("\"><head>")[0];
            	}
            	
            	if(line.contains("<head>")) {
            		if(line.contains("<html>")) {
            			bw.write("<html>"+ "\n");
            		}
            		while ((line = br.readLine()) != null && !(line.contains("</head>")));
            		line = br.readLine();
            	}

            	//Clear SPAN
            	if((line.contains("<span") && line.contains("page-break-before"))) {

            		while ((line = br.readLine()) != null && !(line.contains("</span>")));
            		line = br.readLine();
            	}
            	
            	
            	//Clear Page Break
            	if(!(line.startsWith("<div") || line.startsWith("</div") ) ) {
            		
//            		if(line.startsWith("<h1")) {
//            			if(h1Count == 0) { 
//            				line = "<sect>" + line;
//            			} else {
//            				line = "</sect>\n<sect>" + line;
//            			}
//            				
//            			
//            			h1Count++;
//            		}
//            	
            	if(line.contains("</html>")) {
            		line = line.replace("</html>", "");
            	}
           		bw.write(line+ "\n");
            	}
            	
            }
            br.close();
            bw.flush();
            bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void xmlCleanP(String fileName){
 		
		try {
			FileReader fr = new FileReader(pathNew+fileName);
	        BufferedReader br = new BufferedReader(fr);
	        String line = null;
	        String regex = "[0-9]+";

	        ArrayList<String> destData = new ArrayList<String>();
	        while ((line = br.readLine()) != null) {
	        	//include title
	        	if(line.contains("body lang=")) {
        			line = line.replace("\">","\" title=\"" + title + "\">");
        		}
	        	
	        	//change body to chapter
	        	if(line.contains("<body") || line.contains("</body>")) {
	        		line = line.replace("body", "chapter");
	        	}
	        	
	        	// Collect table
	        	String tableData = new String();
	        	if(line.contains("<table")){
	        		int tCount = 0;
	        		do {
	        			if(line!=null && line.trim().length()!=0) {
	        				tableData = tableData + line;
	        			}
	        			
	        			if(line.contains("<table"))
	        				tCount++;
	        			
	        			if(line.contains("/table>"))
	        				tCount--;
	        		} while ((line = br.readLine()) != null && tCount!=0);
	        		// REMOVE TABLE DATA
//	        		line = tableData;
	        	}
	        	
	        	//collect h1
	        	String h1Data = new String();
	        	if(line.contains("<h1")){
	        		int pCount = 0;
	        		do {
	        			if(line!=null && line.trim().length()!=0) {
	        				h1Data = h1Data + line;
	        			}
	        			
	        			if(line.contains("<h1"))
	        				pCount++;
	        			
	        			if(line.contains("/h1>"))
	        				pCount--;
	        		} while ((line = br.readLine()) != null && pCount!=0);
	            	h1Data = h1Data.replaceAll("&nbsp;", "");
	            	h1Data = imgFix(h1Data);

	        		line = h1Data;
	        		if(h1Data.contains("Text Box:")) {
	        			String h1Alt = h1Data.split("Text Box:")[1];
	        			line = "<h1>" + cleanXML(h1Alt.substring(0,h1Alt.indexOf("\""))) + "</h1>";
	        		} else {
	        			line = "<h1> </h1>";
	        		}
	        	}
	        	

	        	
	        	//Collect h2
	        	String h2Data = new String();
	        	if(line.contains("<h2")){
	        		int pCount = 0;
	        		do {
	        			if(line!=null && line.trim().length()!=0) {
	        				h2Data = h2Data + line;
	        			}
	        			
	        			if(line.contains("<h2"))
	        				pCount++;
	        			
	        			if(line.contains("/h2>"))
	        				pCount--;
	        		} while ((line = br.readLine()) != null && pCount!=0);
	        		
	            	InputSource isH2 = new InputSource();
	            	h2Data = imgFix(h2Data);
	            	h2Data = h2Data.replaceAll("&nbsp;", "");
	            	isH2.setCharacterStream(new StringReader(h2Data));
	        		DocumentBuilder dbH2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		            Document docH = dbH2.parse(isH2);
	        		line = "<h2> " + docH.getDocumentElement().getTextContent() + " </h2>";
	        	}
	        	
	        	//collect p
	        	String pData = new String(); 
	        	if(line.contains("<p")){
	        		int pCount = 0;
	        		do {
	        			if(line!=null && line.trim().length()!=0) {
	        				pData = pData + line;
	        			}
	        			
	        			if(line.contains("<p"))
	        				pCount++;
	        			
	        			if(line.contains("/p>"))
	        				pCount--;
	        			
	        		} while ((line = br.readLine()) != null && pCount!=0);

	        	}
	        	
	        	if(pData!=null && pData.trim().length()!=0 && !pData.contains("<table")
	        				) { //&& !pData.contains("<img") && !pData.contains("MsoBodyText")
	            	InputSource is = new InputSource();
	            	pData = pData.replaceAll("&nbsp;", "");
	            	pData = imgFix(pData);
	                is.setCharacterStream(new StringReader(pData));
	                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	                Document doc = db.parse(is);
	                

//	                if(pData.contains("</b>")) {
//	                	pData = pData.replaceAll("</b>", ". </b>");
//	                	System.out.println(pData);
//	                	
//	                }
	               
	                //Get bold tag out
	                ArrayList<String> boldList = new ArrayList<String>();
	                NodeList nodesBold = doc.getElementsByTagName("b");
	                for (int i=0; i<nodesBold.getLength(); i++) {
	                	Node node = nodesBold.item(i);
	                	if(node !=null) {
	                		boldList.add(node.getTextContent());
	                	}
	                }
	                
	                //Get italic tag out
	                ArrayList<String> italicList = new ArrayList<String>();
	                NodeList nodesItalic = doc.getElementsByTagName("i");
	                for (int i=0; i<nodesItalic.getLength(); i++) {
	                	Node node = nodesItalic.item(i);
	                	if(node !=null) {
	                		italicList.add(node.getTextContent());
	                	}
	                }
	                
	                //Get numbered list out
	                ArrayList<String> numberList = new ArrayList<String>();
	                NodeList nodes = doc.getElementsByTagName("span");
	                for (int i=0; i<nodes.getLength(); i++) {
	                	Node node = nodes.item(i);
	                	if(node !=null && node.getAttributes().getNamedItem("style").getTextContent().contains("color:#E31836")) {
	                		numberList.add(node.getTextContent());
	                	}
	                }
	                
	                
	                String text = cleanXML(doc.getDocumentElement().getTextContent());
	                
	                //Add bold tag
	                if(boldList.size()!=0) {
	                	for(String s : boldList) {
	                		if(text.trim().length()!=0 && s.trim().length()!=0) {
	                			text = text.replace(s, "<b>" + s + "</b>");
	                		}
	                	}
	                }
	                
	                //Add italic tag
	                if(italicList.size()!=0) {
	                	for(String s : italicList) {
	                		if(text.trim().length()!=0 && s.trim().length()!=0) {
	                			text = text.replace(s, "<i>" + s + "</i>");
	                		}
	                	}
	                }
	                
	                
	                //Add list tag
	                if(numberList.size()!=0) {
	                	for(String s : numberList) {
	                		
	                		if(s.startsWith("(i") || s.startsWith("(v") || s.startsWith("(x")) {
	                			text = "<listRoman>" + text.replace(s, "<listNo>" + s + "</listNo><listText>") + "</listText></listRoman>";
	                		} else if(s.trim().matches(regex)){
	                			text = "<listNumber>" + text.replaceFirst("<b>"+s+"</b>", "<listNo>" + "<b>"+s+"</b>" + "</listNo><listText>") + "</listText></listNumber>";
//	                			text = text.replace(s, "<listNumber>" + s + "</listNumber>");
	                		} else if(s.startsWith("(") && s.trim().length()==3) {
	                			text = "<listAlpha>" + text.replace(s, "<listNo>" + s + "</listNo><listText>") + "</listText></listAlpha>";
//	                			text = text.replace(s, "<listAlpha>" + s + "</listAlpha>");
	                		} 
	                		
	                	}
	                }
	                
	                
	                //Add Subheading-Diagonsis/Management or extract P
	                if(text!=null && text.trim().length()!=0) {
//	                	pData = "<par " + doc.getDocumentElement().getAttributes().item(0) + ">" + text + "</par>";
	                	if(text.contains("â—?")) {
	                		text = text.replace("â—?", "*");
	                	}
	                	pData = "<par>" + text + "</par>";
	                } else  if(pData.contains("alt=\"Text Box: MANAGEMENT\"")) {
	                	pData = "<h3>" + "MANAGEMENT" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: DIAGNOSIS\"")) {
	                	pData = "<h3>" + "DIAGNOSIS" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: DIFFERENTIAL DIAGNOSIS\"")) {
	                	pData = "<h3>" + "DIFFERENTIAL DIAGNOSIS" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: INDICATIONS\"")) {
	                	pData = "<h3>" + "INDICATIONS" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: \"")) {
	                	pData = "<h3>" + "" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: CONTRAINDICATIONS\"")) {
	                	pData = "<h3>" + "CONTRAINDICATIONS" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: TECHNIQUE\"")) {
	                	pData = "<h3>" + "TECHNIQUE" + "</h3>";
	                } else  if(pData.contains("alt=\"Text Box: COMPLICATIONS\"")) {
	                	pData = "<h3>" + "COMPLICATIONS" + "</h3>";
	                } else {
	                	pData = "";
	                }
	                
	        	}
	        
	        	if(pData!=null && pData.trim().length()!=0) {
	        		if(!pData.contains(title) && !(pData.contains("<table") && pData.contains("MsoNormal"))) {
	        			destData.add(pData + "\n");
	        		}
	        	}
	        	
	        	if(line!=null && line.trim().length()!=0 && !line.contains("<br clear=\"ALL\">")) { 
	        		destData.add(line+ "\n"); 
	        	}
	        	
	        }
	        br.close();
	        
	        FileWriter fw = new FileWriter(pathNew+fileName);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw = new BufferedWriter(fw);
	        //Clearing SPAN & Adding medhand
	        bw.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"../DTD/medhand.dtd\">\n");
	        for(String str:destData) {
	        	if(!str.startsWith("<span") && !str.startsWith("</span")) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        	}
	        }
	        bw.flush();
	        bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> xmlCleanListAlpha(ArrayList<String> tempData) {
        String startTag = "<listAlpha>";
        String endTag = "</listAlpha>";
        
		ArrayList<String> destData = new ArrayList<String>();
        Iterator<String> itr = tempData.iterator();
        String line = null;
        while(itr.hasNext()) {
        	line = itr.next();
        	if(line.contains(startTag)) {
        		line = "<listAlphaSection>\n" +line;
        		do {
	        		line = line.replace("<par>", "");
	        		line = line.replace("</par>", "");
	        		destData.add(line);
	        		line = itr.next();
        		} while (!line.contains("listRoman") && !line.contains("<h3") && 
        				!line.contains("listNumber") && !line.contains("</sect"));
        		line = "</listAlphaSection>\n" + line;
        	}
        	destData.add(line);
        }
		return destData;
	}

	public static ArrayList<String> xmlCleanListRoman(ArrayList<String> tempData) {
        String startTag = "<listRoman>";
        String endTag = "</listRoman>";
        
		ArrayList<String> destData = new ArrayList<String>();
        Iterator<String> itr = tempData.iterator();
        String line = null;
        while(itr.hasNext()) {
        	line = itr.next();
        	if(line.contains(startTag)) {
        		line = "<listRomanSection>\n" +line;
        		do {
	        		line = line.replace("<par>", "");
	        		line = line.replace("</par>", "");
	        		destData.add(line);
	        		line = itr.next();
        		} while (!line.contains("<h3") && !line.contains("listNumber") 
        				&& !line.contains("</sect"));
        		if(line.contains("</listAlphaSection>")) {
        			line = line.replace("</listAlphaSection>", "</listAlphaSection>\n</listRomanSection>");
        		} else {
        			line = "</listRomanSection>\n" + line; 
        		 }
        	}
        	destData.add(line);
        }
		return destData;
	}

	
	public static ArrayList<String> xmlCleanListNumber(ArrayList<String> tempData) {
        String startTag = "<listNumber>";
        String endTag = "</listNumber>";
        
		ArrayList<String> destData = new ArrayList<String>();
        Iterator<String> itr = tempData.iterator();
        String line = null;
        while(itr.hasNext()) {
        	line = itr.next();
        	if(line.contains(startTag)) {
        		line = "<listNumberSection>\n" +line;
        		do {
	        		line = line.replace("<par>", "");
	        		line = line.replace("</par>", "");
	        		destData.add(line);
	        		line = itr.next();
        		} while (!line.contains("<h3") && !line.contains("</sect"));
        		if(line.contains("</listRomanSection>")) {
        			line = line.replace("</listRomanSection>", "</listRomanSection>\n</listNumberSection>");
        		} else {
        		 line = "</listNumberSection>\n" + line; 
        		 }

        	}
        	destData.add(line);
        }
		return destData;
	}

	public static void xmlParse(){
		String fileName = "section1.xml";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(path+fileName);
			
			//NodeList nodeList = document.getDocumentElement().getChildNodes();

			//System.out.println(nodeList.getLength());
			
			System.out.println(doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void renameFiles() {
		String path = "D:/Workspace/em/xml/xml/Emergency_Medicine/Emergency_Medicine";
		File folder = new File (path);
		for(File file : folder.listFiles()) {
			String name = file.getName();
			String first = name.substring(0,name.indexOf('.'));
			String exten = name.substring(name.indexOf('.'));
			
			String modFirst = "";
			if(name.contains("page") && first.length()<7) {
				
				if(first.length() == 5) {
					modFirst = first.replace("page", "page00");
				}
				
				if(first.length() == 6) {
					modFirst = first.replace("page", "page0");
				}				
				
				String newName = file.getParent() + "/" + modFirst + exten;
				
				file.renameTo(new File(newName));
				 System.out.println(file.getName() +" --- " + newName);
			}
		}
	}

	public static String imgFix(String data){
		int len = data.split("<img").length;
		int count = 0;
		String imgData = null;
		for(String str: data.split("<img")) {
//			System.out.println("<img"+str);
			if(count == 0) {
				imgData = str;
			} else {
				imgData = imgData + "<img" + str.replaceFirst(">", "/>");
			}
			count ++;
		}
		return imgData;
	}
	
	public static String cleanXML(String str) {
		str = str.replaceAll("&", "&amp;");		
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		return str;
	}
}
