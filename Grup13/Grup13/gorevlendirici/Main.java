package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//int varisZamani =0 ,oncelik=0,prosesZamani=0;		
		try {
			  Scanner scanner = new Scanner(new File("ornek.txt"));
		      while (scanner.hasNextLine()) {
		    	  String[] tokens = scanner.nextLine().split(", ");
		          String last = tokens[tokens.length - 1];
		          //System.out.println(tokens[0]);
		          //System.out.println(last);
		          
		          int varisZamani=Integer.parseInt(tokens[0]);
		          int oncelik=Integer.parseInt(tokens[1]);
		          int prosesZamani=Integer.parseInt(tokens[2]);
		          String[] arguments= {tokens[0],tokens[1],tokens[2]};
		          ProcessBuilder processBuilder = new ProcessBuilder();
		          
		          //processBuilder.command("java", "-jar", "C:\\Users\\Seda\\eclipse-workspace\\OS_Project\\src\\OS\\Process.jar 1 2 3");
		          //Process p = processBuilder.start();
		          ProcessBuilder pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\gorevlendirici\\Process.jar",tokens[0],tokens[1],tokens[2]);
		          pb.redirectErrorStream(true); // redirect error stream to output stream
		          pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		          Process proc = pb.start();

		          //System.out.println("1: " + varisZamani);
		          //System.out.println("2: " + );
		          //System.out.println("3: " + prosesZamani);
		      }

		      scanner.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

}
