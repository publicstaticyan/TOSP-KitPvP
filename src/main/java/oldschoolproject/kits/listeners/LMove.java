package oldschoolproject.kits.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import oldschoolproject.managers.KitManager;
import oldschoolproject.managers.UserManager;
import oldschoolproject.users.User;
import oldschoolproject.events.BaseListener;

public class LMove implements BaseListener {
	
	@EventHandler
	public void onMoveThroughSponge(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		User user = UserManager.getUser(p);
		
		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE) {
			// launch player fowards
			KitManager.giveKit(user);
		}
	}
}
