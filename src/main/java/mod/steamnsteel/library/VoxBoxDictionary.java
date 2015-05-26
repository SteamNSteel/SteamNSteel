package mod.steamnsteel.library;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.api.voxbox.IVoxBoxDictionary;
import mod.steamnsteel.api.voxbox.IVoxBoxEntry;
import mod.steamnsteel.api.voxbox.VoxBoxEntryHelper;
import mod.steamnsteel.api.voxbox.VoxBoxHandler;
import mod.steamnsteel.utility.ItemWrapper;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.Map;

public enum VoxBoxDictionary implements IVoxBoxDictionary{
    INSTANCE;

    private static final Map<ItemWrapper, IVoxBoxEntry> entries = Maps.newHashMap();

    public static ImmutableMap<ItemWrapper, IVoxBoxEntry> getEntries(){
        return ImmutableMap.copyOf(entries);
    }

    @Override
    public IVoxBoxEntry getEntry(final String unlocalizedName) {
        if(containsEntry(unlocalizedName)) {
            for (Iterator<ItemWrapper> iterator = entries.keySet().iterator(); iterator.hasNext(); ) {
                ItemWrapper next = iterator.next();

                if (next.getStack().getUnlocalizedName().equals(unlocalizedName)) {
                    return entries.get(next);
                }
            }
        }

        return null;
    }

    @Override
    public IVoxBoxEntry getEntry(ItemStack stack) {
        if(containsEntry(stack)) {
            ItemWrapper wrapper = new ItemWrapper(stack);
            for (Iterator<ItemWrapper> iterator = entries.keySet().iterator(); iterator.hasNext(); ) {
                ItemWrapper next = iterator.next();

                if (wrapper.equals(next))
                    return entries.get(next);
            }
        }
        return null;
    }

    @Override
    public void addEntry(ItemStack stack, IVoxBoxEntry entry) {
        entries.put(new ItemWrapper(stack), entry);
    }

    @Override
    public void removeEntry(String name) {
        if(containsEntry(name)){
            for (Iterator<ItemWrapper> iterator = entries.keySet().iterator(); iterator.hasNext(); ) {
                ItemWrapper next = iterator.next();

                if(next.getStack().getUnlocalizedName().equals(name)) {
                    entries.remove(next);
                    return;
                }
            }
        }
    }

    @Override
    public void removeEntry(ItemStack stack) {
        if(containsEntry(stack)){
            ItemWrapper itemWrapper = new ItemWrapper(stack);
            for (Iterator<ItemWrapper> iterator = entries.keySet().iterator(); iterator.hasNext(); ) {
                ItemWrapper next = iterator.next();

                if(next.equals(itemWrapper)) {
                    entries.remove(next);
                    return;
                }
            }
        }
    }

    @Override
    public boolean containsEntry(String unlocalizedName) {
        for (Iterator<ItemWrapper> iterator = entries.keySet().iterator(); iterator.hasNext(); ) {
            ItemWrapper next = iterator.next();

            if (next.getStack().getUnlocalizedName().equals(unlocalizedName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsEntry(ItemStack stack) {
        ItemWrapper wrapper = new ItemWrapper(stack);
        for (Iterator<ItemWrapper> iterator = entries.keySet().iterator(); iterator.hasNext(); ) {
            ItemWrapper next = iterator.next();

            if (wrapper.equals(next))
                return true;
        }

        return false;
    }

    public static void init(){
        IVoxBoxDictionary dictionary = VoxBoxHandler.voxBoxLibrary.get();
        dictionary.addEntry(new ItemStack(ModItem.voxBox), VoxBoxEntryHelper.createEntry(new ItemStack(ModItem.voxBox), "item.steamnsteel:voxBox.voxboxEntry", "", TheMod.MOD_ID));
    }
}
