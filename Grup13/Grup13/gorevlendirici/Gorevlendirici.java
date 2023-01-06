package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

//public static final String ANSI_BLACK = "\u001B[30m";
//public static final String ANSI_RED = "\u001B[31m";
//public static final String ANSI_GREEN = "\u001B[32m";
//public static final String ANSI_YELLOW = "\u001B[33m";
//public static final String ANSI_BLUE = "\u001B[34m";
//public static final String ANSI_PURPLE = "\u001B[35m";
//public static final String ANSI_CYAN = "\u001B[36m";
//public static final String ANSI_WHITE = "\u001B[37m";

public class Gorevlendirici {
	// Dosya okuma icin scanner sinifi
	Scanner scanner;

	// Program ilerlemesini tutmak ve yonetmek icin program sayaci
	int programSayaci;
	//Renk kodlarinin satir sonunda resetlenebilmesi icin reset kodu
	public static final String ANSI_RESET = "\u001B[0m";

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

	// Sinifta kullanilacak fonksiyonlarin gerceklenmesi kurucu fonksiyonda yapiliyor.
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

		// Renk kodlari icin liste tanimlaniyor.
		String[] renkKodlari = new String[] { "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m",
				"\u001B[36m" };

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
			// Renk kodlari, arka arkaya ayni renklerin gelmemesi icin atanan ID'lerin mod
			// degerlerine gore ataniyor.
			okunanProsesler.add(new Process(pid, varisZamani, oncelik, patlamaZamani, renkKodlari[pidSayac % 6]));

