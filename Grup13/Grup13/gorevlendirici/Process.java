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

	public void Calistir() throws InterruptedException {
		patlamaZamani -= 1;
		Thread.sleep(1000);
	}
}