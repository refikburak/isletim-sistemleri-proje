package gorevlendirici;

public class Process {
	String pid;
	int varisZamani;
	int oncelik;
	int processZamani;
	boolean dahaOnceCalistiMi; //1 ise basladi,2 ise askida,3 ise yurutuluyor,4 sonlandi

	public Process(String pid, int varisZamani, int oncelik, int processZamani) {
		this.pid = pid;
		this.oncelik = oncelik;
		this.processZamani = processZamani;
		this.varisZamani = varisZamani;
	}

}