			// Proses ID'lerinin essiz olmasi icin okunan her satirda sayac arttiriliyor.
			pidSayac++;
		}
		scanner.close();
	}

	// Varis suresi gelmis yani hazir prosesler, bu fonksiyonla gerekli kuyruklara ekleniyor.
	public void KuyrugaEkle() {

		// Liste elemanlarina erisim icin iterator kullaniliyor.
		ListIterator<Process> it = okunanProsesler.listIterator();

		// Listede eleman oldugu surece donen while yapisi kuruluyor.
		while (it.hasNext()) {

			// Iterator'un gosterdigi proses, gecici bir proseste tutuluyor.
			Process temp = it.next();

			// Eger varis suresi program sayacina esitse, yani proses hazirsa; onceligine gore kuyruga ekleniyor.
			if (temp.varisZamani == programSayaci) {

				// Prosesleri onceliklerine gore kuyruklama eklemek icin switch-case yapisi kullaniliyor.
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

	// 20 saniye kontrolu yapılarak proseslerin sonlandırılması saglanıyor.
	public void ZamanAsimiKontrol() {
		int silinecekIndex = -1;

		for (Process p : prosesOncelik1) {
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {
				System.out.println(p.renkKodu + " " +  programSayaci  + ".0000 sn proses zaman aşımı     id :" + p.pid + "öncelik: "
						+ p.oncelik + "kalan süre: " + p.patlamaZamani+ANSI_RESET);
				silinecekIndex = (prosesOncelik1.indexOf(p));
			}
		}
		if (silinecekIndex != -1) {
			prosesOncelik1.remove(silinecekIndex);
			silinecekIndex = -1;
		}

		for (Process p : prosesOncelik2) {
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {
				System.out.println(p.renkKodu + " " + programSayaci + ".0000 sn proses zaman aşımı     id :" + p.pid + "öncelik: "
						+ p.oncelik + "kalan süre: " + p.patlamaZamani+ ANSI_RESET);
				silinecekIndex = (prosesOncelik2.indexOf(p));
			}
		}
		if (silinecekIndex != -1) {
			prosesOncelik2.remove(silinecekIndex);
			silinecekIndex = -1;
		}

		for (Process p : prosesOncelik3) {
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {
				System.out.println(p.renkKodu + " " + programSayaci + ".0000 sn proses zaman aşımı     id :" + p.pid + "öncelik: "
						+ p.oncelik + "kalan süre: " + p.patlamaZamani + ANSI_RESET);
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

		// Islem yapilacak proses, okunabilirligi artirmak adina tanimlanip atama yapiliyor.
		Process islemYapilanProses = gercekZamanli.get(0);

		// Yazdirma amaciyla tutulan proses, islem yapilmadan onceki halini tutuyor.
		islemOncesiProses = new Process(islemYapilanProses);

		// Kuyrugun ilk elemani calistiriliyor.
		islemYapilanProses.Calistir();

		// Yazdirma amaciyla tutulan proses, islem yapildiktan sonraki halini tutuyor.
		islemSonrasiProses = new Process(islemYapilanProses);

		// Eger ilk elemanin kalan suresi 0 olursa proses tamamlaniyor.
		if (islemYapilanProses.patlamaZamani == 0)

			// Tamamlanan proses, listeden kaldiriliyor.
			gercekZamanli.remove(islemYapilanProses);

	}

	// Kullanici prosesleri icin (1. ve 2. oncelik) Geri Beslemeli tipi siralayici
	public void GeriBesleme() {

		// Islem yapilacak prosese kolay erisebilmek adina proses tanimlaniyor.
		Process islemYapilanProses;

		// 1. oncelikli proses listesinin bos olma durumu kontrol ediliyor.
		// 1. oncelik listesi bos degilse islem yapiliyor.
		if (!(prosesOncelik1.isEmpty())) {

			// Islem yapilacak prosese atama yapiliyor.
			islemYapilanProses = prosesOncelik1.get(0);

			// Yazdirma islemi icin tutulan prosesin islem yapilmadan onceki hali tutuluyor
			islemOncesiProses = new Process(islemYapilanProses);

			// Kuyrugun ilk elemani calistiriliyor.
			islemYapilanProses.Calistir();

			// Yazdirma amaciyla tutulan prosesin islem yapildiktan sonraki hali tutuluyor.
			islemSonrasiProses = new Process(islemYapilanProses);

			// Prosesin onceligi dusurulmeden once bitme durumu kontrol ediliyor.
			// Eger proses bitmediyse, onceligi dusurulup diger listeye atiliyor.
			if (islemYapilanProses.patlamaZamani != 0) {

				// Proses, oncelik listesinden cikariliyor.
				prosesOncelik1.remove(islemYapilanProses);

				// Prosesin onceligi dusuruluyor.
				islemYapilanProses.oncelik++;

				// Yazdirma amaciyla kopyalanan prosesin de onceligi dusuruluyor.
				islemSonrasiProses.oncelik++;

				// Proses, bir alt oncelikli listeye ekleniyor.
				prosesOncelik2.add(islemYapilanProses);
			}

			// Eger proses bittiyse, oncelik dusurulmeden listeden cikariliyor.
			else
				prosesOncelik1.remove(islemYapilanProses);
		}

		// 1. oncelik listesi bossa, 2. oncelik listesinde islem yapiliyor.
		else {

			// Islem yapilacak prosese atama yapiliyor.
			islemYapilanProses = prosesOncelik2.get(0);

			// Yazdirma amaciyla tutulan proses, islem yapilmadan onceki halini tutuyor.
			islemOncesiProses = new Process(islemYapilanProses);

			// Kuyrugun ilk elemani calistiriliyor.
			islemYapilanProses.Calistir();

			// Yazdirma amaciyla tutulan proses, islem yapildiktan sonraki halini tutuyor.
			islemSonrasiProses = new Process(islemYapilanProses);

			// Prosesin onceligi dusurulmeden once bitme durumu kontrol ediliyor.
			// Eger proses bitmediyse, onceligi dusurulup diger listeye atiliyor.
			if (islemYapilanProses.patlamaZamani != 0) {

				// Proses, oncelik listesinden cikariliyor.
				prosesOncelik2.remove(islemYapilanProses);

				// Prosesin onceligi dusuruluyor.
				islemYapilanProses.oncelik++;

				// Yazdirma amaciyla kopyalanan prosesin de onceligi dusuruluyor.
				islemSonrasiProses.oncelik++;

				// Proses, bir alt oncelikli listeye ekleniyor.
				prosesOncelik3.add(islemYapilanProses);
			}

			// Eger proses bittiyse, oncelik dusurulmeden listeden cikariliyor.
			else
				prosesOncelik2.remove(islemYapilanProses);
		}
	}

	// Kullanici prosesleri icin (3. oncelik) Round-Robin tipi siralayici fonksion
	public void RoundRobin() {

		// Islem yapilacak proses, okunabilirligi artirmak adina tanimlanip atama
		// yapiliyor.
		Process islemYapilanProses = prosesOncelik3.get(0);

		// Yazdirma amaciyla tutulan proses, islem yapilmadan onceki halini tutuyor.
		islemOncesiProses = new Process(islemYapilanProses);

		// Kuyrugun ilk elemani calistiriliyor.
		islemYapilanProses.Calistir();

		// Yazdirma amaciyla tutulan proses, islem yapildiktan sonraki halini tutuyor.
		islemSonrasiProses = new Process(islemYapilanProses);

		// Proses, listenin sonuna kaydirilmadan once bitme durumu kontrol ediliyor.
		// Eger proses bitmediyse, listenin sonuna kaydiriliyor.
		if (islemYapilanProses.patlamaZamani != 0) {

			// Proses, listede tekrar yasanmamasi icin listeden cikariliyor.
			prosesOncelik3.remove(islemYapilanProses);

			// Proses, listenin sonuna ekleniyor.
			prosesOncelik3.add(islemYapilanProses);
		}

		// Eger proses bittiyse, liste sonuna kaydirilmadan listeden cikariliyor.
		else
			prosesOncelik3.remove(islemYapilanProses);
	}

	//Proseslerin calistirilma islemi gercekleniyor.
	public void Calis() {
		// Programin kuyruklarda proses kalmayana kadar calismasi icin sonsuz dongu olusturuluyor.
		while (true) {

			// Program sayaci her arttiginda varis suresi dolmus bir proses varsa, gerekli
			// kuyruga eklenmesi icin gerekli fonksiyon cagiriliyor.
			if (!(okunanProsesler.isEmpty())) {

				// Bu fonksiyon cagirilirken, gecici liste icinde veri olup olmadigi kontrolu yapiliyor.
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
			// kuyrukta proses kalmamis oluyor. Bu sartlarda da program sonlaniyor ve sonsuz dongu bitiriliyor.
			else if (okunanProsesler.isEmpty()) {
				System.out.println("Program sonlandi.");
				break;
			}

			//20 saniye kontrolu ile prosesin sonlanip sonlanmayacagi durumu saglaniliyor.
			ZamanAsimiKontrol();
			//Prosesin anlik durumu yazdiriliyor.
			Yazdir();
			
			//Onceki processe atama saglanarak son islem yapilan process elde tutuluypr.
			oncekiProses = new Process(islemSonrasiProses);
			//Saniye(program sayaci) arttiriliyor.
			programSayaci++;
		}
	}
	//Istenilen bicimde output icin yazdirma islemi yapiliyor.
	public void Yazdir() {
		
		//Ilk prosesin yazdirilmasinda kullaniliyor
		if (islemOncesiProses != null && oncekiProses == null) { 
			// Gecici
			// prosese daha atama yapilmadigi icin kosul dogru
			// calismakta
			System.out.println(islemOncesiProses.renkKodu+" "+programSayaci + ".0000 sn proses başladı     id : " + islemOncesiProses.pid
					+ " öncelik: " + islemOncesiProses.oncelik + " kalan süre : " + islemOncesiProses.patlamaZamani + ANSI_RESET);

		} else if (oncekiProses.pid == islemOncesiProses.pid)// gecici(onceki) proses ve simdiki proses ayniysa sonlanma
																// veya kesilme olmamis demektir
		{
			System.out.println(islemOncesiProses.renkKodu+" "+programSayaci + ".0000 sn proses yürütülüyor       id : " + islemOncesiProses.pid
					+ " öncelik: " + islemOncesiProses.oncelik + " kalan süre : " + islemOncesiProses.patlamaZamani + ANSI_RESET);

		}

		else if (oncekiProses.pid != islemOncesiProses.pid) { // sonlanma veya kesilme durumu mevcut
			if (oncekiProses.patlamaZamani == 0) { // sonlanma durumu
				System.out.println(oncekiProses.renkKodu+" "+programSayaci + ".0000 sn proses sonlandı        id : " + oncekiProses.pid
						+ " öncelik: " + oncekiProses.oncelik + " kalan süre : " + oncekiProses.patlamaZamani + ANSI_RESET);

				System.out.println(islemOncesiProses.renkKodu+" "+
						programSayaci + ".0000 sn proses başladı        id : " + islemOncesiProses.pid + " öncelik: "
								+ islemOncesiProses.oncelik + " kalan süre : " + islemOncesiProses.patlamaZamani + ANSI_RESET);

			} else if (oncekiProses.patlamaZamani != 0) {// kesilme durumu

				System.out.println(oncekiProses.renkKodu+" "+programSayaci + ".0000 sn proses askıda        id : " + oncekiProses.pid
						+ "öncelik: " + oncekiProses.oncelik + " kalan süre : " + oncekiProses.patlamaZamani + ANSI_RESET);

				System.out.println(islemOncesiProses.renkKodu+" "+
						programSayaci + ".0000 sn proses başladı        id : " + islemOncesiProses.pid + " öncelik: "
								+ islemOncesiProses.oncelik + " kalan süre : " + islemOncesiProses.patlamaZamani + ANSI_RESET);

			}

		} else
			System.out.println("sictin");
	}
}
