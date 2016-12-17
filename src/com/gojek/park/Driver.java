package com.gojek.park;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import com.gojek.park.services.IParkService;
import com.gojek.park.services.ParkService;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
				
		
		try {
			
		
		IParkService parkService=new ParkService();//Should be injected using DI principle
		Scanner in=null;
		if(args!=null && args.length>0)
			 in=new  Scanner(new BufferedReader(new FileReader(args[0])));
		else
			 in=new Scanner(System.in);
		
		String command="",value1="",value2="";
		while(in.hasNext()){
			
			String input=in.nextLine();
			String tokens[]=input.split("\\s+");
			int len=tokens.length;
			if(len>0)
			 command=tokens[0];
			
			if(len>1)
				 value1=tokens[1];
			
			if(len>2)
			 value2=tokens[2];			
			parkService.processCommand(command, value1,value2);
			command="";
			value1="";
			value2="";
			
		}		
		in.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
