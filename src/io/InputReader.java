package io;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import poolManager.Problem;
import poolManager.ProblemBestFit;
import poolManager.ProblemNaive;


public class InputReader {
	
	private String pathname;
	
	public InputReader(String pathname) {
		this.pathname = pathname;
	}
	
	public Problem get() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pathname));
			Problem pb = new ProblemBestFit();
			
			// line 1
			String line  = reader.readLine();
			
			Scanner sc = new Scanner(line);
			
			pb.setNbRow(sc.nextInt());
			pb.setSlotPerRow(sc.nextInt());
			int nbUnavailable = sc.nextInt();
			pb.setnbPool(sc.nextInt());
			
			int nbServer = sc.nextInt();
			
			//unvailable slots
			for(int i=0; i<nbUnavailable; i++) {
				line  = reader.readLine();
				sc = new Scanner(line);
				int row = sc.nextInt();
				int slot = sc.nextInt();
				pb.addUnvailable(row, slot);
			}
			
			//servers
			for(int i=0; i<nbServer; i++) {
				line  = reader.readLine();
				sc = new Scanner(line);
				int size = sc.nextInt();
				int capacity = sc.nextInt();
				pb.addServer(size, capacity);
			}
			
			return pb;
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}
