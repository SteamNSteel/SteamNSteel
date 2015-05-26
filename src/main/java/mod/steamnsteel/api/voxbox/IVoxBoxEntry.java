package mod.steamnsteel.api.voxbox;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

public interface IVoxBoxEntry {

    public ItemStack getItem();
    public String getDialogueKey(); //Localization key
    public String getAudioLocation(); //Audio localization key
}
