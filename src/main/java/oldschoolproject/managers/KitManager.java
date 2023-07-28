package oldschoolproject.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import oldschoolproject.kits.PvP;
import oldschoolproject.users.User;
import oldschoolproject.users.User.UserState;
import oldschoolproject.utils.builders.ItemBuilder;
import oldschoolproject.utils.kits.BaseKit;
import oldschoolproject.utils.kits.KitLoader;
import oldschoolproject.warps.Spawn;

public class KitManager {
	
	// Direct command
	public static void setKit(User user, String kitName) {
		Player p = user.getPlayer();
		
		if (!kitExists(kitName)) {
			p.sendMessage("§cErro: Kit inexistente: " + kitName); 
			return;
		}
		
		if (!(user.getWarp() instanceof Spawn)) {
			p.sendMessage("§cErro: Não é possivel selecionar um kit fora do spawn");
			return;
		}
		
		if (!user.isProtected()) {
			p.sendMessage("§cErro: Você já recebeu um kit");
			return;
		}
		
		user.setKit(findKit(kitName).createInstance());
		
		p.sendMessage("§eKit selecionado: " + kitName.substring(0, 1).toUpperCase() + kitName.substring(1).toLowerCase());
	}
	
	// Steping on sponge
	public static void giveKit(User user) {
		Player p = user.getPlayer();

		// Doesn't make sense to send message every time unprotected player steps into sponge block
		if (!(user.getWarp() instanceof Spawn)) {
			return;
		}
		
		// This guard exists for one reason: Having a kit doesn't mean you're playing, you can have a kit while beign protected
		// Not beign protected means you're playing
		if (!user.isProtected()) {
			return;
		}
		
		// this doesn't matter for the upper guard conditions
		if (!user.hasKit()) {
			user.setKit(new PvP());
			p.sendMessage("§eKit PvP selecionado automaticamente");
		}
		
		setupInventory(user);
		
		user.setState(UserState.Playing);
		
		p.sendMessage("§aVocê recebeu o kit: " + user.getKit().getName());
	}
	
	public static BaseKit findKit(String kitName) {
		return KitLoader.getKitInstances().stream().filter(kit -> kit.getName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
	}
	
	public static boolean kitExists(String kitName) {
		return findKit(kitName) != null;
	}
	
	public static void setupInventory(User user) {
		Inventory inv = user.getPlayer().getInventory();
		
		inv.clear();
		
		inv.setItem(0, user.getKit() instanceof PvP ?
				new ItemBuilder(Material.STONE_SWORD).setName("§aEspada").toItemStack() 
				: 
				new ItemBuilder(Material.WOODEN_SWORD).setName("§aEspada").toItemStack()
		);
		
		inv.setItem(1, user.getKit().getSkillItem() == null ?
				new ItemBuilder(Material.AIR).toItemStack()
				:
				new ItemBuilder(user.getKit().getSkillItem()).setName("§b" + user.getKit().getName()).toItemStack()
		);
		
		for (int i = 0; i < inv.getSize(); i++) {
			inv.addItem(new ItemBuilder(Material.MUSHROOM_STEW).setName("§6Sopa").toItemStack());
		}
	}

}