package moe.feo.shootexp.NMS;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class ItemNBTSet {
    private final ItemStack item;
    private Object NBT;

    public ItemNBTSet(ItemStack item) {
        this.item = item;
        try {
            // net.minecraft.server.v1_16_R3.ItemStack NMSItem = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);
            Object NMSItem = NMS_Class.CraftItemStack_asNMSCopy.invoke(null, item);
            //NBT = NMSItem.getTag();
            NBT = NMS_Class.ItemStack_getTag.invoke(NMSItem);
            if (NBT == null)
//                NBT = new net.minecraft.server.v1_12_R1.NBTTagCompound();
                NBT = NMS_Class.NBTTagCompound.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemStack saveNBT() {
        try {
            //net.minecraft.server.v1_16_R3.ItemStack NMSItem = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);
            Object NMSItem = NMS_Class.CraftItemStack_asNMSCopy.invoke(null, item);
            //NMSItem.setTag(NBT);
            NMS_Class.ItemStack_setTag.invoke(NMSItem, NBT);
            //return org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asBukkitCopy(NMSItem.cloneItemStack());
            return (ItemStack) NMS_Class.CraftItemStack_asBukkitCopy.invoke(null, NMS_Class.ItemStack_cloneItemStack.invoke(NMSItem));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean haveNBT() {
        //return !NBT.isEmpty();
        try {
            return !(boolean) NMS_Class.NBT_isEmpty.invoke(NBT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setString(String key, String value) {
        //NBT.setString(key, value);
        try {
            NMS_Class.NBT_setString.invoke(NBT, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasKey(String key) {
        //return NBT.hasKey(key);
        try {
            return (boolean) NMS_Class.NBT_hasKey.invoke(NBT, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getString(String key) {
        //return NBT.getString(key);
        try {
            return (String) NMS_Class.NBT_getString.invoke(NBT, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBoolean(String key, boolean value) {
        //NBT.setBoolean(key, b);
        try {
            NMS_Class.NBT_setBoolean.invoke(NBT, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInt(String key, int value) {
        //NBT.setInt(key, x1);
        try {
            NMS_Class.NBT_setInt.invoke(NBT, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key) {
        //return NBT.getBoolean(key);
        try {
            return (boolean) NMS_Class.NBT_getBoolean.invoke(NBT, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getInt(String key) {
        //return NBT.getInt(key);
        try {
            return (int) NMS_Class.NBT_getInt.invoke(NBT, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Set<String> getNBTs() {
        //return NBT.getKeys();
        try {
            return (Set<String>) NMS_Class.NBT_getKeys.invoke(NBT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeNBT(String key) {
        //NBT.remove(s);
        try {
            NMS_Class.NBT_remove.invoke(NBT, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NBTTagList_Utils getStringList(String key) {
        //return NBT.getList(key, 8);
        try {
            return new NBTTagList_Utils(NMS_Class.NBT_getList.invoke(NBT, key, 8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStringList(String key, List<String> list) {
        try {
            //net.minecraft.server.v1_16_R3.NBTBase base = NBT.get(s);
            Object base = NMS_Class.NBT_get.invoke(NBT, key);
            if (base == null)
//                base = new net.minecraft.server.v1_16_R3.NBTTagList();
                base = NMS_Class.NBTTagList.newInstance();
//            if (base instanceof net.minecraft.server.v1_16_R3.NBTTagList) {
            if (NMS_Class.NBTTagList.isInstance(base)) {
//                net.minecraft.server.v1_16_R3.NBTTagList list1 = (net.minecraft.server.v1_16_R3.NBTTagList) base;
                NBTTagList_Utils list1 = new NBTTagList_Utils(base);
                for (String item : list) {
//                    list1.add(new net.minecraft.server.v1_16_R3.NBTTagString.a(item));
                    list1.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public void setStringList(String s, net.minecraft.server.v1_16_R3.NBTTagList list) {
    public void setStringList(String key, NBTTagList_Utils list) {
//        NBT.set(key, list);
        try {
            NMS_Class.NBT_set.invoke(NBT, key, list.NBTTagList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
