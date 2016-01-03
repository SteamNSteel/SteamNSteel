package mod.steamnsteel.texturing.api;

import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart;
import mod.steamnsteel.texturing.api.traitspriteregistry.TraitSpriteRegistry;
import mod.steamnsteel.texturing.api.traitregistry.FeatureRegistry;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public abstract class ProceduralConnectedTexture
{
    protected final int DEFAULT = 0;
    /**
     * The featureId for the top of a wall
     */
    public static long TOP;
    /**
     * The featureId for the bottom of a wall
     */
    public static long BOTTOM;
    /**
     * The featureId for the left side of a wall
     */
    public static long LEFT;
    /**
     * The featureId for the right side of a wall
     */
    public static long RIGHT;

    /**
     * The featureId for the top edge of a feature
     */
    public static long FEATURE_EDGE_TOP;
    /**
     * The featureId for the bottom edge of a feature
     */
    public static long FEATURE_EDGE_BOTTOM;
    /**
     * The featureId for the left edge of a feature
     */
    public static long FEATURE_EDGE_LEFT;
    /**
     * The featureId for the right edge of a feature
     */
    public static long FEATURE_EDGE_RIGHT;

    private FeatureRegistry featureRegistry;

    private TraitSpriteRegistry textures;

    protected ProceduralConnectedTexture()
    {
        registerFeatures();
    }

    public void registerSprites(TextureMap textureMap)
    {
        registerFeatures();

        textures = new TraitSpriteRegistry(textureMap);
        registerSprites(textures);
    }

    private void registerFeatures()
    {
        featureRegistry = new FeatureRegistry();

        registerInternalFeatureProperties(featureRegistry);
        registerFeatures(featureRegistry);
    }

    /**
     * Registers some key features used by most textures.
     *
     * @param features the fluent interface to register features with.
     */
    private void registerInternalFeatureProperties(IFeatureRegistry features)
    {
        TOP = features.registerTrait("T");
        LEFT = features.registerTrait("L");
        BOTTOM = features.registerTrait("B");
        RIGHT = features.registerTrait("R");

        FEATURE_EDGE_TOP = features.registerTrait("FE_T");
        FEATURE_EDGE_LEFT = features.registerTrait("FE_L");
        FEATURE_EDGE_BOTTOM = features.registerTrait("FE_B");
        FEATURE_EDGE_RIGHT = features.registerTrait("FE_R");
    }

    /**
     * Called at an appropriate time to register the features used by texture
     *
     * @param features a fluent interface to register features.
     */
    protected abstract void registerFeatures(IFeatureRegistry features);

    /**
     * This method is called at the time it is appropriate to register icons that can be assigned to the wall.
     *
     * @param textures A fluent interface to register textures.
     */
    protected abstract void registerSprites(ISpriteDefinitionStart textures);

    /**
     * Calculates the IIcon to use to represent the side of a block
     *
     * @param blockAccess     a method by which blocks can be accessed (Typically World)
     * @param worldBlockCoord the world block coordinate to describe
     * @param side            the side of the block to describe
     * @return the icon to use when rendering the side of the block.
     */
    public TextureAtlasSprite getSpriteForSide(IBlockAccess blockAccess, BlockPos worldBlockCoord, EnumFacing side)
    {
        SpriteRequest request = new SpriteRequest(blockAccess, worldBlockCoord, side);

        long blockProperties = getTraitSetForSide(request);

        TextureAtlasSprite sprite = textures.getTextureFor(side, blockProperties);

        if (sprite == null)
        {
            String blockPropertiesDescription = featureRegistry.describeTraitSet(blockProperties);
            Logger.warning("Unknown texture: %d (%s) - %s @ (%s) - %s", blockProperties, Long.toBinaryString(blockProperties), blockPropertiesDescription, worldBlockCoord, side);
        }
        return sprite;
    }

    protected TextureAtlasSprite getSpriteForTraitSet(EnumFacing side, long traitSet) {
        return textures.getTextureFor(side, traitSet);
    }

    /**
     * Calculates an id that correlates to a set of features
     *
     * @param request The Icon Request
     * @return the feature mask
     */
    private long getTraitSetForSide(SpriteRequest request)
    {
        long initialTraits = 0;
        if (isBlockPartOfWallAndUnobstructed(request, TextureDirection.BACKWARDS))
        {
            return DEFAULT;
        }

        boolean leftIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.LEFT);
        boolean rightIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.RIGHT);
        boolean aboveIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.ABOVE);
        boolean belowIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.BELOW);

        if (!aboveIsRuinWallAndNotObscured)
        {
            initialTraits |= TOP;
        }
        if (!belowIsRuinWallAndNotObscured)
        {
            initialTraits |= BOTTOM;
        }
        if (!leftIsRuinWallAndNotObscured)
        {
            initialTraits |= LEFT;
        }
        if (!rightIsRuinWallAndNotObscured)
        {
            initialTraits |= RIGHT;
        }

        long traitSet = featureRegistry.getTraitSet(request, initialTraits);

        return traitSet;
    }

    /**
     * Used to debug the texturing process, converts a set of features to a text string describing the conditions
     *
     * @param blockAccess     a method by which blocks can be accessed (Typically World)
     * @param worldBlockCoord the world block coordinate to describe
     * @param side            the side of the block to describe
     * @return a string that represents the various conditions on the wall.
     */
    public final String describeTextureAt(IBlockAccess blockAccess, BlockPos worldBlockCoord, EnumFacing side)
    {
        SpriteRequest request = new SpriteRequest(blockAccess, worldBlockCoord, side);

        long blockProperties = getTraitSetForSide(request);
        return featureRegistry.describeTraitSet(blockProperties);
    }

    /**
     * Override this method to change the behaviour that only compatible blocks count as obscuring a texture.
     *
     * @param request        The Icon Request
     * @param obscuringBlock the block that is beinc checked
     * @return true if the block is obscuring a texture.
     */
    protected boolean canBlockObscure(SpriteRequest request, IBlockState obscuringBlock)
    {
        return isCompatibleBlock(request, obscuringBlock);
    }

    /**
     * Takes a series of TextureDirections and applies them to the request to return a world coordinate.
     *
     * @param request   The Icon Request
     * @param direction The directions to apply
     * @return the world space coordinates after transformation
     */
    private BlockPos getOffsetCoordinate(SpriteRequest request, TextureDirection[] direction)
    {
        BlockPos coord = request.getWorldBlockCoord();
        for (TextureDirection textureDirection : direction)
        {
            coord = coord.offset(request.getEnumFacing(textureDirection));
        }
        return coord;
    }

    /**
     * Detects if a given block can be considered for texturing.
     *
     * @param request the request that the search is being taken under
     * @param block   the block to check compatibility with
     * @return true if the block is considered compatible with this texture.
     */
    protected abstract boolean isCompatibleBlock(SpriteRequest request, IBlockState block);

    /**
     * gets a wall feature present on a given layer at a given location, or null if no feature found.
     *
     * @param request   The Icon Request
     * @param layer     The layer to check
     * @param direction The offsets to the block that is being checked
     * @return The potential feature at a point.
     */
    public IProceduralWallFeature getValidFeature(SpriteRequest request, Layer layer, TextureDirection... direction)
    {
        BlockPos coord = getOffsetCoordinate(request, direction);
        IProceduralWallFeature desiredFeature = featureRegistry.getFeatureAt(coord, layer);
        if (desiredFeature == null)
        {
            return null;
        }

        if (desiredFeature.isFeatureValid(request))
        {
            return desiredFeature;
        }
        return null;
    }

    /**
     * Checks that a feature can be used at a given location.
     *
     * @param request       The Icon Request
     * @param layer         The layer of the feature
     * @param wallFeature   The feature to check
     * @param checkValidity when true, this call will also check that the feature is valid
     * @param offsets     A series of offsets to apply
     * @return true if the feature is valid at the requested location.
     */
    public boolean isFeatureAtOffsetPartOfWallUnobstructedAndOfType(SpriteRequest request, Layer layer, IProceduralWallFeature wallFeature, boolean checkValidity, TextureDirection... offsets)
    {
        return isBlockPartOfWallAndUnobstructed(request, offsets) && isFeatureAtOffsetOfType(request, layer, wallFeature, checkValidity, offsets);
    }

    public boolean isFeatureAtOffsetOfType(SpriteRequest request, Layer layer, IProceduralWallFeature feature, boolean checkValidity, TextureDirection... offsets)
    {
        BlockPos coord = getOffsetCoordinate(request, offsets);
        final IProceduralWallFeature featureAtCoord = featureRegistry.getFeatureAt(coord, layer);
        boolean result = featureAtCoord != null && featureAtCoord.getFeatureTraitId() == feature.getFeatureTraitId();
        if (checkValidity && result)
        {
            result = featureAtCoord.isFeatureValid(request.forLocation(coord));
        }
        return result;
    }

    /**
     * Checks if a block offset from the request is part of the wall and is not obstructed by an opaque block
     *
     * @param request   The Icon Request
     * @param direction The directions to apply
     * @return true if the block is part of the wall and can be seen.
     */
    public final boolean isBlockPartOfWallAndUnobstructed(SpriteRequest request, TextureDirection... direction)
    {
        BlockPos coord = getOffsetCoordinate(request, direction);

        if (!isCompatibleBlock(request, request.getBlockAccess().getBlockState(coord)))
        {
            return false;
        }

        final IBlockState obscuringBlock = request.getBlockAccess().getBlockState(coord.offset(request.getBackwardDirection()));

        return !canBlockObscure(request, obscuringBlock);
    }

    public abstract TextureAtlasSprite getDefaultTextureForSide(EnumFacing side);
    public abstract TextureAtlasSprite getParticleTexture();
}
