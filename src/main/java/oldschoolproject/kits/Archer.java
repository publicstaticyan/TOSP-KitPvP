package oldschoolproject.kits;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import oldschoolproject.utils.builders.ItemBuilder;
import oldschoolproject.utils.kits.BaseKit;

public class Archer extends BaseKit {

	public Archer() {
		super("Archer", new ItemBuilder(Material.BOW).toItemStack(), null);
	}

	@Override
	public boolean activateSkill(PlayerInteractEvent e) {
		return false;
	}

	@Override
	public BaseKit createInstance() {
		return new Archer();
	}

}
