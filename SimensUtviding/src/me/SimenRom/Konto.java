package me.SimenRom;

public class Konto {
	private String eigar;
	private int balanse;
	
	public Konto(String eigar) {
		this.eigar = eigar;
		balanse = 1000;
	}
	
	public Konto(String eigar, int balanse) {
		this.eigar = eigar;
		this.balanse = balanse;
	}
	
	public void trekk(int trekk) {
		balanse -= trekk;
	}
	
	public void leggTil(int LeggTil) {
		this.balanse += LeggTil;
	}

	public String getEigar() {
		return eigar;
	}

	public void setEigar(String eigar) {
		this.eigar = eigar;
	}

	public int getBalanse() {
		return balanse;
	}

	public void setBalanse(int balanse) {
		this.balanse = balanse;
	}
	
	
}
