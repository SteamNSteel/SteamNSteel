package mod.steamnsteel.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import mod.steamnsteel.Reference;
import mod.steamnsteel.Reference.BlockProperties;
import mod.steamnsteel.library.BlockLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;

public class PillarMultiModel extends SNSMultiModel {

    private static final ImmutableMap<String, String> flipData = ImmutableMap.of("flip-v", String.valueOf(true));

    private static final ImmutableMap<EnumFacing, IModelState> transformations = ImmutableMap.of(
            EnumFacing.NORTH, new TRSRTransformation(ModelRotation.X0_Y0),
            EnumFacing.SOUTH, new TRSRTransformation(ModelRotation.X0_Y180),
            EnumFacing.EAST, new TRSRTransformation(ModelRotation.X0_Y90),
            EnumFacing.WEST, new TRSRTransformation(ModelRotation.X0_Y270)
    );

    @SuppressWarnings("deprecation")
    @Override
    public void loadModel(ModelBakeEvent event) {
        final IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

        for (final IBlockState state : BlockLibrary.remnantRuinPillar.getBlockState().getValidStates()) {

            final ModelResourceLocation modelLocation = stateMapper.getModelResourceLocation(state);

            final IBakedModel bakedModel = modelRegistry.getObject(modelLocation);
            if (bakedModel instanceof OBJBakedModel)
            {
                final IBakedModel updatedModel = getBakedModel(state, modelLocation, (OBJBakedModel) bakedModel);
                modelRegistry.putObject(
                        modelLocation,
                        updatedModel
                );
            }
        }
    }

    private IBakedModel getBakedModel(IBlockState state, ModelResourceLocation modelLocation, OBJBakedModel bakedModel)
    {
        final Builder<String, Pair<IModel, IModelState>> builder = new Builder<>();

        final ArrayList<String> visibleGroups = Lists.newArrayList("SSRemnantPillar");
        if (state.getValue(BlockProperties.CONNECT_TOP))
        {
            visibleGroups.add("SSRemnantPillar_CapTop");
        }
        if (state.getValue(BlockProperties.CONNECT_BOTTOM))
        {
            visibleGroups.add("SSRemnantPillar_CapBottom");
        }

        final IModel multiModel = new MultiModel(
                modelLocation,
                bakedModel.getModel(),
                bakedModel.getState(),
                builder.build()
        );

        return multiModel.bake(
                new OBJState(
                        visibleGroups,
                        true,
                        new TRSRTransformation(TRSRTransformation.getMatrix(state.getValue(BlockProperties.HORIZONTAL_FACING)))
                ),
                DefaultVertexFormats.ITEM,
                textureGetter
        );
    }

}
