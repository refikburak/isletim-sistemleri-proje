package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//!!!!!!!!!!!! RENK KODLARI AMA WINDOWSTA CALISMIYOR :):)!!!!!!!!!!!!!!!!!!
//public static final String ANSI_RESET = "\u001B[0m";
//public static final String ANSI_BLACK = "\u001B[30m";
//public static final String ANSI_RED = "\u001B[31m";
//public static final String ANSI_GREEN = "\u001B[32m";
//public static final String ANSI_YELLOW = "\u001B[33m";
//public static final String ANSI_BLUE = "\u001B[34m";
//public static final String ANSI_PURPLE = "\u001B[35m";
//public static final String ANSI_CYAN = "\u001B[36m";
//public static final String ANSI_WHITE = "\u001B[37m";

//SIRALAMA ALGORITMALARINI YAZARKEN VARIS ZAMANI VEYA ONCELIK KONTROL ETMENIZE GEREK YOK. 
//BU KUYRUKLARA PROSESLER DUZENLI OLARAK EKLENIYOR. HEP 0. INDEXTEKILERI CALISTIRICAZ. GEREKLI ISLEMLERI YAPTIRICAZ
//HERHANGI BIR SEKILDE VARIS ZAMANI VEYA ONCELIK KONTROLUNE GEREK YOK.
//List<Process> gercekZamanli; VARIS ZAMANI DOLMUS VE ONCELIGI 0 OLAN PROSESLER BURDA, EGER VARIS ZAMANI DOLMAMISSA EKLENMIYOR. VARIS ZAMANI DOLANLAR SONRADAN EKLENIYOR
//List<Process> prosesOncelik1; AYNI SEKILDE VARIS ZAMANI DOLANLAR SONRADAN EKLENIYOR. ONCELIK 1 OLANLAR BURDA
//List<Process> prosesOncelik2; ONCELIK 2 LER BURDA
//List<Process> prosesOncelik3; ONCELIK 3 LER BURDA
//GENEL CALISIR HALDE YAZILACAK. ICERIDE PROSESLER VARMIS VE HER KOSULDA CALISACAKMIS GIBI YAZILACAK.
// PROGRAM SAYACI KONTROLUNU MAIN DOSYASINDA YAPALIM DIYORUM. SAYACI ORDAN ARTTIRIP SANIYE ILERLETMIS GIBI ORADAN HALLEDERIZ.
//ALGORITMALAR ICINE BEKLEME EKLEMEYE GEREK OLDUGUNU DUSUNMUYORUM. BEKLEMEYI PROSESLERIN ICINE EKLERIZ.

public class Gorevlendirici {
	// Dosya okuma icin scanner sinifi
	Scanner scanner;

	// Program ilerlemesini tutmak ve yonetmek icin program sayaci
	int programSayaci;

	// Proseslerin tutulup yönetileceði kuyruklar
	List<Process> okunanProsesler;
	List<Process> gercekZamanli;
	List<Process> prosesOncelik1;
	List<Process> prosesOncelik2;
	List<Process> prosesOncelik3;

	// Sinifta kullanilacak fonksiyonlarin gerceklenmesi kurucu fonksiyonda
	// yapiliyor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {
		// TODO ornek.txt "dosya" parametresiyle degistirilecek.
		scanner = new Scanner(new File("ornek.txt"));

		// Program sayaci icin deger atamasi yapiliyor.
		programSayaci = 0;

		// Proses kuyruklari icin bos listeler tanimlaniyor.
		okunanProsesler = new ArrayList<Process>();
		gercekZamanli = new ArrayList<Process>();
		prosesOncelik1 = new ArrayList<Process>();
		prosesOncelik2 = new ArrayList<Process>();
		prosesOncelik3 = new ArrayList<Process>();
	}

	// Dosyadan okuma yapan ve gecici listeye atan fonksiyon
	public void Oku() {

		// Proses ID atanmasi icin degisken tutuluyor.
		int pidSayac = 0;

		while (scanner.hasNextLine()) {
			// Satir, gerekli parcalara ayriliyor.
			String[] tokens = scanner.nextLine().split(", ");

			// Proses ID, istenen formata donusturuluyor.
			String pid = String.format("%03d", pidSayac);

			// Bolunen parcalar, proseslere atanmasi icin degiskenlere ataniyor.
			int varisZamani = Integer.parseInt(tokens[0]);
			int oncelik = Integer.parseInt(tokens[1]);
			int prosesZamani = Integer.parseInt(tokens[2]);

			// Okunan satirlar, proseslere donusturulerek listeye ekleniyor.
			okunanProsesler.add(new Process(pid, varisZamani, oncelik, prosesZamani));

			// Proses ID'lerinin essiz olmasi icin okunan her satirda sayac arttiriliyor.
			pidSayac++;
		}
		scanner.close();
	}

	// Varis suresi gelmis yani hazir prosesler, bu fonksiyonla gerekli kuyruklara
	// ekleniyor.
	public void KuyrugaEkle() {

		// Okunan proseslerin varis sureleri program sayaciyla karsilastiriliyor.
		// Program sayaci, sinifin bir elemani oldugu icin direkt erisilebilir halde, o
		// yuzden parametre ile erisilmesine gerek yok.
		for (Process process : okunanProsesler) {

			// Eger varis suresi program sayacina esitse, yani proses hazirsa; onceligine
			// gore kuyruga ekleniyor.
			if (process.varisZamani == programSayaci) {

				// Oncelik 0, gercek zamanli proseslerin kuyruga eklenmesi.
				if (process.oncelik == 0)
					gercekZamanli.add(process);

				// Oncelik 1, kullanici proseslerinin geri beslemeli kuyruga eklenmesi.
				else if (process.oncelik == 1)
					prosesOncelik1.add(process);

				// Oncelik 2, kullanici proseslerinin geri beslemeli kuyruga eklenmesi.
				else if (process.oncelik == 2)
					prosesOncelik2.add(process);

				// Oncelik 3, kullanici proseslerinin round-robin kuyruguna eklenmesi.
				else
					prosesOncelik3.add(process);
			}
		}
	}

	// Gercek zamanli prosesler icin Ilk Gelen Ilk Calýsýr tipi siralayici fonksiyon
	public void FCFS() {
//		if(!gercekZamanli.isEmpty()) {
//			gercekZamanli.get(0).Calistir();
//		}
	}

	// Kullanici prosesleri icin (1. ve 2. oncelik) Geri Beslemeli tipi siralayici
	// fonksiyon
	public void GeriBesleme() {

	}

	// Kullanici prosesleri icin (3. oncelik) Round-Robin tipi siralayici fonksion
	public void RoundRobin() {

	}
}
