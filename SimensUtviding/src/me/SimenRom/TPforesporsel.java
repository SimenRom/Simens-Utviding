package me.SimenRom;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TPforesporsel { //implements Runnable
	private Player fra;
	private Player til;
//	private boolean tilGodta;
//	private long nedTelling;
	private long tidNo;
	private long tidUgyldig;
	private boolean gjennomfor;
	private ChatColor FARGE1 = SimensUtviding.getFARGE1();
	private ChatColor FARGE2 = SimensUtviding.getFARGE2();
	public TPforesporsel(Player fra, Player til, int gyldigTidSek) {
		this.fra = fra;
		this.til = til;
		tidNo = System.currentTimeMillis();
		tidUgyldig = tidNo + (gyldigTidSek * 1000);
		gjennomfor = false;
	}
	
	public void gjennomforTP() {
		if(fra.isOnline() && til.isOnline())
		fra.teleport(til);
		fra.playSound(fra.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
		til.playSound(fra.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
		fra.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 10));
//		fra.spawnParticle(arg0, arg1, arg2);
		fra.sendMessage(FARGE1 +"Du har blitt teleportert til " + til.getDisplayName() + ".");
		til.sendMessage(FARGE1 + fra.getDisplayName() + " har blitt teleportert til deg.");
	}
	
//	private void sjekkGyldig() {
//		while (tidNo < tidUgyldig) { // && !gjennomfort
//			if (gjennomfor) {
//				fra.teleport(til);
//				fra.sendMessage("Du har blitt teleportert til " + til.getDisplayName() + ".");
//				til.sendMessage(fra.getDisplayName() + " har blitt teleportert til deg.");
//				return;
//			}
//			tidNo = System.currentTimeMillis();
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

//		}
//		fra.sendMessage("Forespørselen om teleport til " + til.getDisplayName() + " har utløpt. Send ny.");
//		til.sendMessage(fra.getDisplayName() + " sin forespørsel om teleport er utløpt.");
//
//	}

//	@Override
//	public void run() {
//		sjekkGyldig();
//	}

	public void godtaForesporsel() {
		gjennomfor = true;
	}

	public Player getFra() {
		return fra;
	}

	public Player getTil() {
		return til;
	}

	public long getTidNo() {
		return tidNo;
	}

	public long getTidUgyldig() {
		return tidUgyldig;
	}

}
