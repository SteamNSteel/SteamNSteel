package mod.steamnsteel.client.codemodel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.machine.PipeBlock.PipeStates;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Vector3f;
import java.lang.reflect.Field;

import static mod.steamnsteel.block.machine.PipeBlock.PipeStates.*;

/*
* Generates the pipe caps on the models. Saves having to copy and paste a ton of json
*/
@SideOnly(Side.CLIENT)
public class PipeModel extends BaseCodeModel
{
    private final ImmutableSet<PipeStates> capFlip = ImmutableSet.copyOf(new PipeStates[]{ds, de, sw, dw, uw});
    private static final ImmutableMap<String, String> flipData = ImmutableMap.of("flip-v", String.valueOf(true));

    private static final ResourceLocation capLocation = new ResourceLocation(Reference.MOD_ID, "block/pipes/SSPipesCap.obj");

    private static final String nObjModel = "model";
    private Field fObjModel = null;

    final ImmutableMap<EnumFacing, IModelState> transformations;

    public PipeModel()
    {
        try
        {
            fObjModel = OBJBakedModel.class.getDeclaredField(nObjModel);
            fObjModel.setAccessible(true);
        } catch (NoSuchFieldException e)
        {
            Logger.severe(String.format("missing field '%s' in OBJBakedModel. Pipe caps will not be generated", nObjModel));
        }

        //Populate model transformation map
        final float sz = 0.185f;
        final Builder<EnumFacing, IModelState> builder = ImmutableMap.builder();

        builder.put(EnumFacing.DOWN, TRSRTransformation
                .blockCornerToCenter(new TRSRTransformation(ModelRotation.X180_Y0))
                .compose(new TRSRTransformation(new Vector3f(0.0f, -sz, 0.0f), null, null, null))
        );
        builder.put(EnumFacing.UP, TRSRTransformation
                .blockCornerToCenter(new TRSRTransformation(ModelRotation.X0_Y0))
                .compose(new TRSRTransformation(new Vector3f(0.0f, 1.0f - sz, 0.0f), null, null, null))
        );
        builder.put(EnumFacing.NORTH, TRSRTransformation
                .blockCornerToCenter(new TRSRTransformation(ModelRotation.X180_Y0))
                .compose(new TRSRTransformation(new Vector3f(0.0f, -sz, 0.0f), null, null, null))
        );
        builder.put(EnumFacing.SOUTH, TRSRTransformation
                .blockCornerToCenter(new TRSRTransformation(ModelRotation.X0_Y0))
                .compose(new TRSRTransformation(new Vector3f(0.0f, 1.0f - sz, 0.0f), null, null, null))
        );
        builder.put(EnumFacing.EAST, TRSRTransformation
                .blockCornerToCenter(new TRSRTransformation(ModelRotation.X180_Y0))
                .compose(new TRSRTransformation(new Vector3f(0.0f, -sz, 0.0f), null, null, null))
        );
        builder.put(EnumFacing.WEST, TRSRTransformation
                .blockCornerToCenter(new TRSRTransformation(ModelRotation.X0_Y0))
                .compose(new TRSRTransformation(new Vector3f(0.0f, 1.0f - sz, 0.0f), null, null, null))
        );

        transformations = builder.build();
    }

    @Override
    public void loadModel(ModelBakeEvent event)
    {
        if (fObjModel == null) return;

        for (final IBlockState si : ModBlock.pipe.getBlockState().getValidStates())
        {
            final boolean capA = si.getValue(PipeBlock.END_A_CAP);
            final boolean capB = si.getValue(PipeBlock.END_B_CAP);

            if (capA || capB)
            {
                final Builder<String, Pair<IModel, IModelState>> builder = new Builder<>();
                final ModelResourceLocation mrl = sdm.getModelResourceLocation(si);
                final PipeStates pipeState = si.getValue(PipeBlock.PIPE_STATE);

                final IModel modelCap = procsessModel(loadModel(capLocation), flipData);
                final OBJBakedModel bakedModel = (OBJBakedModel) event.getModelRegistry().getObject(mrl);

                IModel model = null;
                try
                {
                    model = (IModel) fObjModel.get(bakedModel);
                } catch (IllegalAccessException e) {}

                if (capA) builder.put("capa", Pair.of(modelCap, getTransformForCap(getCorrectCapA(pipeState))));
                if (capB) builder.put("capb", Pair.of(modelCap, getTransformForCap(getCorrectCapB(pipeState))));


                event.getModelRegistry().putObject(mrl,
                        new MultiModel(mrl, model, bakedModel.getState(), builder.build())
                                .bake(bakedModel.getState(), DefaultVertexFormats.ITEM, textureGetter)
                );
            }
        }
    }

    private static IModel procsessModel(IModel model, ImmutableMap<String, String> data)
    {
        if (model instanceof OBJModel)
        {
            return ((OBJModel) model).process(data);
        }

        return model;
    }

    private EnumFacing getCorrectCapA(PipeStates states)
    {
        return capFlip.contains(states) ?
                states.getEndB() :
                states.getEndA();
    }

    private EnumFacing getCorrectCapB(PipeStates states)
    {
        return capFlip.contains(states) ?
                states.getEndA() :
                states.getEndB();
    }


    private IModelState getTransformForCap(EnumFacing e)
    {
        final IModelState ims = transformations.get(e);

        return ims == null ? TRSRTransformation.identity() : ims;
    }
}

