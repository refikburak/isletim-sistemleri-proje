package process;

public class Process {
	int varisZamani;
	int oncelik;
	int patlamaZamani;
	 public Process(String varisZamani,String oncelik, String patlamaZamani) {
		 this.oncelik=Integer.parseInt(oncelik);
		 this.patlamaZamani=Integer.parseInt(patlamaZamani);
		 this.varisZamani=Integer.parseInt(varisZamani);
	 }
	 
	 public void yazdir() {
		 System.out.println("Oncelik:"+oncelik);		 
		 System.out.println("Patlama:"+patlamaZamani);
		 System.out.println("Varis:"+varisZamani);
}
}