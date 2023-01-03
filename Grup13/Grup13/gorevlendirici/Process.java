package gorevlendirici;

public class Process {

	// Proses ID'leri istenen formata getirilebilmesi icin String tutuluyor.
	String pid;
	int varisZamani;
	int oncelik;
	int patlamaZamani;

	// Kontroller icin gerekli propertyler
	int baslamaZamani;
	boolean dahaOnceCalistiMi = false;

	// Dosyadan okunan prosesler icin parametrelerin alindigi kurucu fonksiyon.
	public Process(String pid, int varisZamani, int oncelik, int patlamaZamani) {
		this.pid = pid;
		this.oncelik = oncelik;
		this.patlamaZamani = patlamaZamani;
		this.varisZamani = varisZamani;
	}

	// Kontroller icin kullanilan prosesleri kopyalayabilmek icin kullanilan kurucu
	// fonksiyon.
	public Process(Process p) {
		this.pid = p.pid;
		this.oncelik = p.oncelik;
		this.patlamaZamani = p.patlamaZamani;
		this.varisZamani = p.varisZamani;
		this.dahaOnceCalistiMi = p.dahaOnceCalistiMi;
		this.baslamaZamani = p.baslamaZamani;
	}

	// Prosesin patlama zamanini azaltan yani bir cycle calistiran fonksiyon.
	public void Calistir() {
		patlamaZamani -= 1;

		// Daha once calistigini belirten propertye calistigini belirten atama.
		if (!dahaOnceCalistiMi) {
			dahaOnceCalistiMi = true;
		}
	}

}