package mod.steamnsteel.api.voxbox;

import net.minecraft.item.ItemStack;

public interface IVoxBoxDictionary {

    public IVoxBoxEntry getEntry(String unlocalizedName);
    public IVoxBoxEntry getEntry(ItemStack stack);
    public void addEntry(ItemStack stack, IVoxBoxEntry entry);
    public void removeEntry(String name);
    public void removeEntry(ItemStack stack);
    public boolean containsEntry(String unlocalizedName);
    public boolean containsEntry(ItemStack stack);
}
