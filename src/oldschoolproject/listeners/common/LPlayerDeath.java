package oldschoolproject.listeners.common;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import oldschoolproject.utils.loaders.listener.BaseListener;

public class LPlayerDeath extends BaseListener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		e.getDrops().clear();
	}
}
