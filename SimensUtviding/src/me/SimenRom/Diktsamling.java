package me.SimenRom;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Bukkit;

public class Diktsamling {
	private final static String FILNAMN = "E:\\Nedlastinger\\BukkitServer13.1.2\\server\\plugins\\MorgonOrd.txt";
	private static List<String> diktsamling = new ArrayList<String>();
	private static Random rand = new Random();
	
	public static String hentTilfeldigDikt() {
		if(diktsamling.size() == 0) {
			return "God morgon!";
		}
		String dikt = diktsamling.get(rand.nextInt(diktsamling.size()));
		return dikt;
	}
	
	public static void lesDiktlisteFil() {
		List<String> liste = new ArrayList<String>();
		Scanner sc;
		try {
			File fil = new File(FILNAMN);
			sc = new Scanner(fil);
			String linje;
//			sc.nextLine();
			while (sc.hasNextLine()) {
				linje = sc.nextLine();
				if(linje.length() < 3) {
					break;
				}
				liste.add(linje);
			}
			sc.close();
			diktsamling = liste;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Bukkit.getLogger().info("Fant " + liste.size() + "dikt i fil.");
	}
}
