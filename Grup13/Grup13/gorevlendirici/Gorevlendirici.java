package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//!!!!!!!!!!!! RENK KODLARI AMA WÝNDOWSTA ÇALIÞMIYOR :):)!!!!!!!!!!!!!!!!!!
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

	// Sýnýfta kullanýlacak fonksiyonlarýn gerçeklenmesi kurucu fonksiyonda
	// yapýlýyor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {
		// TODO ornek.txt "dosya" parametresiyle deðiþtirilecek.
		scanner = new Scanner(new File("ornek.txt"));
		okunanProsesler = new ArrayList<Process>();
	}

	// Dosyadan okuma yapan fonksiyon.
	public void Oku() {

		// Proses ID atanmasý için deðiþken tutuluyor.
		int pidSayac = 0;

		while (scanner.hasNextLine()) {
			// Satýr, gerekli parçalara ayrýlýyor.
			String[] tokens = scanner.nextLine().split(", ");

			// Bölünen parçalar, proseslere atanmasý için deðiþkenlere atanýyor.
			int varisZamani = Integer.parseInt(tokens[0]);
			int oncelik = Integer.parseInt(tokens[1]);
			int prosesZamani = Integer.parseInt(tokens[2]);

			// Proses ID, istenen formata dönüþtürülüyor.
			String pid = String.format("%03d", pidSayac);

			// Okunan satýrlar, proseslere dönüþtürülerek listeye ekleniyor.
			okunanProsesler.add(new Process(pid, varisZamani, oncelik, prosesZamani));

			// Proses ID'lerinin eþsiz olmasý için okunan her satýrda sayaç arttýrýlýyor.
			pidSayac++;
		}

		scanner.close();
	}
}
