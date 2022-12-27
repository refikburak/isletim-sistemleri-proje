package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

//FCFS
//GER�BESLEME
//ROUNDROB�N
//RENK KODLARINI PROCESSSE PROP OLARAK VERME
//20 SAN�YE SONRA ZORTLATMA -KAMIL CAGLAR
//YAZDIRMA -KAMIL CAGLAR
//YAZDIRMA -KAMIL CAGLAR (Kamil basladi)

//OPSIYONELLER
//LIST YERINE QUEUEUEUE
//1 2 3 ONCELIKLILERI KULLANICI PROSESLERI DIYE BI ARRAYE ALMAK

//RAPOR 
//DOSYA YER�NE ARGS PARAMETRES�

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

//Process sinifina bir iki bir sey ekledim deneme icin o onemli degil simdilik
//ornek.txt den deneme yapabilmek icin simdilik proje dosyasinin icerisine yerlestirdim
//direkt gorevlendirici mainini calistiriyorum. 
//bir iki degiskenin ismi degismis olabilir proses yerine process falan elim yanlis gidiyor diye degistirdim
//3 oncelikliler olmayinca hocaninkiyle ayni gibi cikti, sorun cozulunce ekleme yapacagim.
//ayrica calistiri da oku fonk icinde cagirdim deneme yapabilmek icin gerekirse degistiririz.
public class Gorevlendirici {
	// Dosya okuma icin scanner sinifi
	Scanner scanner;

	// Program ilerlemesini tutmak ve yonetmek icin program sayaci
	int programSayaci = 0;

	// Proseslerin tutulup y�netilece�i kuyruklar
	List<Process> okunanProcessler;
	Queue<Process> gercekZamanli;
	Queue<Process> prosesOncelik1;
	Queue<Process> prosesOncelik2;
	Queue<Process> prosesOncelik3;

	// Sinifta kullanilacak fonksiyonlarin gerceklenmesi kurucu fonksiyonda
	// yapiliyor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {
		// TODO ornek.txt "dosya" parametresiyle degistirilecek.

		// Program sayaci icin deger atamasi yapiliyor.
		// programSayaci = 0;

