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
	List<Process> okunanProsesler;

	// S�n�fta kullan�lacak fonksiyonlar�n ger�eklenmesi kurucu fonksiyonda
	// yap�l�yor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {
		// TODO ornek.txt "dosya" parametresiyle de�i�tirilecek.
		scanner = new Scanner(new File("ornek.txt"));
		okunanProsesler = new ArrayList<Process>();
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
}
