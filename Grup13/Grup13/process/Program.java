package process;
import process.Process;
public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Process process=new Process(args[0], args[1], args[2]);
		process.yazdir();
	}

}
