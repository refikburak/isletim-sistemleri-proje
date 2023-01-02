package program;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("java", "-jar",
				System.getProperty("user.dir") + "\\gorevlendirici\\Process.jar", "ornek.txt");
		pb.redirectErrorStream(true); // redirect error stream to output stream
		pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//		Process proc = pb.start();
		pb.start();
	}

}
