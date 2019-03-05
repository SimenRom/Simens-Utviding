package me.SimenRom;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class KontoListe {
	private List<Konto> liste;
	
	public KontoListe() {
		liste = new ArrayList<Konto>();
	}
	
	public Konto hentKonto(String brukarnavn) {
		for(Konto k : liste) {
			if(k.getEigar().equalsIgnoreCase(brukarnavn)) { //her har eg endra frå equals til ignoreCase!!
				return k;
			}
		}
		nyKonto(new Konto(brukarnavn));
		return null;
	}
	
	public void nyKonto(Konto konto) {
		liste.add(konto);
	}
	
	public void fjernKonto(Konto konto) {
		liste.remove(konto);
	}
	
	public void fjernKonto(String brukarnavn) {
		for(Konto k : liste) {
			if(k.getEigar().equals(brukarnavn)) {
				liste.remove(k);
			}
		}
	}
	public List<Konto> getListe(){
		return liste;
	}
	public int lengde() {
		return liste.size();
	}
	
}
