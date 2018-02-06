package com.goks;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("***Start***");
		
		
		File sourceFolder = new File("C:\\Goks\\Personal\\Uncle\\DVD1");
		for (File file:sourceFolder.listFiles()) {
			String n = file.getName();
			if(n.contains(" (1)")) {
				String name = file.getParent() +"/" + n.replace(" (1)", "");
				System.out.println();
				System.out.println(name);
				file.renameTo(new File (name));
			}
		}
				
		
	}

}
