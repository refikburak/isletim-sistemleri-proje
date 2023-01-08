package gorevlendirici;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

	// Parametre olarak alinacak dosya
	String dosya;

	// Program ilerlemesini tutmak ve yonetmek icin program sayaci
	int programSayaci;

	// Renk kodlarinin satir sonunda resetlenebilmesi icin reset kodu
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

	// Sinifta kullanilacak fonksiyonlarin gerceklenmesi kurucu fonksiyonda
	// yapiliyor.
	public Gorevlendirici(String dosya) throws FileNotFoundException {

		// Program sayaci icin deger atamasi yapiliyor.
		programSayaci = 0;

		// Proses kuyruklari icin bos listeler tanimlaniyor.
		okunanProsesler = new ArrayList<Process>();
		gercekZamanli = new ArrayList<Process>();
		prosesOncelik1 = new ArrayList<Process>();
		prosesOncelik2 = new ArrayList<Process>();
		prosesOncelik3 = new ArrayList<Process>();

		// Dosya adi atamasi yapiliyor.
		this.dosya = dosya;
	}

	// Dosyadan okuma yapan ve gecici listeye atan fonksiyon
	public void Oku() throws FileNotFoundException {

		scanner = new Scanner(new File(dosya));

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

	// Varis suresi gelmis yani hazir prosesler, bu fonksiyonla gerekli kuyruklara
	// ekleniyor.
	public void KuyrugaEkle() {

		// Liste elemanlarina erisim icin iterator kullaniliyor.
		ListIterator<Process> it = okunanProsesler.listIterator();

		// Listede eleman oldugu surece donen while yapisi kuruluyor.
		while (it.hasNext()) {

			// Iterator'un gosterdigi proses, gecici bir proseste tutuluyor.
			Process temp = it.next();

			// Eger varis suresi program sayacina esitse, yani proses hazirsa; onceligine
			// gore kuyruga ekleniyor.
			if (temp.varisZamani == programSayaci) {

				// Prosesleri onceliklerine gore kuyruklama eklemek icin switch-case yapisi
				// kullaniliyor.
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

	// 20 saniye kontrolu yapilarak proseslerin sonlandirilmasi saglaniyor.
	public void ZamanAsimiKontrol() throws Exception {

		// Silinecek index degiskeni tanimlanip atamasi yapiliyor.
		// -1'e esitlenme sebebi, eger index bulunamazsa veya zaman asimi yoksa kontrolu
		// yapilabilmesi
		int silinecekIndex = -1;

		// 1. oncelikli kuyrugun tum elemanlar� kontrol ediliyor.
		for (Process p : prosesOncelik1) {

			// Eger proses, daha once calismissa ve prosesin calismasindan itibaren 20
			// saniye gecmisse fakat proses bitmemisse zaman asimina ugratiliyor.
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {

				// Zaman asimi mesaji icin, cikti istenen formata getiriliyor.
				System.out.println(String.format(
						"%s%s.0000 sn proses zaman asimi     id : %s     oncelik : %s    kalan sure : %s%s", p.renkKodu,
						String.valueOf(programSayaci), p.pid, String.valueOf(p.oncelik),
						String.valueOf(p.patlamaZamani), ANSI_RESET));

				// Silinecek index, zaman asimina ugramis prosesin index'ine esitleniyor.
				silinecekIndex = (prosesOncelik1.indexOf(p));
			}
		}

		// Eger silinecek index'e atama yapilmissa, yani -1 degilse proses siliniyor.
		if (silinecekIndex != -1) {

			// Proses, bulundugu listeden cikariliyor.
			prosesOncelik1.remove(silinecekIndex);

			// Silinecek index, kontrol saglamasi icin tekrar -1'e esitleniyor.
			silinecekIndex = -1;
		}

		// 2. oncelikli kuyrugun tum elemanlar� kontrol ediliyor.
		for (Process p : prosesOncelik2) {

			// Eger proses, daha once calismissa ve prosesin calismasindan itibaren 20
			// saniye gecmisse fakat proses bitmemisse zaman asimina ugratiliyor.
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {

				// Zaman asimi mesaji icin, cikti istenen formata getiriliyor.
				System.out.println(String.format(
						"%s%s.0000 sn proses zaman asimi     id : %s     oncelik : %s    kalan sure : %s%s", p.renkKodu,
						String.valueOf(programSayaci), p.pid, String.valueOf(p.oncelik),
						String.valueOf(p.patlamaZamani), ANSI_RESET));

				// Silinecek index, zaman asimina ugramis prosesin index'ine esitleniyor.
				silinecekIndex = (prosesOncelik2.indexOf(p));
			}
		}

		// Eger silinecek index'e atama yapilmissa, yani -1 degilse proses siliniyor.
		if (silinecekIndex != -1) {

			// Proses, bulundugu listeden cikariliyor.
			prosesOncelik2.remove(silinecekIndex);

			// Silinecek index, kontrol saglamasi icin tekrar -1'e esitleniyor.
			silinecekIndex = -1;
		}

		// 3. oncelikli kuyrugun tum elemanlar� kontrol ediliyor.
		for (Process p : prosesOncelik3) {

			// Eger proses, daha once calismissa ve prosesin calismasindan itibaren 20
			// saniye gecmisse fakat proses bitmemisse zaman asimina ugratiliyor.
			if (programSayaci - p.baslamaZamani == 21 && p.dahaOnceCalistiMi) {

				// Zaman asimi mesaji icin, cikti istenen formata getiriliyor.
				System.out.println(String.format(
						"%s%s.0000 sn proses zaman asimi     id : %s     oncelik : %s    kalan sure : %s%s", p.renkKodu,
						String.valueOf(programSayaci), p.pid, String.valueOf(p.oncelik),
						String.valueOf(p.patlamaZamani), ANSI_RESET));

				// Silinecek index, zaman asimina ugramis prosesin index'ine esitleniyor.
				silinecekIndex = (prosesOncelik3.indexOf(p));
			}
		}

		// Eger silinecek index'e atama yapilmissa, yani -1 degilse proses siliniyor.
		if (silinecekIndex != -1) {

			// Proses, bulundugu listeden cikariliyor.
			prosesOncelik3.remove(silinecekIndex);

			// Silinecek index, kontrol saglamasi icin tekrar -1'e esitleniyor.
			silinecekIndex = -1;
		}

	}

	// Gercek zamanli prosesler icin Ilk Gelen Ilk Calisir tipi siralayici
	// fonksiyon
	public void FCFS() {

		// Islem yapilacak proses, okunabilirligi artirmak adina tanimlanip atama
		// yapiliyor.
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

	// Proseslerin calistirilma islemi gercekleniyor.
	public void Calis() throws Exception {

		// Programin kuyruklarda proses kalmayana kadar calismasi icin sonsuz dongu
		// olusturuluyor.
		while (true) {

			// Program sayaci her arttiginda varis suresi dolmus bir proses varsa, gerekli
			// kuyruga eklenmesi icin gerekli fonksiyon cagiriliyor.
			if (!(okunanProsesler.isEmpty())) {

				// Bu fonksiyon cagirilirken, gecici liste icinde veri olup olmadigi kontrolu
				// yapiliyor.
				KuyrugaEkle();

			}
			// Gercek zamanli proses listesi bos degilse FCFS siralayici kullaniliyor.
			if (!(gercekZamanli.isEmpty())) {

				// Prosesin daha once calisma kontrolu yapiliyor.
				if (!gercekZamanli.get(0).dahaOnceCalistiMi) {

					// Eger proses baslamadiysa, baslama zamani program sayacina esitleniyor.
					gercekZamanli.get(0).baslamaZamani = programSayaci;
				}
				FCFS();
			}

			// Gercek zamanli kuyruk bossa 1. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Geri Beslemeli siralayici kullaniliyor.
			else if (!(prosesOncelik1.isEmpty())) {

				// Prosesin daha once calisma kontrolu yapiliyor.
				if (!prosesOncelik1.get(0).dahaOnceCalistiMi) {

					// Eger proses baslamadiysa, baslama zamani program sayacina esitleniyor.
					prosesOncelik1.get(0).baslamaZamani = programSayaci;
				}

				GeriBesleme();
			}

			// 1. oncelik kuyrugu bossa 1. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Geri Beslemeli siralayici kullaniliyor.
			else if (!(prosesOncelik2.isEmpty())) {

				// Prosesin daha once calisma kontrolu yapiliyor.
				if (!prosesOncelik2.get(0).dahaOnceCalistiMi) {

					// Eger proses baslamadiysa, baslama zamani program sayacina esitleniyor.
					prosesOncelik2.get(0).baslamaZamani = programSayaci;
				}
				GeriBesleme();
			}

			// 2. oncelik kuyrugu bossa 3. oncelikli prosesler kontrol ediliyor.
			// Kuyruk bos degilse Round Robin siralayici kullaniliyor.
			else if (!(prosesOncelik3.isEmpty())) {

				// Prosesin daha once calisma kontrolu yapiliyor.
				if (!prosesOncelik3.get(0).dahaOnceCalistiMi) {

					// Eger proses baslamadiysa, baslama zamani program sayacina esitleniyor.
					prosesOncelik3.get(0).baslamaZamani = programSayaci;
				}

				RoundRobin();
			}

			// 20 saniye kontrolu ile prosesin sonlanip sonlanmayacagi durumu saglaniliyor.
			ZamanAsimiKontrol();

			// Prosesin anlik durumu yazdiriliyor.
			Yazdir();

			// Eger 3. oncelik kuyrugu da bossa, tum kuyruklar kontrol edilmis oldugu icin
			// kuyrukta proses kalmamis oluyor. Bu sartlarda da program sonlaniyor ve sonsuz
			// dongu bitiriliyor.
			if (okunanProsesler.isEmpty() && gercekZamanli.isEmpty() && prosesOncelik1.isEmpty()
					&& prosesOncelik2.isEmpty() && prosesOncelik3.isEmpty()) {
				// programSayaci
				System.out.println("Program sonlandi.");
				break;
			}
			// Islem yapilmis prosesimiz var ise onceki processe atama saglanarak son islem
			// yapilan process, elde tutuluypr.
			if (islemSonrasiProses != null) {
				oncekiProses = new Process(islemSonrasiProses);
			}
			// Saniye(program sayaci) arttiriliyor.
			programSayaci++;

		}
	}

	// Istenilen bicimde output icin yazdirma islemi yapiliyor.
	public void Yazdir() throws Exception {

		// Onceki islem yapilan proses bossa ilk proses budur.
		if (oncekiProses == null) {

			// Islem yapilacak proses bos degilse ilk proses baslangic mesaji yazdirilir.
			if (islemOncesiProses != null) {

				// Ilk prosesin yazdirilmasi
				System.out.println(
						String.format("%s%s.0000 sn proses basladi     id : %s     oncelik : %s    kalan sure : %s%s",
								islemOncesiProses.renkKodu, String.valueOf(programSayaci), islemOncesiProses.pid,
								String.valueOf(islemOncesiProses.oncelik),
								String.valueOf(islemOncesiProses.patlamaZamani), ANSI_RESET));
			}
		}

		// Program cycle'inda eger onceki islem gormus proses ve islem gorecek olan
		// proses ayniysa, ayni proses yurutulmeye devam ediyor demektir.
		else if (oncekiProses.pid == islemOncesiProses.pid
				&& oncekiProses.patlamaZamani == islemOncesiProses.patlamaZamani) {

			// Ayni prosesin yurutulmesi ve mesaj yazdirilmasi
			System.out.println(
					String.format("%s%s.0000 sn proses yurutuluyor     id : %s     oncelik : %s    kalan sure : %s%s",
							islemOncesiProses.renkKodu, String.valueOf(programSayaci), islemOncesiProses.pid,
							String.valueOf(islemOncesiProses.oncelik), String.valueOf(islemOncesiProses.patlamaZamani),
							ANSI_RESET));

		}

		// Eger onceki islem gormus proses ve islem gorecek proses farkliysa kontrol
		// edilmesi gerek.
		else if (oncekiProses.pid != islemOncesiProses.pid) {

			// Onceki islem gormus prosesin patlama zamani 0'sa proses bitmis demektir.
			// Bir adet sonlanma mesaji, bir adet de baslama mesaji yazdiriliyor.
			if (oncekiProses.patlamaZamani == 0) {

				// Onceki islem gormus prosesin bitme mesaji
				System.out.println(String.format(
						"%s%s.0000 sn proses sonlandi     id : %s     oncelik : %s    kalan sure : %s%s",
						oncekiProses.renkKodu, String.valueOf(programSayaci), oncekiProses.pid,
						String.valueOf(oncekiProses.oncelik), String.valueOf(oncekiProses.patlamaZamani), ANSI_RESET));

				// Islem gorecek olan prosesin baslama mesaji
				System.out.println(
						String.format("%s%s.0000 sn proses basladi     id : %s     oncelik : %s    kalan sure : %s%s",
								islemOncesiProses.renkKodu, String.valueOf(programSayaci), islemOncesiProses.pid,
								String.valueOf(islemOncesiProses.oncelik),
								String.valueOf(islemOncesiProses.patlamaZamani), ANSI_RESET));

			}

			// Eger onceki islem gormus prosesin patlama zamani 0 degilse, proses daha
			// bitmemistir.
			// Fakat onceki kosuluna gore su anda islem yapilacak olan proses bu degildir.
			// Yani onceki proses kesintiye alinmis, baska proses baslamistir.
			// Bir adet askiya alinma mesaji, bir adet baslama mesaji yazdiriliyor.
			else if (oncekiProses.patlamaZamani != 0) {

				// Onceki islem gormus prosesin askiya alinma mesaji
				System.out.println(String.format(
						"%s%s.0000 sn proses askida     id : %s     oncelik : %s    kalan sure : %s%s",
						oncekiProses.renkKodu, String.valueOf(programSayaci), oncekiProses.pid,
						String.valueOf(oncekiProses.oncelik), String.valueOf(oncekiProses.patlamaZamani), ANSI_RESET));

				// Islem gorecek olan prosesin baslama mesaji
				System.out.println(
						String.format("%s%s.0000 sn proses basladi     id : %s     oncelik : %s    kalan sure : %s%s",
								islemOncesiProses.renkKodu, String.valueOf(programSayaci), islemOncesiProses.pid,
								String.valueOf(islemOncesiProses.oncelik),
								String.valueOf(islemOncesiProses.patlamaZamani), ANSI_RESET));

			}
		}

		// Eger butun prosesler sonlanmissa ve varis zamani bekleyen baska proses yoksa
		// son prosesin sonlanma mesaji yazdiriliyor.
		if (okunanProsesler.isEmpty() && gercekZamanli.isEmpty() && prosesOncelik1.isEmpty() && prosesOncelik2.isEmpty()
				&& prosesOncelik3.isEmpty()) {

			// Son islem yapilan prosesin sonlandirilma mesaji
			System.out.println(
					String.format("%s%s.0000 sn proses sonlandi     id : %s     oncelik : %s    kalan sure : %s%s",
							islemSonrasiProses.renkKodu, String.valueOf(programSayaci + 1), islemSonrasiProses.pid,
							String.valueOf(islemSonrasiProses.oncelik),
							String.valueOf(islemSonrasiProses.patlamaZamani), ANSI_RESET));

		}

	}
}