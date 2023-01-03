package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

//RAPOR 
//DOSYA YERï¿½NE ARGS PARAMETRESï¿½

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
	int programSayaci;

	// Proseslerin tutulup yonetilecegi kuyruklar
	List<Process> okunanProsesler;
	List<Process> gercekZamanli;
	List<Process> prosesOncelik1;
	List<Process> prosesOncelik2;
	List<Process> prosesOncelik3;

	// Yazdir fonksiyonuna yardimci olacak prosesler
	Process oncekiProses;
	Process islemOncesiProses;
	Process islemSonrasiProses;

	// Sinifta kullanilacak fonksiyonlarin gerceklenmesi kurucu fonksiyonda
	// yapiliyor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {
		// TODO ornek.txt "dosya" parametresiyle degistirilecek.

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
	public void Oku() throws FileNotFoundException {

		scanner = new Scanner(new File("ornek.txt"));

		// Proses ID atanmasi icin degisken tutuluyor.
		int pidSayac = 0;

		while (scanner.hasNextLine()) {
			// Satir, gerekli parcalara ayriliyor.
			String[] tokens = scanner.nextLine().split(", ");

			// Proses ID, istenen formata donusturuluyor.
			String pid = String.format("%04d", pidSayac);

			// Bolunen parcalar, proseslere atanmasi icin degiskenlere ataniyor.
			int varisZamani = Integer.parseInt(tokens[0]);
			int oncelik = Integer.parseInt(tokens[1]);
			int patlamaZamani = Integer.parseInt(tokens[2]);

			// Okunan satirlar, proseslere donusturulerek listeye ekleniyor.
			okunanProsesler.add(new Process(pid, varisZamani, oncelik, patlamaZamani));
			// Proses ID'lerinin essiz olmasi icin okunan her satirda sayac arttiriliyor.
			pidSayac++;
		}
		scanner.close();

		Calis();
	}

	// Varis suresi gelmis yani hazir prosesler, bu fonksiyonla gerekli kuyruklara
	// ekleniyor.
	public void KuyrugaEkle() {

		// Liste elemanlarina erisim icin iterator kullaniliyor.
		ListIterator<Process> it = okunanProsesler.listIterator();

		// Listede eleman oldugu surece donen while yapýsý
		while (it.hasNext()) {

			// Iterator'un gosterdigi proses, gecici bir proseste tutuluyor.
			Process temp = it.next();

			// Eger varis suresi program sayacina esitse, yani proses hazirsa; onceligine
			// gore kuyruga ekleniyor.
			if (temp.varisZamani == programSayaci) {

				// Prosesleri onceliklerine gore kuyruklama eklemek icin switch kullaniliyor.
				switch (temp.oncelik) {
				// Oncelik 0, gercek zamanli proseslerin kuyruga eklenmesi.
				case 0:
					gercekZamanli.add(temp);
					it.remove();
					break;
				// Oncelik 1, kullanici proseslerinin geri beslemeli kuyruga eklenmesi.
				case 1:
					prosesOncelik1.add(temp);
					it.remove();
					break;
				// Oncelik 2, kullanici proseslerinin geri beslemeli kuyruga eklenmesi.
				case 2:
					prosesOncelik2.add(temp);
					it.remove();
					break;
				// Oncelik 3, kullanici proseslerinin round-robin kuyruguna eklenmesi.
				case 3:
					prosesOncelik3.add(temp);
					it.remove();
					break;
				}
			}
		}
	}

	// Degistirilmesi cok muhtemel.
	public void ZamanAsimiKontrol() {
		int silinecekIndex = -1;

		for (Process p : prosesOncelik1) {
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {
				System.out.println(programSayaci + "sn proses zamanAsimi     id :" + p.pid + "oncelik: " + p.oncelik
						+ "kalan süre: " + p.patlamaZamani);
				silinecekIndex = (prosesOncelik1.indexOf(p));
			}
		}
		if (silinecekIndex != -1) {
			prosesOncelik1.remove(silinecekIndex);
			silinecekIndex = -1;
		}

		for (Process p : prosesOncelik2) {
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {
				System.out.println(programSayaci + "sn proses zamanAsimi     id :" + p.pid + "oncelik: " + p.oncelik
						+ "kalan süre: " + p.patlamaZamani);
				silinecekIndex = (prosesOncelik2.indexOf(p));
			}
		}
		if (silinecekIndex != -1) {
			prosesOncelik2.remove(silinecekIndex);
			silinecekIndex = -1;
		}

		for (Process p : prosesOncelik3) {
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {
				System.out.println(programSayaci + "sn proses zamanAsimi     id :" + p.pid + "oncelik: " + p.oncelik
						+ "kalan süre: " + p.patlamaZamani);
				silinecekIndex = (prosesOncelik3.indexOf(p));
			}
		}

		if (silinecekIndex != -1) {
			prosesOncelik3.remove(silinecekIndex);
			silinecekIndex = -1;
		}

	}

	// Gercek zamanli prosesler icin Ilk Gelen Ilk Calisir tipi siralayici
	// fonksiyon
	public void FCFS() {

		// Yazdirma amaciyla tutulan proses, islem yapilmadan onceki halini tutuyor.
		islemOncesiProses = new Process(gercekZamanli.get(0));

		// Kuyrugun ilk elemani calistiriliyor.
		gercekZamanli.get(0).Calistir();

		// Yazdirma amaciyla tutulan proses, islem yapildiktan sonraki halini tutuyor.
		islemSonrasiProses = new Process(gercekZamanli.get(0));

		// Eger ilk elemanin kalan suresi 0 olursa proses tamamlaniyor.
		if (gercekZamanli.get(0).patlamaZamani == 0) {

			// Tamamlanan proses, listeden kaldiriliyor.
			gercekZamanli.remove(0);
		}
	}

	// Kullanici prosesleri icin (1. ve 2. oncelik) Geri Beslemeli tipi siralayici
	// fonksiyon
	public void GeriBesleme() {

		// 1. oncelikli proses listesinin bos olma durumu kontrol ediliyor.
		// Eger bossa 2. oncelikli listede islem yapiliyor.
		if (!(prosesOncelik1.isEmpty())) {

			// Yazdirma amaciyla tutulan proses, islem yapilmadan onceki halini tutuyor.
			islemOncesiProses = new Process(prosesOncelik1.get(0));

			// Kuyrugun ilk elemani calistiriliyor.
			prosesOncelik1.get(0).Calistir();

			// Yazdirma amaciyla tutulan proses, islem yapildiktan sonraki halini tutuyor.
			islemSonrasiProses = new Process(prosesOncelik1.get(0));

			// Prosesin onceligi dusurulmeden once bitme durumu kontrol ediliyor.
			// Eger proses bitmediyse, onceligi dusurulup diger listeye atiliyor.
			if (prosesOncelik1.get(0).patlamaZamani != 0) {

				// Proses, oncelik listesinden cikariliyor.
				Process gecici = prosesOncelik1.remove(0);

				// Prosesin onceligi dusuruluyor.
				gecici.oncelik++;

				// Yazdirma amaciyla kopyalanan prosesin de onceligi dusuruluyor.
				islemSonrasiProses.oncelik++;

				// Proses, bir alt oncelikli listeye ekleniyor.
				prosesOncelik2.add(gecici);
			}

			else {
				prosesOncelik1.remove(0);
			}

		} else {
			prosesOncelik2.get(0).Calistir();
			islemSonrasiProses = new Process(prosesOncelik2.get(0));
			if (prosesOncelik2.get(0).patlamaZamani == 0) {
				prosesOncelik2.remove(0);
			} else {
				Process gecici = prosesOncelik2.remove(0);
				gecici.oncelik++;
				prosesOncelik3.add(gecici);
			}
		}

	}

	// Kullanici prosesleri icin (3. oncelik) Round-Robin tipi siralayici fonksion
	public void RoundRobin() {

		prosesOncelik3.get(0).Calistir();
		islemSonrasiProses = new Process(prosesOncelik3.get(0));
		if (prosesOncelik3.get(0).patlamaZamani == 0) {
			prosesOncelik3.remove(0);
		} else {
			Process gecici = prosesOncelik3.remove(0);
			prosesOncelik3.add(gecici);
		}

	}

	// GECICI VEYA KALICI OLARAK BURDA
	public void Calis() {
		// Programin kuyruklarda proses kalmayana kadar calismasi icin sonsuz dongu
		// olusturuluyor.

		while (true) {

			// Program sayaci her arttiginda varis suresi dolmus bir proses varsa, gerekli
			// kuyruga eklenmesi icin gerekli fonksiyon cagiriliyor.
			// Bu fonksiyon cagirilirken, gecici liste icinde veri olup olmadigi kontrolu
			// yapiliyor.
			if (!(okunanProsesler.isEmpty())) {
				KuyrugaEkle();
			}

			// Gercek zamanli proses listesi bos degilse FCFS siralayici kullaniliyor.
			if (!(gercekZamanli.isEmpty())) {

				if (!gercekZamanli.get(0).dahaOnceCalistiMi) {
					gercekZamanli.get(0).baslamaZamani = programSayaci;
					gercekZamanli.get(0).dahaOnceCalistiMi = true;
				}

				FCFS();
			}

			// Gercek zamanli kuyruk bossa 1. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Geri Beslemeli siralayici kullaniliyor.
			else if (!(prosesOncelik1.isEmpty())) {

				if (!prosesOncelik1.get(0).dahaOnceCalistiMi) {
					prosesOncelik1.get(0).baslamaZamani = programSayaci;
					prosesOncelik1.get(0).dahaOnceCalistiMi = true;
				}
				islemOncesiProses = new Process(prosesOncelik1.get(0));
				GeriBesleme();

			}

			// 1. oncelik kuyrugu bossa 1. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Geri Beslemeli siralayici kullaniliyor.
			else if (!(prosesOncelik2.isEmpty())) {
				if (!prosesOncelik2.get(0).dahaOnceCalistiMi) {
					prosesOncelik2.get(0).baslamaZamani = programSayaci;
					prosesOncelik2.get(0).dahaOnceCalistiMi = true;
				}
				GeriBesleme();
			}

			// 2. oncelik kuyrugu bossa 3. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Round Robin siralayici kullaniliyor.
			else if (!(prosesOncelik3.isEmpty())) {
				if (!prosesOncelik3.get(0).dahaOnceCalistiMi) {
					prosesOncelik3.get(0).baslamaZamani = programSayaci;
					prosesOncelik3.get(0).dahaOnceCalistiMi = true;
				}
				islemOncesiProses = new Process(prosesOncelik3.get(0));
				RoundRobin();

			}

			// Eger 3. oncelik kuyrugu da bossa, tum kuyruklar kontrol edilmis oldugu icin
			// kuyrukta proses kalmamis oluyor. Bu sartlarda da program sonlaniyor ve sonsuz
			// dongumuz bitiriliyor.
			else {
				System.out.println("Program sonlandi.");
				break;
			}
			ZamanAsimiKontrol();
			Yazdir();

			oncekiProses = new Process(islemSonrasiProses);

			programSayaci++;
		}
	}

	public void Yazdir() {

		if (islemOncesiProses != null && oncekiProses == null) { // sadece ilk prosesin yazdýrýlmasýnda kullanýlýr.
																	// Gecici
																	// prosese daha atama yapýlmadýgý icin kosul dogru
																	// calismakta
			System.out.println(programSayaci + ".0000 sn proses baþladý     id : " + islemOncesiProses.pid
					+ " oncelik: " + islemOncesiProses.oncelik + " kalan sure : " + islemOncesiProses.patlamaZamani);

		} else if (oncekiProses.pid == islemOncesiProses.pid)// gecici(onceki) proses ve simdiki proses ayniysa sonlanma
																// veya kesilme olmamis demektir
		{
			System.out.println(programSayaci + ".0000 sn proses yürütülüyor       id : " + islemOncesiProses.pid
					+ " oncelik: " + islemOncesiProses.oncelik + " kalan sure : " + islemOncesiProses.patlamaZamani);

		}

		else if (oncekiProses.pid != islemOncesiProses.pid) { // sonlanma veya kesilme durumu mevcut
			if (oncekiProses.patlamaZamani == 0) { // sonlanma durumu
				System.out.println(programSayaci + ".0000 sn proses sonlandi        id : " + oncekiProses.pid
						+ " oncelik: " + oncekiProses.oncelik + " kalan sure : " + oncekiProses.patlamaZamani);

				System.out.println(
						programSayaci + ".0000 sn proses basladi        id : " + islemOncesiProses.pid + " oncelik: "
								+ islemOncesiProses.oncelik + " kalan sure : " + islemOncesiProses.patlamaZamani);

			} else if (oncekiProses.patlamaZamani != 0) {// kesilme durumu

				System.out.println(programSayaci + ".0000 sn proses askida        id : " + oncekiProses.pid
						+ " oncelik: " + oncekiProses.oncelik + " kalan sure : " + oncekiProses.patlamaZamani);

				System.out.println(
						programSayaci + ".0000 sn proses basladi        id : " + islemOncesiProses.pid + " oncelik: "
								+ islemOncesiProses.oncelik + " kalan sure : " + islemOncesiProses.patlamaZamani);

			}

		} else
			System.out.println("sictin");
	}
}
