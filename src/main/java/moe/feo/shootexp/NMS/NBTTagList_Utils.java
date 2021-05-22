package moe.feo.shootexp.NMS;

public class NBTTagList_Utils {
    //net.minecraft.server.v1_16_R3.NBTTagList
    public Object NBTTagList;

    public NBTTagList_Utils(Object NBTTagList) {
        this.NBTTagList = NBTTagList;
    }


    public void add(String item) {
        try {
            NMS_Class.NBTTagList_add.invoke(NBTTagList, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(int index) {
        try {
            NMS_Class.NBTTagList_remove.invoke(NBTTagList, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
