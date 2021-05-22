package moe.feo.shootexp;

import moe.feo.shootexp.NMS.NMS_Class;
import moe.feo.shootexp.config.Config;
import moe.feo.shootexp.config.Language;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 插件主类
 */
public class ShootEXP extends JavaPlugin {

	@Override
	public void onEnable() {
		NMS_Class.init();
		saveDefaultConfig();
		Config.load();
		Language.saveDefault();
		Language.load();
		getServer().getPluginManager().registerEvents(new AttackListener(), this);
		getServer().getPluginManager().registerEvents(new EatListener(), this);
		this.getCommand("shootexp").setExecutor(Commands.getInstance());
		this.getCommand("shootexp").setTabCompleter(Commands.getInstance());
	}

	@Override
	public void onDisable() {

	}
}
