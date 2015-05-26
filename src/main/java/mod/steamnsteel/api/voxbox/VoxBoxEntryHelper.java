package mod.steamnsteel.api.voxbox;

import net.minecraft.item.ItemStack;

public class VoxBoxEntryHelper {

    /**
     * Creates an entry for use in the VoxBox api
     * @param stack The {@link net.minecraft.item.ItemStack} to associate the entry with
     * @param dialogueKey Unlocalized name for the text to be displayed. Format is "line1<br>line2<br>..." where <br> is a line break.
     * @param audioFileName Unlocalized name for the audio file to be played, can be null or empty if undesired
     * @param modID Mod ID for looking up the unlocalized names
     * @return {@link mod.steamnsteel.api.voxbox.IVoxBoxEntry} created with passed args
     */
    public static IVoxBoxEntry createEntry(final ItemStack stack, final String dialogueKey, final String audioFileName, final String modID){
        IVoxBoxEntry entry = new IVoxBoxEntry() {

            @Override
            public ItemStack getItem() {
                return stack;
            }

            @Override
            public String getDialogueKey() {
                return dialogueKey;
            }

            @Override
            public String getAudioLocation() {
                if(audioFileName != null && modID != null)
                    return modID + ":" + audioFileName;
                else
                    return null;
            }
        };

        return entry;
    }
}
