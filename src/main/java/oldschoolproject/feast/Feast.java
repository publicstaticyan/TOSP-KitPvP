package oldschoolproject.feast;

import java.util.Arrays;
import java.util.Random;

import oldschoolproject.Main;
import oldschoolproject.holograms.Hologram;
import oldschoolproject.utils.builders.ItemBuilder;
import oldschoolproject.utils.formatters.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class Feast {

	private static final int SECONDS_TO_SPAWN = 5; // 300
	private static final int SECONDS_TO_DESPAWN = 5; // 10
	private static final int SECONDS_TO_COOLDOWN = 5; // 600

	private static ItemStack[] defaultLoot = new ItemStack[] {
			new ItemBuilder(Material.STONE_SWORD).setName("§eEspada de Pedra").toItemStack(),
			new ItemBuilder(Material.FISHING_ROD).setName("§eVara de Pesca").toItemStack(),
			new ItemBuilder(Material.FIRE_CHARGE).setName("§eBola de Fogo").toItemStack(),
			new ItemBuilder(Material.IRON_SWORD).setName("§eEspada de Ferro").toItemStack(),
			new ItemBuilder(Material.ENDER_PEARL).setName("§ePérola do Fim").toItemStack(),
			new ItemBuilder(Material.LEATHER_HELMET).setName("§eCapacete de Couro").toItemStack(),
			new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§ePeitoral de Couro").toItemStack(),
			new ItemBuilder(Material.LEATHER_LEGGINGS).setName("§eCalça de Couro").toItemStack(),
			new ItemBuilder(Material.LEATHER_BOOTS).setName("§eBotas de Couro").toItemStack(),
			new ItemBuilder(Material.CHAINMAIL_HELMET).setName("§eCapacete de Malha").toItemStack(),
			new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setName("§ePeitoral de Malha").toItemStack(),
			new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setName("§eCalça de Malha").toItemStack(),
			new ItemBuilder(Material.CHAINMAIL_BOOTS).setName("§eBotas de Malha").toItemStack(),
			new ItemBuilder(Material.IRON_CHESTPLATE).setName("§ePeitoral de Ferro").toItemStack(),
			new ItemBuilder(Material.TURTLE_HELMET).setName("§eCasco de Tartaruga").toItemStack(),
			new ItemBuilder(Material.TOTEM_OF_UNDYING).setName("§eTotem da Imortalidade").toItemStack(),
			new ItemBuilder(Material.TNT).setName("§eTNT").toItemStack(),
			new ItemBuilder(Material.SNOWBALL, 4).setName("§eBola de Neve").toItemStack(),
			new ItemBuilder(Material.GOLDEN_APPLE).setName("§eMaçã Dourada").toItemStack(),
			new ItemBuilder(Material.POTION).setName("§ePoção de Velocidade").setPotionEffect(PotionEffectType.SPEED, PotionType.SPEED, 30 * 20, 2, false).toItemStack(),
			new ItemBuilder(Material.POTION).setName("§ePoção de Resistencia ao Fogo").setPotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionType.FIRE_RESISTANCE, 30 * 20, 1, false).toItemStack(),
			new ItemBuilder(Material.POTION).setName("§ePoção de Regeneração").setPotionEffect(PotionEffectType.REGENERATION, PotionType.REGEN, 30 * 20, 2, false).toItemStack(),
			new ItemBuilder(Material.SPLASH_POTION).setName("§ePoção de Lentidão").setPotionEffect(PotionEffectType.SLOW, PotionType.SLOWNESS, 30 * 20, 4, false).toItemStack(),
			new ItemBuilder(Material.SPLASH_POTION).setName("§ePoção de Cura").setPotionEffect(PotionEffectType.HEAL, PotionType.INSTANT_HEAL, 30 * 20, 2, false).toItemStack(),
			new ItemBuilder(Material.SPLASH_POTION).setName("§ePoção de Dano").setPotionEffect(PotionEffectType.HARM, PotionType.INSTANT_DAMAGE, 30 * 20, 2, false).toItemStack(),
	};

	private Hologram hologram;

	private Location location;

	private int secondsToSpawn;

	private int secondsToDespawn;
	
	public Feast(Location location) {
		this.location = location;

		this.hologram = new Hologram(this.location.clone().add(0, 1, 0), Arrays.asList("§6§lFEAST", "-"));

		this.hologram.spawn();

		this.destroy();

		beginSpawnCountdown();
	}

	private Location[] getChestLocs() {
		return new Location[] {
				getChestLoc(2, -2),
				getChestLoc(2, 0),
				getChestLoc(2, 2),
				getChestLoc(1, -1),
				getChestLoc(1, 1),
				getChestLoc(0, -2),
				getChestLoc(0, 0),
				getChestLoc(0, 2),
				getChestLoc(-1, -1),
				getChestLoc(-1, 1),
				getChestLoc(-2, -2),
				getChestLoc(-2, 0),
				getChestLoc(-2, 2)
		};
	}

	// 3 - 5 items per chest
	// 0 - 26 slots in the inventory

	public void spawn() {
		Arrays.stream(getChestLocs()).forEach(location -> {
			location.getBlock().setType(Material.CHEST);

			if (location.getBlock().getState() instanceof Chest) {

				Chest chest = (Chest)location.getBlock().getState();

				// generate number of items
				// generate for each number a slot
				// place item in slot if slot isn't null

				Random random = new Random();

				int randomNumberOfItems = random.nextInt(3) + 3; // 3 - 5

				int i = 0;

				while (i < randomNumberOfItems) {

					int randomSlot = random.nextInt(27);

					ItemStack loot = defaultLoot[random.nextInt(defaultLoot.length)];

					if (chest.getInventory().getItem(randomSlot) != null) {
						continue;
					}

					if (chest.getInventory().contains(loot)) {
						continue;
					}

					chest.getInventory().setItem(randomSlot, loot);
					i++;
				}
			}
		});

		hologram.setLine(1, "§bBaús spawnados!");

		beginDespawnCountdown();
	}

	public void destroy() {
		Arrays.stream(getChestLocs()).forEach(location -> {
			location.getBlock().setType(Material.AIR);
		});
	}
	
	public Location getLocation() {
		return this.location;
	}

	public static ItemStack[] getDefaultLoot() {
		return defaultLoot;
	}

	private Location getChestLoc(int xOffset, int zOffset) {
		return new Location(Bukkit.getWorld("world"), this.location.getX() + xOffset, this.location.getY(), this.location.getZ() + zOffset);
	}

	public void beginDespawnCountdown() {
		this.secondsToDespawn = SECONDS_TO_DESPAWN;

		new BukkitRunnable() {
			@Override
			public void run() {
				hologram.setLine(1, "§c" + ChatFormatter.formatSeconds(secondsToDespawn));

				if (secondsToDespawn < 1) {
					destroy();
					beginCooldown();
					cancel();
				}

				secondsToDespawn--;
			}
		}.runTaskTimer(Main.getInstance(), 20 * 5, 20);
	}

	public void beginSpawnCountdown() {
		this.secondsToSpawn = SECONDS_TO_SPAWN;

		new BukkitRunnable() {
			@Override
			public void run() {
				hologram.setLine(1, "§a" + ChatFormatter.formatSeconds(secondsToSpawn));

				if (secondsToSpawn < 1) {
					spawn();
					cancel();
				}

				secondsToSpawn--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

	public void beginCooldown() {
		hologram.setLine(1, "§eEm cooldown...");

		new BukkitRunnable() {
			@Override
			public void run() {
				beginSpawnCountdown();
			}
		}.runTaskLater(Main.getInstance(), 20 * SECONDS_TO_COOLDOWN);
	}
}
