package moe.feo.shootexp;

import moe.feo.shootexp.config.Config;
import moe.feo.shootexp.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 命令执行器
 */
public class Commands implements TabExecutor {

	private static final Commands executor = new Commands();

	private Commands() {
	}

	public static Commands getInstance() {
		return executor;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length == 1) {
			List<String> list = new ArrayList<>();
			String main = args[0].toLowerCase();
			if ("help".startsWith(main)) list.add("help");
			if ("status".startsWith(main) && sender.hasPermission("shootexp.status")) {
				list.add("status");
			}
			if ("item".startsWith(main) && sender.hasPermission("shootexp.item")) {
				list.add("item");
			}
			if ("restore".startsWith(main) && sender.hasPermission("shootexp.restore")) {
				list.add("restore");
			}
			if ("set".startsWith(main) && sender.hasPermission("shootexp.set")) {
				list.add("set");
			}
			if ("reload".startsWith(main) && sender.hasPermission("shootexp.reload")) {
				list.add("reload");
			}
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		}
		if (args.length == 2) {
			List<String> list = new ArrayList<>();
			String main = args[0];
			String sub1 = args[1];
			if ("restore".equals(main) && sender.hasPermission("shootexp.restore")) {
				if ("all".startsWith(sub1)) {
					list.add("all");
				}
				if ("times".startsWith(sub1)) {
					list.add("times");
				}
				if ("stock".startsWith(sub1)) {
					list.add("stock");
				}
			}
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {// 没有带参数
			String[] helpArgs = { "help" };
			onCommand(sender, cmd, label, helpArgs);
			return true;
		}
		switch (args[0].toLowerCase()) {
			case "help": {
				sender.sendMessage(Language.COMMAND_HELP_TITLE.getString());
				sender.sendMessage(Language.COMMAND_HELP_HELP.getString());
				if (sender.hasPermission("shootexp.status")) {
					sender.sendMessage(Language.COMMAND_HELP_STATUS.getString());
				}
				if (sender.hasPermission("shootexp.item")) {
					sender.sendMessage(Language.COMMAND_HELP_ITEM.getString());
				}
				if (sender.hasPermission("shootexp.restore")) {
					sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
				}
				if (sender.hasPermission("shootexp.set")) {
					sender.sendMessage(Language.COMMAND_HELP_SET.getString());
				}
				if (sender.hasPermission("shootexp.reload")) {
					sender.sendMessage(Language.COMMAND_HELP_RELOAD.getString());
				}
				break;
			}
			case "status": {
				if (!sender.hasPermission("shootexp.status")) {
					sender.sendMessage(Language.COMMAND_NO_PERMISSION.getString());
					break;
				}
				int times = 0;
				int stock = Config.MAX_STOCK.getInt();
				String name;
				UUID uuid;
				if (args.length == 1) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(Language.COMMAND_PLAYER_ONLY.getString());
						break;
					}
					name = sender.getName();
					uuid = ((Player) sender).getUniqueId();
				} else if (args.length == 2) {
					name = args[1];
					uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
				} else {
					sender.sendMessage(Language.COMMAND_INVALID.getString());
					sender.sendMessage(Language.COMMAND_HELP_STATUS.getString());
					break;
				}
				if (PlayerStatusManager.hasStatus(uuid)) {
					PlayerStatus status = PlayerStatusManager.getStatus(uuid);
					times = status.getTimesOfShoot();
					stock = status.getStock();
				}
				String msg = Language.COMMAND_STATUS.getString().replace("%PLAYER%", name)
						.replace("%TIMES%", Integer.toString(times)).replace("%STOCK%", Integer.toString(stock));
				sender.sendMessage(msg);
				break;
			}
			case "item": {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Language.COMMAND_PLAYER_ONLY.getString());
					break;
				}
				if (!sender.hasPermission("shootexp.item")) {
					sender.sendMessage(Language.COMMAND_NO_PERMISSION.getString());
					break;
				}
				if (args.length != 4) {
					sender.sendMessage(Language.COMMAND_INVALID.getString());
					sender.sendMessage(Language.COMMAND_HELP_ITEM.getString());
					break;
				}
				String attacker = args[1];
				String defender = args[2];
				int amount;
				try {
					amount = Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					sender.sendMessage(Language.COMMAND_INVALID.getString());
					sender.sendMessage(Language.COMMAND_HELP_ITEM.getString());
					break;
				}
				EXP exp = new EXP(attacker, defender, amount);
				Player player = (Player) sender;
				player.getInventory().addItem(exp.getEXPItem());
				break;
			}
			case "restore": {
				if (!sender.hasPermission("shootexp.restore")) {
					sender.sendMessage(Language.COMMAND_NO_PERMISSION.getString());
					break;
				}
				if (!(args.length >= 3)) {
					sender.sendMessage(Language.COMMAND_INVALID.getString());
					sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
					break;
				}
				String type = args[1];
				String player = args[2];
				UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
				switch (type) {
					case "all": {
						if (args.length != 3) {
							sender.sendMessage(Language.COMMAND_INVALID.getString());
							sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
							break;
						}
						// 如果有状态就将它恢复满
						if (PlayerStatusManager.hasStatus(uuid)) {
							PlayerStatus status = PlayerStatusManager.getStatus(uuid);
							status.restoreShootFull();
							status.restoreStockFull();
						}
						sender.sendMessage(Language.COMMAND_RESTORE.getString());
						break;
					}
					case "times": {
						if (args.length != 4) {
							sender.sendMessage(Language.COMMAND_INVALID.getString());
							sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
							break;
						}
						// 参数是否是数字
						int times;
						try {
							times = Integer.parseInt(args[3]);
						} catch (NumberFormatException e) {
							sender.sendMessage(Language.COMMAND_INVALID.getString());
							sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
							break;
						}
						PlayerStatus status;
						if (PlayerStatusManager.hasStatus(uuid)) {
							status = PlayerStatusManager.getStatus(uuid);
						} else {
							status = new PlayerStatus();
							PlayerStatusManager.addStatus(uuid, status);
						}
						status.restoreShoot(times);
						sender.sendMessage(Language.COMMAND_RESTORE.getString());
						break;
					}
					case "stock": {
						if (args.length != 4) {
							sender.sendMessage(Language.COMMAND_INVALID.getString());
							sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
							break;
						}
						// 参数是否为数字
						int stock;
						try {
							stock = Integer.parseInt(args[3]);
						} catch (NumberFormatException e) {
							sender.sendMessage(Language.COMMAND_INVALID.getString());
							sender.sendMessage(Language.COMMAND_HELP_RESTORE.getString());
							break;
						}
						PlayerStatus status;
						if (PlayerStatusManager.hasStatus(uuid)) {
							status = PlayerStatusManager.getStatus(uuid);
						} else {
							status = new PlayerStatus();
							PlayerStatusManager.addStatus(uuid, status);
						}
						status.restoreStock(stock);
						sender.sendMessage(Language.COMMAND_RESTORE.getString());
						break;
					}
				}
				break;
			}
			case "set": {
				if (!sender.hasPermission("shootexp.set")) {
					sender.sendMessage(Language.COMMAND_NO_PERMISSION.getString());
					break;
				}
				if (args.length != 4) {
					sender.sendMessage(Language.COMMAND_INVALID.getString());
					sender.sendMessage(Language.COMMAND_HELP_SET.getString());
					break;
				}
				String player = args[1];
				int times;
				int stock;
				try {
					times = Integer.parseInt(args[2]);
					stock = Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					sender.sendMessage(Language.COMMAND_INVALID.getString());
					sender.sendMessage(Language.COMMAND_HELP_SET.getString());
					break;
				}
				UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
				PlayerStatus status;
				if (PlayerStatusManager.hasStatus(uuid)) {
					status = PlayerStatusManager.getStatus(uuid);
				} else {
					status = new PlayerStatus();
					PlayerStatusManager.addStatus(uuid, status);
				}
				status.setTimesOfShoot(times);
				status.setStock(stock);
				sender.sendMessage(Language.COMMAND_SET.getString());
				break;
			}
			case "reload": {
				if (!sender.hasPermission("shootexp.reload")) {
					sender.sendMessage(Language.COMMAND_NO_PERMISSION.getString());
					break;
				}
				ShootEXP.getPlugin(ShootEXP.class).saveDefaultConfig();
				Config.load();
				Language.saveDefault();
				Language.load();
				sender.sendMessage(Language.COMMAND_RELOADED.getString());
				break;
			}
			default: {
				sender.sendMessage(Language.COMMAND_INVALID.getString());
				sender.sendMessage(Language.COMMAND_HELP_HELP.getString());
			}
		}
		return true;
	}
}
