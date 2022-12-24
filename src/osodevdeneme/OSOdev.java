package osodevdeneme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class OSOdev {

	public static void main(String[] args) throws IOException {

		List<Process> p = new ArrayList<Process>();
		Queue<Process> gercekZamanli=new LinkedList<Process>();
		Queue<Process> oncelik1=new LinkedList<Process>();
		Queue<Process> oncelik2=new LinkedList<Process>();
		Queue<Process> oncelik3=new LinkedList<Process>();


		Scanner scanner = new Scanner(new File("src/osodevdeneme/ornek.txt"));
		while (scanner.hasNextLine()) {

			Process process=new Process();
			String[] tokens = scanner.nextLine().split(", ");
		    process.varisZamani=Integer.parseInt(tokens[0]);
		    process.oncelik=Integer.parseInt(tokens[1]);
		    process.processZamani=Integer.parseInt(tokens[2]);
		    p.add(process);
		    System.out.println(process.varisZamani+"  "+process.oncelik+"  "+process.processZamani);
		}
		int sayac=0;
		
		for (int i=0;i<p.size();i++) {
			if (p.get(i).oncelik==0) {
				gercekZamanli.add(p.get(i));
			}
			else if (p.get(i).oncelik==1) {

				sayac++;
				oncelik1.add(p.get(i));
			}
			else if (p.get(i).oncelik==2) {
				oncelik2.add(p.get(i));
			}
			else {
				oncelik3.add(p.get(i));
			}
		}

		Iterator it = gercekZamanli.iterator();
        System.out.println("Kuyruk boyutu: " + gercekZamanli.size());
        while (it.hasNext()) {
            Process iteratorValue = (Process) it.next();
            System.out.println( iteratorValue.oncelik);
        }
        Iterator it1 = oncelik1.iterator();
        System.out.println("Kuyruk boyutu: " + oncelik1.size());
        while (it1.hasNext()) {
            Process iteratorValue = (Process) it1.next();
            System.out.println( iteratorValue.oncelik);
        }
        Iterator it2 = oncelik2.iterator();
        System.out.println("Kuyruk boyutu: " + oncelik2.size());
        while (it2.hasNext()) {
            Process iteratorValue = (Process) it2.next();
            System.out.println( iteratorValue.oncelik);
        }

        Iterator it3 = oncelik3.iterator();
        System.out.println("Kuyruk boyutu: " + oncelik3.size());
        while (it3.hasNext()) {
            Process iteratorValue = (Process) it3.next();
            System.out.println( iteratorValue.oncelik);
        }

		scanner.close();
	}
}