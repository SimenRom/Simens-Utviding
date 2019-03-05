package me.SimenRom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class SimensUtviding extends JavaPlugin {
	private static final int TELEPORTPRIS = 150;
	private static final int TELEPORTTID = 20; // Oppgi i sekund
	private List<TPforesporsel> TPlist = new ArrayList<TPforesporsel>();
	private static final ChatColor FARGE1 = ChatColor.GREEN;
	private static final ChatColor FARGE2 = ChatColor.YELLOW;
	private static final int STARTPAKKEPRIS = 200;

	static KontoListe liste;
	private final char cl = (char) (181 + '0');
	private final char cS = (char) (149 + '0');
	private final String aaTegn = Character.toString(cl);
	private final String AATegn = Character.toString(cS);

	private static HashMap<String, Integer> aktivListe;
	private static HashMap<String, Long> sistPaalogga;

	@Override
	public void onEnable() {
		getLogger().info("Leser kontoliste frå fil.");
		liste = LesarSkrivarBank.leskontoListeFil();
		getLogger().info("Lasta inn " + liste.lengde() + " kontoar fra" + aaTegn + " fil.");
		aktivListe = new HashMap<String, Integer>();
		sistPaalogga = new HashMap<String, Long>();
		getServer().getPluginManager().registerEvents(new MineEvents(), this);
		Diktsamling.lesDiktlisteFil();
	}

	@Override
	public void onDisable() {
		LesarSkrivarBank.skrivkontoListeFil(liste);
		getLogger().info("Skriver kontoliste til fil. Antall kontoar: " + liste.lengde());
	}

	public int antallLedigeInv(Player player) {
		int tellar = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) {

			} else {
				tellar++;
			}
		}
		return tellar;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("warp") && sender instanceof Player) {
			warpMetode(sender, cmd, label, args);
			return true;
		} else if (cmd.getName().equals("sjekkSov") && sender instanceof Player) {
			Player pl = (Player) sender;
			pl.sendMessage("Sleep ticks: " + pl.getSleepTicks());
		} else if (cmd.getName().equals("dag") && sender instanceof Player) {
			Player pl = (Player) sender;
			if (args.length >= 1 && args[0].equals("bekreft")) {
				if (liste.hentKonto(pl.getDisplayName()).getBalanse() >= 50 && pl.getWorld().getFullTime() > 1300) {
					liste.hentKonto(pl.getDisplayName()).trekk(50);
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(FARGE1 + " " + pl.getDisplayName() + " betalte 50Sims for dag.");
					}
					pl.getWorld().setFullTime(0);
				} else {
					pl.sendMessage(FARGE2 + "Det er allerede dag, eller du har ikkje råd.");
				}
			} else {
				pl.sendMessage(FARGE1 + "Om 50% av alle sover blir det dag. Alternativ kan du betale " + FARGE2
						+ "50Simelionar" + FARGE1 + " for dag no med /dag bekreft");
			}

		} else if (cmd.getName().equals("resettKontoar bekreft") && sender instanceof Player) {
			Player pl = (Player) sender;
			if (pl.isOp()) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (liste.hentKonto(p.getDisplayName()) != null) {
						liste.hentKonto(p.getDisplayName()).setBalanse(1000);
					} else {
						liste.nyKonto(new Konto(pl.getDisplayName()));
					}
				}
				for (Konto k : liste.getListe()) {
					k.setBalanse(1000);
				}
			}
		} else if (cmd.getName().equals("startpakke") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 0 && args[0].equalsIgnoreCase("bekreft")) {
				Konto kontoen = liste.hentKonto(player.getDisplayName());
				if (kontoen.getBalanse() >= STARTPAKKEPRIS && antallLedigeInv(player) >= 21) {
					player.sendMessage(FARGE1 + "Du har kjøpt startpakken");
					kontoen.trekk(STARTPAKKEPRIS);
					PlayerInventory inventory = player.getInventory();
					for (int i = 0; i < 8; i++) {
						inventory.addItem(new ItemStack(Material.SPRUCE_LOG));
						inventory.addItem(new ItemStack(Material.TORCH));
						// inventory.addItem(new ItemStack(Material.STONE_BRICKS));
						// inventory.addItem(new ItemStack(Material.STONE_BRICKS));
						// inventory.addItem(new ItemStack(Material.DARK_OAK_STAIRS));
						// inventory.addItem(new ItemStack(Material.QUARTZ_BLOCK));
						// inventory.addItem(new ItemStack(Material.QUARTZ_BLOCK));
						// inventory.addItem(new ItemStack(Material.QUARTZ_STAIRS));
						// inventory.addItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
						// inventory.addItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
						// inventory.addItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
						// inventory.addItem(new ItemStack(Material.DARK_OAK_FENCE));
						// inventory.addItem(new ItemStack(Material.DARK_OAK_PLANKS));
						// inventory.addItem(new ItemStack(Material.DARK_OAK_PLANKS));
						// inventory.addItem(new ItemStack(Material.DARK_OAK_PLANKS));
						// inventory.addItem(new ItemStack(Material.DARK_OAK_PLANKS));
						// inventory.addItem(new ItemStack(Material.SNOW_BLOCK));

					}
					inventory.addItem(new ItemStack(Material.COMPASS));
					inventory.addItem(new ItemStack(Material.GRAY_BED));
				} else {
					player.sendMessage(FARGE2
							+ "Du har ikkje råd til startpakken, eller ikkje nok ledig plass i inventory. Startpakken krever 21 ledige felt i din inventory.");
				}
			} else {
				player.sendMessage(FARGE1 + "Startpakken kostar " + STARTPAKKEPRIS + " Simelionar. Skriv \"" + FARGE2
						+ "/startpakke bekreft" + FARGE1 + "\" for å kjøpe!");
			}
			return true;
		} else if (cmd.getName().equals("hello") && sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(ChatColor.AQUA + "Her er tekst.");
			player.sendMessage(ChatColor.BLUE + "Her er tekst.");
			player.sendMessage(ChatColor.DARK_AQUA + "Her er tekst.");
			player.sendMessage(ChatColor.DARK_PURPLE + "Her er tekst.");
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Her er tekst.");
			player.sendMessage(ChatColor.GREEN + "Her er tekst.");
			player.sendMessage(ChatColor.RED + "Her er tekst.");
			player.sendMessage(FARGE1 + "Hei " + player.getDisplayName() + "! Pålogga spelarar:");
			List<Player> onlineFolk = (List<Player>) Bukkit.getServer().getOnlinePlayers();
			for (Player p : onlineFolk) {
				player.sendMessage(ChatColor.DARK_GREEN + p.getDisplayName());
			}
		} else if (cmd.getName().equalsIgnoreCase("godta") && sender instanceof Player) { // && args.length > 0
			Player player = (Player) sender;
			if (args.length > 0) {
				for (TPforesporsel tp : TPlist) {
					List<Player> alle = (List<Player>) Bukkit.getServer().getOnlinePlayers();
					Player goal = null;
					;
					for (Player p : alle) {
						if (p.getDisplayName().equalsIgnoreCase(args[0])) {
							goal = p;
							break;
						}
					}
					if (goal != null && tp.getTil().getDisplayName().equals(player.getDisplayName())
							&& tp.getTil().getDisplayName().equalsIgnoreCase(goal.getDisplayName())) {
						if (System.currentTimeMillis() > tp.getTidUgyldig()) {
							tp.getFra().sendMessage(FARGE1 + "Tps-forespørselen din til " + tp.getTil()
									+ " er utgått på tid. Send ny!");
							tp.getTil().sendMessage(
									FARGE1 + "Tps-forespørselen frå " + tp.getFra() + "er utgått på tid. Send ny!");
						} else {
							tp.gjennomforTP();
							liste.hentKonto(tp.getFra().getDisplayName()).trekk(TELEPORTPRIS);
							tp.getFra().sendMessage(FARGE1 + "Du har blitt trekt " + FARGE2 + TELEPORTPRIS + FARGE1
									+ "Simelionar for teleport");
						}
						TPlist.remove(tp);
						return true;
					} else {
						player.sendMessage(FARGE1 + "Fant inge online spelar med navn " + args[0]);
					}
				}
			} else {
				for (TPforesporsel tp : TPlist) {
					if (tp.getTil().getDisplayName().equals(player.getDisplayName())) {
						if (System.currentTimeMillis() > tp.getTidUgyldig()) {
							tp.getFra().sendMessage(FARGE1 + "Tps-forespørselen din til " + tp.getTil().getDisplayName()
									+ " er utgått på tid. Send ny!");
							tp.getTil().sendMessage(FARGE1 + "Tps-forespørselen frå " + tp.getFra().getDisplayName()
									+ "er utgått på tid. Send ny!");
						} else {
							tp.gjennomforTP();
							liste.hentKonto(tp.getFra().getDisplayName()).trekk(TELEPORTPRIS);
							tp.getFra().sendMessage(FARGE1 + "Du har blitt trekt " + FARGE2 + TELEPORTPRIS + FARGE1
									+ " Simelionar for teleport");
						}
						TPlist.remove(tp);
						return true;
					}
				}
			}
			player.sendMessage(FARGE1 + "Du har ingen innkommande teleport-forespørslar! Send ein med /tps");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tps") && sender instanceof Player) {
			Player player = (Player) sender;
			List<Player> onlineFolk = (List<Player>) Bukkit.getServer().getOnlinePlayers();
			if (args.length == 1) { // && onlineFolk.size() > 1
				for (Player p : onlineFolk) {
					if (p.getDisplayName().equalsIgnoreCase(args[0])) {
						teleporter(player, p);
						return true;
					}
				}
			}
			player.sendMessage(
					FARGE1 + "Du på oppgi ein pålogga spelar eller har ikkje nok peng! tps kostar " + TELEPORTPRIS);
			return true;
			// if (erArgumentSpelar(args[0])) {
			//
			// } else if (erGydligKordinatar(args)) {
			// player.sendMessage("Lager destinasjon");
			// Location dest = new Location(player.getWorld(), Integer.parseInt(args[0]), 1,
			// Integer.parseInt(args[1]));
			// player.sendMessage("Sjekker om destinasjon er gyldig");
			// for(int i = 1; i < 100; i++) {
			// if(dest.getBlock().getType().isBlock()) {
			//
			// player.teleport(dest);
			// return true;
			// }
			// player.sendMessage(i + ": " + dest.getBlock().getType());
			// dest.setX(i);
			// }
			// player.sendMessage("Fant ikkje gyldig possisjon");
			// }

		} else if (cmd.getName().equalsIgnoreCase("peng") && sender instanceof Player) {
			Player player = (Player) sender;
//			getLogger().info("Antall argument: " + args.length);
			if (args.length == 0) {
				if (liste.hentKonto(player.getDisplayName()) == null) {
					liste.nyKonto(new Konto(player.getDisplayName()));
				}
				player.sendMessage(
						FARGE1 + "Du har " + liste.hentKonto(player.getDisplayName()).getBalanse() + " Simelionar.");
				return true;

				// ~~~ LISTE ~~~ LISTE ~~~ LISTE ~~~ LISTE ~~~ LISTE ~~~ LISTE ~~~ LISTE ~~~
			} else if (args.length == 1 && (args[0].equalsIgnoreCase("liste"))
					|| args[0].equalsIgnoreCase("oversikt")) {
				for (Konto k : liste.getListe()) {
					int navnLengde = k.getEigar().length();
					int antMellomrom = 20 - navnLengde;
					String mellomrom = "";
					for (int i = 0; i < antMellomrom; i++) {
						mellomrom += " ";
					}
					player.sendMessage(FARGE1 + " ~ " + k.getEigar() + mellomrom + k.getBalanse() + "Sim");
				}
			} else if (player.isOp() && args.length == 2 && args[0].equalsIgnoreCase("generer")
					&& args[1].matches("[0-9]{0,5}")) {
				liste.hentKonto(player.getDisplayName()).leggTil(Integer.parseInt(args[1]));
				player.sendMessage(
						FARGE1 + "Du har no " + liste.hentKonto(player.getDisplayName()).getBalanse() + " Simelionar.");
				// LesarSkrivarBank.skrivkontoListeFil(liste);

				// ~~~ SEND ~~~ SEND ~~~ SEND ~~~ SEND ~~~ SEND ~~~ SEND ~~~ SEND ~~~ SEND ~~~
			} else if (args.length == 3 && args[0].equalsIgnoreCase("send") && liste.hentKonto(args[1]) != null
					&& args[2].matches("[0-9]{0,5}")) {
				int pengar = Integer.parseInt(args[2]);
				Konto sendar = liste.hentKonto(player.getDisplayName());
				Konto mottakar = liste.hentKonto(args[1]);
				int forskjell = sendar.getBalanse() - pengar;
				if (forskjell >= 0 && mottakar != null) {
					sendar.trekk(pengar);
					mottakar.leggTil(pengar);
					player.sendMessage(FARGE1 + "Du sendte " + pengar + " Simelionar til " + mottakar.getEigar());
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.getDisplayName().equals(mottakar.getEigar())) {
							p.sendMessage(FARGE1 + "Du mottok " + pengar + " Simelionar frå " + sendar.getEigar());
							// LesarSkrivarBank.skrivkontoListeFil(liste);
						}
					}
				} else {
					player.sendMessage(FARGE2 + "Du mangler " + Math.abs(forskjell) + " for " + aaTegn + " sende "
							+ pengar + " Simelionar.");
				}
			} else if (args.length == 1 && ((args[0].equalsIgnoreCase("hjelp") || args[0].equalsIgnoreCase("help")))) {
				player.sendMessage(FARGE1
						+ "/peng er eit system for å ha økonomi på serveren. Du kan bruke peng til å betale eller selge varer/tjenester.");
				player.sendMessage(ChatColor.GREEN + "</peng>" + FARGE1 + " for å sjekke din eigen balanse.");
				player.sendMessage(FARGE2 + "</peng send> \"" + FARGE1 + " for å sende peng til ein anna spelar. ");
				player.sendMessage(FARGE2 + "</peng oversikt> \"" + FARGE1 + " for å sjå balansen til andre spelarar.");
				player.sendMessage(FARGE2 + "Kontakt SimenRom om noko er feil eller om du har idear.");
				// ~~~ FEIL ~~~ FEIL ~~~ FEIL ~~~ FEIL ~~~ FEIL ~~~ FEIL ~~~ FEIL ~~~ FEIL ~~~
			} else {
				player.sendMessage(FARGE2
						+ "Feil kommando. Bruk \"/peng send <SpelarNamn>\" eller \"/peng generer <Sum>\" Eksempel: /peng send SpelarNamn 1337");
			}

		}
		return true;
	}

	private void warpMetode(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length > 0 && args[0].equals("bekreft")) {
			if (liste.hentKonto(player.getDisplayName()).getBalanse() > TELEPORTPRIS) {
				if (player.getBedSpawnLocation() != null) {
					player.teleport(player.getBedSpawnLocation());
					liste.hentKonto(player.getDisplayName()).trekk(TELEPORTPRIS);
					player.sendMessage(FARGE1 + "Du har blitt trekt " + TELEPORTPRIS + " for warpen.");
				} else {
					player.sendMessage(FARGE2 + "Du har ingen seng! Har du hugsa å sove i den fyrst?");
				}
			} else {
				player.sendMessage(FARGE2 + "Du har ikkje nok Simelionar for warp!");
			}
		} else {
			player.sendMessage(FARGE1 + "Warp teleporterer deg til senga di! Dette kostar "+FARGE2+" " + TELEPORTPRIS + " " + FARGE1 + "Simelionar. Skriv /warp bekreft for å warpe.");
		}
		// if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
		// player.sendMessage(FARGE1 + "Sett opp eit skilt og skriv WARP SpelarNamn på
		// det. Eksempel: WARP SimenRom");
		// // Gi beskjed om at brukar skal trykke på block(skilt)
		// } else if (args.length == 1 && ((args[0].equalsIgnoreCase("hjelp") ||
		// args[0].equalsIgnoreCase("help")))) {
		// // Forklare spelar korleis bruke warps
		//
		// } else if (args.length == 1) {
		// // sjekke om varpen er ein som finst og at den høyrer til vedkommande
		// } else {
		// // Liste opp spelaren sine tilgjengelege warps?
		// }
		// player.sendMessage(FARGE1 + "Warp-kommando er ikkje laga ferdig enda.");
	}

	private boolean teleporter(Player fra, Player til) {
		if (liste.hentKonto(fra.getDisplayName()).getBalanse() >= TELEPORTPRIS) {
			fra.sendMessage(FARGE1 + "Venter på at " + til.getName() + " skal godta. Vedkommande har " + TELEPORTTID
					+ " sekund på å godta.");
			til.sendMessage(FARGE1 + fra.getName() + " vil teleportere til deg! " + FARGE2 + "/godta" + FARGE1
					+ " for å godta. Du har " + TELEPORTTID + " sekund på å godta. Ignorer for å ikkje akseptere.");
			TPforesporsel tpf = new TPforesporsel(fra, til, TELEPORTTID);
			TPlist.add(tpf);

		} else {
			fra.sendMessage(FARGE1 + "Du har ikkje nok Simelionar. Teleport kostar " + TELEPORTPRIS + ".");
		}

		return true;
	}

	private boolean erGydligKordinatar(String[] kord) {
		// for(int i = 0; i < 2; i++) {
		//
		// }
		return true;
	}

	public boolean erArgumentSpelar(String arg) {
		return false;
	}

	public static ChatColor getFARGE1() {
		return FARGE1;
	}

	public static ChatColor getFARGE2() {
		return FARGE2;
	}

	public static HashMap<String, Integer> getAktivListe() {
		return aktivListe;
	}

	public static HashMap<String, Long> getSistPaalogga() {
		return sistPaalogga;
	}

	public void setAktivListe(HashMap<String, Integer> aktivListe) {
		this.aktivListe = aktivListe;
	}

	public static KontoListe hentListe() {
		// TODO Auto-generated method stub
		return liste;
	}

	public static void loggAktiv(String navn, int bln) {
		Bukkit.getLogger().info(navn + " fekk " + bln + " for å vera aktiv.");
	}
	public static void loggTilbake(String navn, int bln) {
		Bukkit.getLogger().info(navn + " fekk " + bln + " for å logge på.");
	}
}
