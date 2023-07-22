package oldschoolproject.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import oldschoolproject.managers.UserManager;
import oldschoolproject.menus.WarpsMenu;
import oldschoolproject.utils.commands.BaseCommand;

public class CWarpSelector extends BaseCommand {

	public CWarpSelector() {
		super("warpinv");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		
		new WarpsMenu(UserManager.getUser(p)).open();
	}
	
	

}