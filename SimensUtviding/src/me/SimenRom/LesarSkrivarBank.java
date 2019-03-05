package me.SimenRom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class LesarSkrivarBank {
	private final static String FILNAMN = "E:\\Nedlastinger\\BukkitServer13.1.2\\server\\plugins\\bankfil.txt";
	public static void skrivkontoListeFil(KontoListe liste) {
		
		
		try {
			PrintWriter skrivar = new PrintWriter(FILNAMN);
			skrivar.println("Oversikt over alle bankkontoar når server er av:");
			for(Konto k : liste.getListe()) {
				skrivar.println(k.getEigar() + ":" + k.getBalanse());
			}
			skrivar.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static KontoListe leskontoListeFil() {
		KontoListe liste = new KontoListe();
		Scanner sc;
		try {
			File fil = new File(FILNAMN);
			sc = new Scanner(fil);
			String linje;
			sc.nextLine();
			while (sc.hasNextLine()) {
				linje = sc.nextLine();
				if(linje.length() < 3) {
					break;
				}
				String[] info = linje.split(":");
				liste.nyKonto(new Konto(info[0], Integer.parseInt(info[1])));
			}
			sc.close();
			return liste;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
