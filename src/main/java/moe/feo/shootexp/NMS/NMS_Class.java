package moe.feo.shootexp.NMS;

import org.bukkit.Bukkit;

import java.lang.reflect.Method;

public class NMS_Class {
    public static Class NBTTagCompound;
    public static Class NBTBase;
    public static Class NBTTagList;
    public static Class ItemStack;
    public static Class CraftItemStack;
    public static Class NBTTagString;

    public static Method NBT_isEmpty;
    public static Method NBT_setBoolean;
    public static Method NBT_setString;
    public static Method NBT_setInt;
    public static Method NBT_set;
    public static Method NBT_hasKey;
    public static Method NBT_getBoolean;
    public static Method NBT_getString;
    public static Method NBT_getInt;
    public static Method NBT_getKeys;
    public static Method NBT_getList;
    public static Method NBT_get;
    public static Method NBT_remove;

    public static Method NBTTagString_new;
    public static Method NBTTagList_add;
    public static Method NBTTagList_remove;

    public static Method CraftItemStack_asNMSCopy;
    public static Method CraftItemStack_asBukkitCopy;

    public static Method ItemStack_setTag;
    public static Method ItemStack_getTag;
    public static Method ItemStack_cloneItemStack;

    public static String version;

    public static void init() {
        try {
            String server = Bukkit.getServer().getClass().getPackage().getName();
            version = server.split("\\.")[3];
            NBTTagCompound = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
            ItemStack = Class.forName("net.minecraft.server." + version + ".ItemStack");
            CraftItemStack = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
            NBTBase = Class.forName("net.minecraft.server." + version + ".NBTBase");
            NBTTagList = Class.forName("net.minecraft.server." + version + ".NBTTagList");
            NBTTagString = Class.forName("net.minecraft.server." + version + ".NBTTagString");

            NBT_isEmpty = NBTTagCompound.getMethod("isEmpty");
            NBT_setBoolean = NBTTagCompound.getMethod("setBoolean", String.class, boolean.class);
            NBT_setString = NBTTagCompound.getMethod("setString", String.class, String.class);
            NBT_setInt = NBTTagCompound.getMethod("setInt", String.class, int.class);
            NBT_set = NBTTagCompound.getMethod("set", String.class, NMS_Class.NBTBase);
            NBT_hasKey = NBTTagCompound.getMethod("hasKey", String.class);
            NBT_getBoolean = NBTTagCompound.getMethod("getBoolean", String.class);
            NBT_getString = NBTTagCompound.getMethod("getString", String.class);
            NBT_getInt = NBTTagCompound.getMethod("getInt", String.class);
            NBT_getList = NBTTagCompound.getMethod("getList", String.class, int.class);
            NBT_get = NBTTagCompound.getMethod("get", String.class);
            NBT_remove = NBTTagCompound.getMethod("remove", String.class);

            NBTTagList_remove = NBTTagList.getMethod("remove", int.class);

            ItemStack_setTag = ItemStack.getMethod("setTag", NMS_Class.NBTTagCompound);
            ItemStack_getTag = ItemStack.getMethod("getTag");
            ItemStack_cloneItemStack = ItemStack.getMethod("cloneItemStack");

            CraftItemStack_asNMSCopy = CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            CraftItemStack_asBukkitCopy = CraftItemStack.getMethod("asBukkitCopy", NMS_Class.ItemStack);

            if (version.equalsIgnoreCase("v1_16_R3")) {
                NBT_getKeys = NBTTagCompound.getMethod("getKeys");
                NBTTagString_new = NBTTagString.getMethod("a", String.class);
                NBTTagList_add = NBTTagList.getMethod("add", int.class, NMS_Class.NBTBase);
            } else if (version.equalsIgnoreCase("v1_12_R1")) {
                NBT_getKeys = NBTTagCompound.getMethod("c");
                NBTTagList_add = NBTTagList.getMethod("add", NMS_Class.NBTBase);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
