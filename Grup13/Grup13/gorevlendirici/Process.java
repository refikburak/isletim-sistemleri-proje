package gorevlendirici;

public class Process {
	String pid;
	int varisZamani;
	int oncelik;
	int patlamaZamani;

	public Process(String pid, int varisZamani, int oncelik, int patlamaZamani) {
		this.pid = pid;
		this.oncelik = oncelik;
		this.patlamaZamani = patlamaZamani;
		this.varisZamani = varisZamani;
	}

	public void yazdir() {
		System.out.println("Oncelik:" + oncelik);
		System.out.println("Patlama:" + patlamaZamani);
		System.out.println("Varis:" + varisZamani);
	}
}