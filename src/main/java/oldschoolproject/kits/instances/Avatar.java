package oldschoolproject.kits.instances;

import oldschoolproject.kits.BaseKit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import oldschoolproject.Main;
import oldschoolproject.managers.UserManager;
import oldschoolproject.users.User;
import oldschoolproject.utils.builders.ItemBuilder;

public class Avatar extends BaseKit {
	
	public Avatar() {
		super(
				"Avatar",
				20,
				new ItemBuilder(Material.BEACON).toItemStack(),
				5
				);
	}

	@Override
	public BaseKit createInstance() {
		return new Avatar();
	}
	
	int index = -1; // Neutral beacon item starting point
	
	public void swapBeam(User user) {
		index++;
		
		if (index == 4) index = 0;
		
		user.getKit().getSkillItem().setType(BeamType.values()[index].getMaterial());
		
		user.getPlayer().getInventory().setItemInMainHand(user.getKit().getSkillItem());
	}
	
	@Override
	public boolean activateSkill(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			
			User user = UserManager.getUser(e.getPlayer());
			
			swapBeam(user);
			
			return false;
		}
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			BlockIterator bi = new BlockIterator(e.getPlayer().getEyeLocation(), 0, 24);
			
			new BukkitRunnable() {
				public void run() {
					if (bi.hasNext()) {
						
						Location loc = bi.next().getLocation();
						loc.getWorld().playEffect(loc, Effect.STEP_SOUND, BeamType.values()[index].getMaterial());
						
						for (Entity nearby : loc.getWorld().getNearbyEntities(loc, 2.5D, 2.5D, 2.5D)) {
							if (nearby == e.getPlayer()) {
								continue;
							}
							
							if (nearby instanceof Player && UserManager.getUser((Player)nearby).isProtected()) {
								continue;
							}
							
							if (!(nearby instanceof LivingEntity)) {
								continue;
							}
							
							((LivingEntity)nearby).setNoDamageTicks(0);
							((LivingEntity)nearby).damage(BeamType.values()[index].getDamage(), e.getPlayer());
							((LivingEntity)nearby).setFireTicks(BeamType.values()[index].getFireTicks());
							
//							if (BeamType.values()[index].getPotionEffect() != null) {
								((LivingEntity)nearby).addPotionEffect(BeamType.values()[index].getPotionEffect());
//							}
						}
					} else {
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
		return true;
	}
	
	public enum BeamType {
		
		Air(Material.QUARTZ_BLOCK, 0.5D, 0, new PotionEffect(PotionEffectType.WEAKNESS, 20 * 3, 2)),
		Water(Material.LAPIS_BLOCK, 0.5D, 0, new PotionEffect(PotionEffectType.BLINDNESS, 20, 1)),
		Earth(Material.GRASS_BLOCK, 0.5D, 0, new PotionEffect(PotionEffectType.SLOW, 20, 1)),
		Fire(Material.REDSTONE_BLOCK, 0.5D, 60, new PotionEffect(PotionEffectType.CONFUSION, 20 * 3, 2));

		Material material;
		Double damage;
		Integer fireTicks;
		PotionEffect potionEffect;
		
		BeamType(Material material, Double damage, Integer fireTicks, PotionEffect potionEffect) {
			this.material = material;
			this.damage = damage;
			this.fireTicks = fireTicks;
			this.potionEffect = potionEffect;
		}
		
		Material getMaterial() {
			return material;
		}
	
		Double getDamage() {
			return damage;
		}
		
		Integer getFireTicks() {
			return fireTicks;
		}
		
		PotionEffect getPotionEffect() {
			return potionEffect;
		}
	}
}
