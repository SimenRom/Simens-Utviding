package me.SimenRom;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MineEvents implements Listener {
	private HashMap<String, Integer> aktivListe = SimensUtviding.getAktivListe();
	private HashMap<String, Long> sistPaalogga = SimensUtviding.getSistPaalogga();
	private ChatColor FARGE1 = SimensUtviding.getFARGE1();
	private ChatColor FARGE2 = SimensUtviding.getFARGE2();
	// private HashMap<String, Boolean> sover = new HashMap<String, Boolean>();
	private int antallSover = 0;
	final private int BELONNING = 20;
	@EventHandler
	public void PlayerIsSleeping(PlayerBedEnterEvent event) {
		if (event.getPlayer().getWorld().getTime() > 12540 && Bukkit.getOnlinePlayers().size() > 1) {
			Bukkit.broadcastMessage(
					FARGE2 + event.getPlayer().getDisplayName() + FARGE1 + " sover! Kanskje du og skal legge deg?");
		}
	}
	// // if(!event.getBed().isEmpty()) {
	// // Bukkit.broadcastMessage("senga er jo ikkje tom");
	// // }
	// oppdaterAntallSover();
	//
	// if (event.getPlayer().getWorld().getTime() > 12540) {
	// if ((double) Bukkit.getOnlinePlayers().size() / 2.0 <= (double) antallSover)
	// {
	// Bukkit.broadcastMessage(FARGE1 + Diktsamling.hentTilfeldigDikt());
	//// Bukkit.broadcastMessage("Weatherduration: " +
	// event.getPlayer().getWorld().getWeatherDuration());
	// event.getPlayer().getWorld().setWeatherDuration(170000);
	// event.getPlayer().getWorld().setFullTime(0);
	// antallSover = 0;
	// } else {
	// Bukkit.broadcastMessage(FARGE1 + event.getPlayer().getDisplayName() + "
	// sover. No sover "
	// + ((double) antallSover / (double) Bukkit.getOnlinePlayers().size()) * 100.0
	// + "% av alle. Minst 50% må sove for at det skal bli dag.");
	//
	// }
	// }
	// // Bukkit.broadcastMessage(antallSover + " " +
	// // Bukkit.getOnlinePlayers().size());
	// }
	// public void oppdaterAntallSover() {
	// int tal = 0;
	// for (Player p : Bukkit.getOnlinePlayers()) {
	// if (p.getSleepTicks() > 0) {
	// tal++;
	// }
	// }
	// antallSover = tal;
	// }
	// @EventHandler
	// public void PlayerBedLeaveEvent(PlayerBedEnterEvent event) {
	// try {
	// Thread.sleep(100);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (event.getPlayer().getWorld().getTime() >= 13000) {
	// oppdaterAntallSover();
	//
	// Bukkit.broadcastMessage(
	// FARGE2 + event.getPlayer().getDisplayName() + " har gått i frå senga. Antall
	// sover:" + antallSover);
	// }
	// }

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		String spelarNamn = e.getPlayer().getDisplayName();
		if (!aktivListe.containsKey(spelarNamn)) {
			aktivListe.put(spelarNamn, 0);
		}
		if (aktivListe.get(spelarNamn) == 100000) {
			SimensUtviding.hentListe().hentKonto(spelarNamn).leggTil(BELONNING);
			e.getPlayer().sendMessage(
					FARGE1 + "Du fekk " + FARGE2 + BELONNING + FARGE1 + " simelionar for å vere aktiv! :D ");
			SimensUtviding.loggAktiv(e.getPlayer().getDisplayName(), BELONNING);
			aktivListe.replace(spelarNamn, 0);
		}
		aktivListe.replace(spelarNamn, (aktivListe.get(spelarNamn) + 10));
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (sistPaalogga.get(p.getDisplayName()) == null) { // setter tida til 0, så du uansett får velkommen-meldingen.
															// Unngår nullpointerexception.
			sistPaalogga.put(event.getPlayer().getDisplayName(), 0L);
		}
		if (sistPaalogga.get(p.getDisplayName()) + 3600000 < System.currentTimeMillis()) { // 100000 = 100 sekund, 1 time!
			// p.sendMessage(FARGE1 + "Velkommen " + p.getDisplayName() + "!");
//			p.sendMessage(FARGE1 + "Dine butikkar har solgt varer verdi " + FARGE2 + 0 + FARGE1 + "$im sidan sist.");
			p.sendMessage(FARGE1 + "Velkommen tilbake "+p.getDisplayName()+"! Her har du "+ FARGE2 + 25 + FARGE1 + "Simelionar! :)");
			SimensUtviding.loggTilbake(p.getDisplayName(), 25);
			SimensUtviding.hentListe().hentKonto(p.getDisplayName()).leggTil(25);
		}
		// p.sendMessage("sist:" +sistPaalogga.get(p.getDisplayName()));
		// p.sendMessage("no :" +System.currentTimeMillis());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		sistPaalogga.put(event.getPlayer().getDisplayName(), System.currentTimeMillis());
	}

}
