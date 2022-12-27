package gorevlendirici;

public class Process {
	String pid;
	int varisZamani;
	int oncelik;
	int processZamani;
	boolean dahaOnceCalistiMi = false; // 1 ise basladi,2 ise askida,3 ise yurutuluyor,4 sonlandi
	int baslamaZamani;

	public Process(String pid, int varisZamani, int oncelik, int processZamani) {
		this.pid = pid;
		this.oncelik = oncelik;
		this.processZamani = processZamani;
		this.varisZamani = varisZamani;
	}

	public Process(Process p) {
		this.pid = p.pid;
		this.oncelik = p.oncelik;
		this.processZamani = p.processZamani;
		this.varisZamani = p.varisZamani;
		this.dahaOnceCalistiMi = p.dahaOnceCalistiMi;
		this.baslamaZamani = p.baslamaZamani;
	}

	public void Calistir() {
		processZamani = processZamani - 1;
	}

}