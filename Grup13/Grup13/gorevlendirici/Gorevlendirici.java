package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//!!!!!!!!!!!! RENK KODLARI AMA W�NDOWSTA �ALI�MIYOR :):)!!!!!!!!!!!!!!!!!!
//public static final String ANSI_RESET = "\u001B[0m";
//public static final String ANSI_BLACK = "\u001B[30m";
//public static final String ANSI_RED = "\u001B[31m";
//public static final String ANSI_GREEN = "\u001B[32m";
//public static final String ANSI_YELLOW = "\u001B[33m";
//public static final String ANSI_BLUE = "\u001B[34m";
//public static final String ANSI_PURPLE = "\u001B[35m";
//public static final String ANSI_CYAN = "\u001B[36m";
//public static final String ANSI_WHITE = "\u001B[37m";

public class Gorevlendirici {
	Scanner scanner;
	int programSayaci;
	List<Process> okunanProsesler;
	List<Process> gercekZamanli;
	List<Process> prosesOncelik1;
	List<Process> prosesOncelik2;
	List<Process> prosesOncelik3;

	// S�n�fta kullan�lacak fonksiyonlar�n ger�eklenmesi kurucu fonksiyonda
	// yap�l�yor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {
		// TODO ornek.txt "dosya" parametresiyle de�i�tirilecek.
		scanner = new Scanner(new File("ornek.txt"));

		// Proses kuyruklar� i�in bo� listeler tan�mlan�yor.
		okunanProsesler = new ArrayList<Process>();
		gercekZamanli = new ArrayList<Process>();
		prosesOncelik1 = new ArrayList<Process>();
		prosesOncelik2 = new ArrayList<Process>();
		prosesOncelik3 = new ArrayList<Process>();

		// Program sayac� i�in de�er atamas� yap�l�yor.
		programSayaci = 0;
	}

	// Dosyadan okuma yapan fonksiyon.
	public void Oku() {

		// Proses ID atanmas� i�in de�i�ken tutuluyor.
		int pidSayac = 0;

		while (scanner.hasNextLine()) {
			// Sat�r, gerekli par�alara ayr�l�yor.
			String[] tokens = scanner.nextLine().split(", ");

			// B�l�nen par�alar, proseslere atanmas� i�in de�i�kenlere atan�yor.
			int varisZamani = Integer.parseInt(tokens[0]);
			int oncelik = Integer.parseInt(tokens[1]);
			int prosesZamani = Integer.parseInt(tokens[2]);

			// Proses ID, istenen formata d�n��t�r�l�yor.
			String pid = String.format("%03d", pidSayac);

			// Okunan sat�rlar, proseslere d�n��t�r�lerek listeye ekleniyor.
			okunanProsesler.add(new Process(pid, varisZamani, oncelik, prosesZamani));

			// Proses ID'lerinin e�siz olmas� i�in okunan her sat�rda saya� artt�r�l�yor.
			pidSayac++;
		}
		scanner.close();
	}

	// Var�� s�resi gelmi� yani haz�r prosesler, bu fonksiyonla gerekli kuyruklara
	// ekleniyor.
	public void KuyrugaEkle() {

		// Okunan proseslerin var�� s�releri program sayac�yla kar��la�t�r�l�yor.
		// Program sayac�, s�n�f�n bir eleman� oldu�u i�in direkt eri�ilebilir halde, o
		// y�zden parametre ile eri�ilmesine gerek yok.
		for (Process process : okunanProsesler) {

			// E�er var�� s�resi program sayac�na e�itse, yani proses haz�rsa; �nceli�ine
			// g�re kuyru�a ekleniyor.
			if (process.varisZamani == programSayaci) {

				// �ncelik 0, ger�ek zamanl� proseslerin kuyru�a eklenmesi.
				if (process.oncelik == 0)
					gercekZamanli.add(process);

				// �ncelik 1, kullan�c� proseslerinin geri beslemeli kuyru�a eklenmesi.
				else if (process.oncelik == 1)
					prosesOncelik1.add(process);

				// �ncelik 2, kullan�c� proseslerinin geri beslemeli kuyru�a eklenmesi.
				else if (process.oncelik == 2)
					prosesOncelik2.add(process);

				// �ncelik 3, kullan�c� proseslerinin round-robin kuyru�una eklenmesi.
				else
					prosesOncelik3.add(process);
			}
		}
	}
}
