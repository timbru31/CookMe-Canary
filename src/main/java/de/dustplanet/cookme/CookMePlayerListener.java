package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.Random;
import net.canarymod.Canary;
import net.canarymod.api.DamageType;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.factory.PotionFactory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.potion.PotionEffect;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.EatHook;
import net.canarymod.plugin.PluginListener;

/**
 * CookMe for Canary (recode)
 * Handles the players activities!
 *
 * Refer to the forum thread:
 * http://forums.canarymod.net/?topic=3523.0
 *
 * @author xGhOsTkiLLeRx
 *
 */

public class CookMePlayerListener implements PluginListener {
    private CookMe plugin;
    private boolean message = true;
    private Random random = new Random();
    private PotionFactory factory;

    public CookMePlayerListener(CookMe instance) {
	plugin = instance;
	factory = Canary.factory().getPotionFactory();
    }

    @HookHandler
    public void onEat(EatHook hook) {
	Player player = hook.getPlayer();
	Item item = hook.getItem();
	// Just in case to prevent NPE
	String effect = "damage";
	Timestamp now = new Timestamp(System.currentTimeMillis());
	// Check if player is affected
	// EffectList
	PotionEffect[] effects = null;
	if (!player.hasPermission("cookme.safe")) {
	    // Check for item & right clicking
	    if (sameItem(item) && !plugin.cooldownManager.hasCooldown(player, now)) {
		// Only 1 effect
		effects = new PotionEffect[1];
		// Make a temp double and a value between 0 and 99
		double temp = 0;
		int i = 0;
		// Get the number for the effect
		for(i = 0; i < plugin.percentages.length; i++) {
		    temp += plugin.percentages[i];
		    if (random.nextInt(100) <= temp) {
			break;
		    }
		}
		// EffectStrenght, Duration etc.
		int randomEffectStrength = random.nextInt(16);
		int randomEffectTime = (random.nextInt((plugin.maxDuration - plugin.minDuration)  + 1)  +  plugin.minDuration);
		// Player gets random damage, stack minus 1
		if (i == 0) {
		    int randomDamage = random.nextInt(9) + 1;
		    effect = plugin.localization.getString("damage");
		    player.dealDamage(DamageType.GENERIC, randomDamage);
		}			
		// Player dies, stack minus 1
		if (i == 1) {
		    effect = plugin.localization.getString("death");
		    player.kill();
		}
		// Random venom damage (including green hearts :) )
		if (i == 2) {
		    effect = plugin.localization.getString("venom");
		    effects[0] = factory.newPotionEffect(PotionEffectType.POISON, randomEffectTime, randomEffectStrength);
		}
		// Food bar turns green (poison)
		if (i == 3) {
		    effect = plugin.localization.getString("hungervenom");
		    effects[0] = factory.newPotionEffect(PotionEffectType.HUNGER, randomEffectTime, randomEffectStrength);
		}
		// TODO  negative amount?
		// Sets the food level down. Stack minus 1
		if (i == 4) {
		    int currentFoodLevel = player.getHunger();
		    int randomFoodLevel = 0;
		    if (currentFoodLevel != 0) {
			randomFoodLevel = random.nextInt(currentFoodLevel);
		    }
		    effect = plugin.localization.getString("hungerdecrease");
		    player.setHunger(randomFoodLevel);
		}
		// Confusion
		if (i == 5) {
		    effect = plugin.localization.getString("confusion");
		    effects[0] = factory.newPotionEffect(PotionEffectType.CONFUSION, randomEffectTime, randomEffectStrength);
		}
		// Blindness
		if (i == 6) {
		    effect = plugin.localization.getString("blindness");
		    effects[0] = factory.newPotionEffect(PotionEffectType.BLINDNESS, randomEffectTime, randomEffectStrength);
		}

		// Weakness
		if (i == 7) {
		    effect = plugin.localization.getString("weakness");
		    effects[0] = factory.newPotionEffect(PotionEffectType.WEAKNESS, randomEffectTime, randomEffectStrength);

		}
		// Slowness
		if (i == 8) {
		    effect = plugin.localization.getString("slowness");
		    effects[0] = factory.newPotionEffect(PotionEffectType.MOVESLOWDOWN, randomEffectTime, randomEffectStrength);

		}
		// Slowness for blocks
		if (i == 9) {
		    effect = plugin.localization.getString("slowness_blocks");
		    effects[0] = factory.newPotionEffect(PotionEffectType.DIGSLOWDOWN, randomEffectTime, randomEffectStrength);

		}
		// Instant Damage
		if (i == 10) {
		    effect = plugin.localization.getString("instant_damage");
		    effects[0] = factory.newPotionEffect(PotionEffectType.HARM, randomEffectTime, randomEffectStrength);

		}
		// Refusing
		if (i == 11) {
		    effect = plugin.localization.getString("refusing");
		    hook.setCanceled();
		}
		// Wither effect
		if (i == 12) {
		    effect = plugin.localization.getString("wither");
		    effects[0] = factory.newPotionEffect(PotionEffectType.WITHER, randomEffectTime, randomEffectStrength);
		}

		// Message
		message(player, effect);

		// No health or hunger
		hook.setLevelGain(0);
		hook.setSaturationGain(0);

		// Add player to cooldown list
		if (plugin.cooldown != 0) {
		    plugin.cooldownManager.addPlayer(player);
		}
	    }
	} else if (plugin.preventVanillaPoison) {
	    // Prevent the vanilla poison, too?
	    if (item.getType() == ItemType.RawChicken || item.getType() == ItemType.RottenFlesh) {
		effects = new PotionEffect[0];
	    }
	}
	
	// Finally set potions
	hook.setPotionEffects(effects);
    }

    private void message(Player player, String message) {
	if (plugin.messages) {
	    plugin.message(player, message, null, null);
	}
    }

    // Is the item in the list? Yes or no
    private boolean sameItem(Item item) {
	for (String itemName : plugin.itemList) {
	    // Get the Material
	    try {
		int ID = Integer.valueOf(itemName);
		if (ID == item.getId()) {
		    return true;
		}
	    } catch (NumberFormatException e) {
		try {
		    ItemType mat = ItemType.fromString(itemName);
		    // Get ID & compare
		    if (mat == item.getType()) {
			return true;
		    }
		}
		// Not valid
		catch (IllegalArgumentException e2) {
		    // Prevent spamming
		    if (message) {
			plugin.getLogman().logWarning("Couldn't load the foods! Please check your config!");
			plugin.getLogman().logWarning("The following item ID/name is invalid: " + itemName);
			message = false;
		    }
		}
	    }
	}
	return false;
    }
}