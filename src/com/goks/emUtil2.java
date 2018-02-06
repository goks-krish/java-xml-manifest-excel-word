package com.goks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class emUtil2 {
	
//	String static path = "D:/Workspace/em/doc/";
//	static String path = "original/";
	static String path = "./";
	
	static String pathNew = path+"new/";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
			
			//1. Make sect
			xmlParse(fileName);
		}
		

		System.out.println("Start:" + date);
		System.out.println("End:" + new Date());

	}
	
	
	public static void xmlParse(String fileName){
		
		try {
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document doc = builder.parse(path+fileName);
//			NodeList nodeList = doc.getDocumentElement().getChildNodes();
//			for(int i = 0; i< nodeList.getLength(); i++) {
//				Node node = nodeList.item(i);
//				System.out.println(node.getChildNodes().getLength());
//			}
            FileReader fr = new FileReader(path+fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            int h1Count = 0;
            ArrayList<String> destData = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
            	if(line.startsWith("<h1")) {
//            		System.out.println(line);
            		if(h1Count == 0) {
            			line = "<sect>\n" + line;
            		} else {
            			line = "</sect>\n<sect>\n" + line;
            		}
            		h1Count ++;
            		
            	}
            	if(line.contains("</chapter>")) {
            		line = "</sect>\n" + line;
            	}
            	destData.add(line+ "\n");
            }
        
	        //1. Add addSect2
	        destData = addSect2(destData);
            FileWriter fw = new FileWriter(pathNew+fileName);
            BufferedWriter bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();
	        
	        //2. Add addSect3
            fr = new FileReader(pathNew+fileName);
            br = new BufferedReader(fr);
            line = null;
            destData = new ArrayList<String>();
            while ((line = br.readLine()) != null) { 
            	destData.add(line+ "\n");
            }
            destData = addSect3(destData);
	        fw = new FileWriter(pathNew+fileName);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();
	        
	        //3. Clean ListAlpha 
            fr = new FileReader(pathNew+fileName);
            br = new BufferedReader(fr);
            line = null;
            destData = new ArrayList<String>();
            while ((line = br.readLine()) != null) { 
            	destData.add(line+ "\n");
            }
	        destData = emUtil.xmlCleanListAlpha(destData);
	        fw = new FileWriter(pathNew+fileName);
	        bw = new BufferedWriter(fw);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str);
	        }
	        bw.flush();
	        bw.close();

	        //4. Clean ListRoman
            fr = new FileReader(pathNew+fileName);
            br = new BufferedReader(fr);
            line = null;
            destData = new ArrayList<String>();
            while ((line = br.readLine()) != null) { 
            	destData.add(line+ "\n");
            }	       	        
	        destData = emUtil.xmlCleanListRoman(destData);
	        fw = new FileWriter(pathNew+fileName);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();

	        //5. Clean ListNumber
            fr = new FileReader(pathNew+fileName);
            br = new BufferedReader(fr);
            line = null;
            destData = new ArrayList<String>();
            while ((line = br.readLine()) != null) { 
            	destData.add(line+ "\n");
            }
	        destData = emUtil.xmlCleanListNumber(destData);
	        fw = new FileWriter(pathNew+fileName);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();
	        
	        //6. Fix ROMAN
            fr = new FileReader(pathNew+fileName);
            br = new BufferedReader(fr);
            line = null;
            destData = new ArrayList<String>();
            
            while ((line = br.readLine()) != null) { 
            	if(line.contains("<listRoman>")){
            		while(!line.contains("</listRoman>")) {
            			destData.add(line+"\n");
            			line = br.readLine();
            		}
            		String nextLine = br.readLine();
            		if(nextLine.contains("<listAlphaSection>")) {
            			line = line.replace("</listRoman>", "");
            			destData.add(line + "\n");
            			line = nextLine;
            			while(!line.contains("</listAlphaSection>")) {
            				destData.add(line + "\n");
            				line = br.readLine();
            			}
            			destData.add(line + "\n</listRoman>\n" );
            		} else {
            			destData.add(line + "\n" + nextLine + "\n");
            		}
            	} else {
            		destData.add(line+"\n");
            	}
            }

            fw = new FileWriter(pathNew+fileName);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();

	        //7. Fix NUMBER
            fr = new FileReader(pathNew+fileName);
            br = new BufferedReader(fr);
            line = null;
            destData = new ArrayList<String>();
            
            while ((line = br.readLine()) != null) { 
            	if(line.contains("<listNumber>")){
            		while(!line.contains("</listNumber>")) {
            			destData.add(line+"\n");
            			line = br.readLine();
            		}
            		String nextLine = br.readLine();
            		if(nextLine.contains("<listRomanSection>")) {
            			line = line.replace("</listNumber>", "");
            			destData.add(line + "\n");
            			line = nextLine;
            			while(!line.contains("</listRomanSection>")) {
            				destData.add(line + "\n");
            				line = br.readLine();
            			}
            			destData.add(line + "\n</listNumber>\n" );
            		} else {
            			destData.add(line + "\n" + nextLine + "\n");
            		}
            	} else {
            		destData.add(line+"\n");
            	}
            }


            fw = new FileWriter(pathNew+fileName);
	        bw = new BufferedWriter(fw);
	        for(String str:destData) {
	        		bw.write(str.replaceAll("&nbsp;", ""));
	        }
	        bw.flush();
	        bw.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static ArrayList<String> addSect3(ArrayList<String> tempData) {
		ArrayList<String> destData = new ArrayList<String>();
        Iterator<String> itr = tempData.iterator();
        String line = null;
        boolean addedSect3 = false;
        boolean insideSect = false;
        boolean insideSect2 = false;
        
        int insideSectCount = 0;
        int insideSect2Count = 0;
       
        while(itr.hasNext()) {
        	line = itr.next();
        	
        	
        	if(line.contains("<sect2>")) {
        		insideSect2 = true;
        		insideSect = false;
        	}
   
        	if(insideSect2) {
        		if (line.contains("<h3>")) {
	        		addedSect3 = true;
	        		if(insideSect2Count == 0) {
	        			line = "<sect3>\n" + line;
	        		} else {
	        			line = "</sect3>\n<sect3>\n" + line;
	        		}
	        		addedSect3 = true;
	        		insideSect2Count++;
        		}
            	if(line.contains("</sect2>")) {
            		line = "</sect3>\n" + line;
            		addedSect3 = false;
            		insideSect2Count = 0;
        		}
        	}
        	
        	if(line.contains("</sect2>")) {
        		insideSect2 = false;
        	}

        	if(line.contains("<sect>")) {
        		insideSect = true;
        	}
   

        	if(insideSect) {
        		if (line.contains("<h3>")) {
	        		addedSect3 = true;
	        		if(insideSect2Count == 0) {
	        			line = "<sect3>\n" + line;
	        		} else {
	        			line = "</sect3>\n<sect3>\n" + line;
	        		}
	        		addedSect3 = true;
	        		insideSect2Count++;
        		}
            	if(line.contains("</sect>") && addedSect3) {
            		line = "</sect3>\n" + line;
            		addedSect3 = false;
            		insideSect2Count = 0;
        		}
        	}
        	
        	if(line.contains("</sect>")) {
        		insideSect = false;
        	}

        	destData.add(line);
        	
        }
		
		return destData;
	}
	
	public static ArrayList<String> addSect2(ArrayList<String> tempData) {
		ArrayList<String> destData = new ArrayList<String>();
        Iterator<String> itr = tempData.iterator();
        String line = null;
        boolean addedSect2 = false;
        boolean insideSect = false;
        while(itr.hasNext()) {
        	line = itr.next();
        	
        	if(line.contains("<sect>")) {
        		insideSect = true;
    		}
        	
        	if(line.startsWith("<h2")) {
        		addedSect2 = true;
        		if(insideSect) {
        			line = "<sect2>\n" + line;
        		} else {
        			line = "</sect2>\n<sect2>\n" + line;
        			addedSect2 = true;
        		}
        		insideSect = false;
        	}
        	
        	if(line.contains("</sect>") && addedSect2) {
        		line = "</sect2>\n" + line;
        		addedSect2 = false;
    		}

        	destData.add(line);
        	
        }
		
		return destData;
	}
}