		// Proses kuyruklari icin bos listeler tanimlaniyor.
		okunanProcessler = new ArrayList<Process>();
		gercekZamanli = new LinkedList<Process>();
		prosesOncelik1 = new LinkedList<Process>();
		prosesOncelik2 = new LinkedList<Process>();
		prosesOncelik3 = new LinkedList<Process>();
	}

	// Dosyadan okuma yapan ve gecici listeye atan fonksiyon
	public void Oku() throws FileNotFoundException {
		scanner = new Scanner(new File("ornek.txt"));

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
			int processZamani = Integer.parseInt(tokens[2]);

			// Okunan satirlar, proseslere donusturulerek listeye ekleniyor.
			okunanProcessler.add(new Process(pid, varisZamani, oncelik, processZamani));
			// Proses ID'lerinin essiz olmasi icin okunan her satirda sayac arttiriliyor.
			pidSayac++;
		}
		scanner.close();
		Calis();
	}

	// Varis suresi gelmis yani hazir prosesler, bu fonksiyonla gerekli kuyruklara
	// ekleniyor.
	public void KuyrugaEkle() {
		// Okunan proseslerin varis sureleri program sayaciyla karsilastiriliyor.
		// Program sayaci, sinifin bir elemani oldugu icin direkt erisilebilir halde, o
		// yuzden parametre ile erisilmesine gerek yok.
		for (Process process : okunanProcessler) {

			// Eger varis suresi program sayacina esitse, yani proses hazirsa; onceligine
			// gore kuyruga ekleniyor.
			if (process.varisZamani == programSayaci) {
				// Oncelik 0, gercek zamanli proseslerin kuyruga eklenmesi.
				if (process.oncelik == 0) {
					gercekZamanli.add(process);

				}

				// Oncelik 1, kullanici proseslerinin geri beslemeli kuyruga eklenmesi.
				else if (process.oncelik == 1) {
					prosesOncelik1.add(process);

				}
				// Oncelik 2, kullanici proseslerinin geri beslemeli kuyruga eklenmesi.
				else if (process.oncelik == 2) {
					prosesOncelik2.add(process);

				}
				// Oncelik 3, kullanici proseslerinin round-robin kuyruguna eklenmesi.
				else {
					prosesOncelik3.add(process);

				}
			}
		}
	}

	// Gercek zamanli prosesler icin Ilk Gelen Ilk Cal�s�r tipi siralayici fonksiyon
	public void FCFS() {
		// Kuyrugun ilk elemani calistiriliyor.
		gercekZamanli.peek().Calistir();

		// Eger ilk elemanin kalan suresi 0 olursa proses tamamlaniyor.
		if (gercekZamanli.peek().processZamani == 0) {
			gercekZamanli.remove();
		}
	}

	// Kullanici prosesleri icin (1. ve 2. oncelik) Geri Beslemeli tipi siralayici
	// fonksiyon
	public void GeriBesleme() {

		if (!(prosesOncelik1.isEmpty())) {
			prosesOncelik1.peek().Calistir();
			if (prosesOncelik1.peek().processZamani == 0) {
				prosesOncelik1.remove();
			} else {
				Process gecici = prosesOncelik1.poll();
				gecici.oncelik++;
				prosesOncelik2.add(gecici);
			}

		} else if (!(prosesOncelik2.isEmpty())) {
			prosesOncelik2.peek().Calistir();
			if (prosesOncelik2.peek().processZamani == 0) {
				prosesOncelik2.remove();
			} else {
				Process gecici = prosesOncelik2.poll();
				gecici.oncelik++;
				prosesOncelik3.add(gecici);
			}
		}

	}

	// Kullanici prosesleri icin (3. oncelik) Round-Robin tipi siralayici fonksion
	public void RoundRobin() {

		prosesOncelik3.peek().Calistir();
		if (prosesOncelik3.peek().processZamani == 0) {
			prosesOncelik3.remove();
		} else {
			Process gecici = prosesOncelik3.poll();
			prosesOncelik3.add(gecici);
		}

	}

	// GECICI VEYA KALICI OLARAK BURDA
	public void Calis() {
		// Programin kuyruklarda proses kalmayana kadar calismasi icin sonsuz dongu
		// olusturuluyor.
		Process geciciProcess = null;
		Process mevcutProcess = null;
		while (true) {
			// Program sayaci her arttiginda varis suresi dolmus bir proses varsa, gerekli
			// kuyruga eklenmesi icin gerekli fonksiyon cagiriliyor.
			// Bu fonksiyon cagirilirken, gecici liste icinde veri olup olmadigi kontrolu
			// yapiliyor.
			if (!(okunanProcessler.isEmpty())) {
				KuyrugaEkle();
				// System.out.println("denme1");
			}

			// Gercek zamanli proses listesi bos degilse FCFS siralayici kullaniliyor.
			if (!(gercekZamanli.isEmpty())) {
				mevcutProcess = new Process(gercekZamanli.peek());

				if (!mevcutProcess.dahaOnceCalistiMi) {
					mevcutProcess.baslamaZamani = programSayaci;
					mevcutProcess.dahaOnceCalistiMi = true;
				}

				FCFS();
			}

			// Gercek zamanli kuyruk bossa 1. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Geri Beslemeli siralayici kullaniliyor.
			else if (!(prosesOncelik1.isEmpty())) {

				mevcutProcess = new Process(prosesOncelik1.peek());

				if (!mevcutProcess.dahaOnceCalistiMi) {
					mevcutProcess.baslamaZamani = programSayaci;
					mevcutProcess.dahaOnceCalistiMi = true;
				}
				GeriBesleme();

			}

			// 1. oncelik kuyrugu bossa 1. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Geri Beslemeli siralayici kullaniliyor.
			else if (!(prosesOncelik2.isEmpty())) {
				mevcutProcess = new Process(prosesOncelik2.peek());
				if (!mevcutProcess.dahaOnceCalistiMi) {
					mevcutProcess.baslamaZamani = programSayaci;
					mevcutProcess.dahaOnceCalistiMi = true;
				}
				GeriBesleme();
				// System.out.println("denme4");

			}

			// 2. oncelik kuyrugu bossa 3. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Round Robin siralayici kullaniliyor.
			else if (!(prosesOncelik3.isEmpty())) {
				mevcutProcess = new Process(prosesOncelik3.peek());
				if (!mevcutProcess.dahaOnceCalistiMi) {
					mevcutProcess.baslamaZamani = programSayaci;
					mevcutProcess.dahaOnceCalistiMi = true;
				}
				RoundRobin();

			}

			// Eger 3. oncelik kuyrugu da bossa, tum kuyruklar kontrol edilmis oldugu icin
			// kuyrukta proses kalmamis oluyor. Bu sartlarda da program sonlaniyor ve sonsuz
			// dongumuz bitiriliyor.
			else {
				System.out.println("Program sonlandi.");
				break;
			}

//			System.out.println("YAZDIR");

			if (mevcutProcess != null && geciciProcess == null) {
				System.out.println(programSayaci + " sn proses basladi     id : " + mevcutProcess.pid + " oncelik: "
						+ mevcutProcess.oncelik + " kalan sure : " + mevcutProcess.processZamani);

			} else if (geciciProcess.pid == mevcutProcess.pid) {
				System.out.println(programSayaci + " sn proses yurutuluyor       id : " + mevcutProcess.pid
						+ " oncelik: " + mevcutProcess.oncelik + " kalan sure : " + mevcutProcess.processZamani);

			}

			else if (geciciProcess.pid != mevcutProcess.pid) {
				if (geciciProcess.processZamani == 0) {
					System.out.println(programSayaci + " sn proses sonlandi        id : " + geciciProcess.pid
							+ " oncelik: " + geciciProcess.oncelik + " kalan sure : " + geciciProcess.processZamani);

					System.out.println(programSayaci + " sn proses basladi        id : " + mevcutProcess.pid
							+ " oncelik: " + mevcutProcess.oncelik + " kalan sure : " + mevcutProcess.processZamani);
				} else if (geciciProcess.processZamani != 0) {

					System.out.println(programSayaci + " sn proses askida        id : " + geciciProcess.pid
							+ " oncelik: " + geciciProcess.oncelik + " kalan sure : " + geciciProcess.processZamani);

					System.out.println(programSayaci + " sn proses basladi        id : " + mevcutProcess.pid
							+ " oncelik: " + mevcutProcess.oncelik + " kalan sure : " + mevcutProcess.processZamani);

				}

			} else
				System.out.println("sictin");

			geciciProcess = mevcutProcess;
			programSayaci++;
		}
	}
}
