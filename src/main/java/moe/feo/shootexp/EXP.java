package moe.feo.shootexp;

import moe.feo.shootexp.NMS.ItemNBTSet;
import moe.feo.shootexp.NMS.NMS_Class;
import moe.feo.shootexp.config.Config;
import moe.feo.shootexp.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示一滩粘稠的经验
 */
public class EXP {

	private static final NamespacedKey ownerKey = new NamespacedKey(ShootEXP.getPlugin(ShootEXP.class), "owner");
	private static final NamespacedKey recipientKey = new NamespacedKey(ShootEXP.getPlugin(ShootEXP.class), "recipient");
	private static final NamespacedKey amountKey = new NamespacedKey(ShootEXP.getPlugin(ShootEXP.class), "amount");

	private final String owner;
	private final String recipient;
	private final int amount;
	private final ItemStack item;

	/**
	 * 构造一滩粘稠的经验
	 * @param owner
	 * 经验所有者
	 * @param recipient
	 * 经验赠予者
	 * @param amount
	 * 经验数量
	 */
	public EXP(String owner, String recipient, int amount) {
		this.owner = owner;
		this.recipient = recipient;
		this.amount = amount;
		this.item = genEXPItem();
	}

	/**
	 * 构造一滩粘稠的经验
	 * @param item
	 * 经验物品
	 */
	public EXP(ItemStack item) {
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			assert meta != null;
			ItemNBTSet nbt  = new ItemNBTSet(item);
			this.owner = nbt.getString(ownerKey.getKey());
			this.recipient = nbt.getString(recipientKey.getKey());
			this.amount = nbt.getInt(amountKey.getKey());
		} else {
			this.owner = null;
			this.recipient = null;
			this.amount = 0;
		}
		this.item = item;
	}

	/**
	 * 判断物品是否为粘稠的经验
	 * @param item
	 * 物品
	 * @return 物品是否为粘稠的经验
	 */
	public static boolean isEXPItem(ItemStack item) {
		if (NMS_Class.version.equalsIgnoreCase("v1_12_R1")) {
			if (item.getType() != Material.getMaterial("INK_SACK") && !item.hasItemMeta() && item.getDurability() != 15) {
				return false;
			}
		} else {
			if (item.getType() != Material.getMaterial("BONE_MEAL") && !item.hasItemMeta()) {
				return false;
			}
		}
		ItemMeta meta = item.getItemMeta();
		assert meta != null;
		ItemNBTSet nbt = new ItemNBTSet(item);
		return nbt.hasKey(ownerKey.getKey()) || nbt.hasKey(recipientKey.getKey()) ||
				nbt.hasKey(amountKey.getKey());
	}

	/**
	 * 生成一个经验物品
	 * @return 生成的经验物品
	 */
	private ItemStack genEXPItem() {
		ItemStack item;
		if (NMS_Class.version.equalsIgnoreCase("v1_12_R1"))
			item = new ItemStack(Material.getMaterial("INK_SACK"), 1, (short) 15);
		else
			item = new ItemStack(Material.getMaterial("BONE_MEAL"));
		ItemMeta meta = item.getItemMeta();
		String itemName = Language.ITEM_NAME.getString().replace("%OWNER%", owner)
				.replace("%RECIPIENT%", recipient).replace("%AMOUNT%", Integer.toString(amount));
		List<String > loreList = new ArrayList<>();
		for (String lore : Language.ITEM_LORE.getStringList()) {
			loreList.add(lore.replace("%OWNER%", owner).replace("%RECIPIENT%", recipient).
					replace("%AMOUNT%", Integer.toString(amount)));
		}
		assert meta != null;
		meta.setDisplayName(itemName);
		meta.setLore(loreList);
		if (!NMS_Class.version.equalsIgnoreCase("v1_12_R1")) {
			if (Config.CUSTOM_MODEL_DATA_ENABLE.getBoolean()) {
				meta.setCustomModelData(Config.CUSTOM_MODEL_DATA_VALUE.getInt());
			}
		}
		item.setItemMeta(meta);
		ItemNBTSet nbt = new ItemNBTSet(item);
		nbt.setString(ownerKey.getKey(),  owner);
		nbt.setString(recipientKey.getKey(), recipient);
		nbt.setInt(amountKey.getKey(), amount);
		item = nbt.saveNBT();
		return item;
	}

	/**
	 * 获取经验物品
	 * @return 经验物品
	 */
	public ItemStack getEXPItem() {
		return item;
	}

	/**
	 * 获取经验所有者的名称
	 * @return 经验所有者的名称
	 */
	public String getOwner() {
		if (owner == null) {
			return "UNKNOWN";
		}
		return owner;
	}

	/**
	 * 获取经验的赠予者
	 * @return 经验的赠予者
	 */
	public String getRecipient() {
		if (recipient == null) {
			return "UNKNOWN";
		}
		return recipient;
	}

	/**
	 * 获取这滩经验所蕴含的经验数量
	 * @return 经验的数量
	 */
	public int getAmount() {
		return amount;
	}
}
