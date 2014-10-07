package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.client.renderer.model.PipeModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class PipeTESR extends SteamNSteelTESR
{
    public static ResourceLocation TEXTURE = getResourceLocation(PipeBlock.NAME);
    private final PipeModel model = new PipeModel();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {

    }
}
