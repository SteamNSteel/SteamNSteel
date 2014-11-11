package mod.steamnsteel.texturing.api;

import mod.steamnsteel.texturing.api.traiticonregistry.ITextureConditionSet;
import mod.steamnsteel.texturing.api.traiticonregistry.ProceduralTextureRegistry;
import mod.steamnsteel.texturing.api.traitregistry.FeatureRegistry;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

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

    private ProceduralTextureRegistry textures;

    protected ProceduralConnectedTexture()
    {
        registerFeatures();
    }

    public void registerIcons(IIconRegister iconRegister)
    {
        registerFeatures();

        textures = new ProceduralTextureRegistry(iconRegister);
        registerIcons(textures);
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
        TOP = features.registerFeatureProperty("T");
        LEFT = features.registerFeatureProperty("L");
        BOTTOM = features.registerFeatureProperty("B");
        RIGHT = features.registerFeatureProperty("R");

        FEATURE_EDGE_TOP = features.registerFeatureProperty("FE_T");
        FEATURE_EDGE_LEFT = features.registerFeatureProperty("FE_L");
        FEATURE_EDGE_BOTTOM = features.registerFeatureProperty("FE_B");
        FEATURE_EDGE_RIGHT = features.registerFeatureProperty("FE_R");
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
    protected abstract void registerIcons(ITextureConditionSet textures);

    /**
     * Calculates the IIcon to use to represent the side of a block
     *
     * @param blockAccess     a method by which blocks can be accessed (Typically World)
     * @param worldBlockCoord the world block coordinate to describe
     * @param side            the side of the block to describe
     * @return the icon to use when rendering the side of the block.
     */
    public IIcon getIconForSide(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        IconRequest request = new IconRequest(blockAccess, worldBlockCoord, side);

        long blockProperties = getTexturePropertiesForSide(request);

        IIcon icon = textures.getTextureFor(blockProperties);

        if (icon == null)
        {
            String blockPropertiesDescription = featureRegistry.describeSide(blockProperties);

            Logger.warning("Unknown texture: %d (%s) - %s @ (%s) - %d", blockProperties, Long.toBinaryString(blockProperties), blockPropertiesDescription, worldBlockCoord, side);
        }
        return icon;
    }

    /**
     * Calculates an id that correlates to a set of features
     *
     * @param request The Icon Request
     * @return the feature mask
     */
    private long getTexturePropertiesForSide(IconRequest request)
    {
        long blockProperties = 0;
        ForgeDirection orientation = request.getOrientation();
        if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN || isBlockPartOfWallAndUnobstructed(request, TextureDirection.BACKWARDS))
        {
            return DEFAULT;
        }

        boolean leftIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.LEFT);
        boolean rightIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.RIGHT);
        boolean aboveIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.ABOVE);
        boolean belowIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(request, TextureDirection.BELOW);

        if (!aboveIsRuinWallAndNotObscured)
        {
            blockProperties |= TOP;
        }
        if (!belowIsRuinWallAndNotObscured)
        {
            blockProperties |= BOTTOM;
        }
        if (!leftIsRuinWallAndNotObscured)
        {
            blockProperties |= LEFT;
        }
        if (!rightIsRuinWallAndNotObscured)
        {
            blockProperties |= RIGHT;
        }

        blockProperties = featureRegistry.getFeatureBits(request, blockProperties);

        return blockProperties;
    }

    /**
     * Used to debug the texturing process, converts a set of features to a text string describing the conditions
     *
     * @param blockAccess     a method by which blocks can be accessed (Typically World)
     * @param worldBlockCoord the world block coordinate to describe
     * @param side            the side of the block to describe
     * @return a string that represents the various conditions on the wall.
     */
    public final String describeTextureAt(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        IconRequest request = new IconRequest(blockAccess, worldBlockCoord, side);

        long blockProperties = getTexturePropertiesForSide(request);
        return featureRegistry.describeSide(blockProperties);

    }

    /**
     * Checks if a block offset from the request is part of the wall and is not obstructed by an opaque block
     *
     * @param request   The Icon Request
     * @param direction The directions to apply
     * @return true if the block is part of the wall and can be seen.
     */
    public final boolean isBlockPartOfWallAndUnobstructed(IconRequest request, TextureDirection... direction)
    {
        WorldBlockCoord coord = getOffsetCoordinate(request, direction);

        if (!isCompatibleBlock(request, coord.getBlock(request.getBlockAccess())))
        {
            return false;
        }

        final Block obscuringBlock = coord.offset(request.getBackwardDirection()).getBlock(request.getBlockAccess());

        return !canBlockObscure(request, obscuringBlock);

    }

    /**
     * Override this method to change the behaviour that only compatible blocks count as obscuring a texture.
     *
     * @param request        The Icon Request
     * @param obscuringBlock the block that is beinc checked
     * @return true if the block is obscuring a texture.
     */
    protected boolean canBlockObscure(IconRequest request, Block obscuringBlock)
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
    private WorldBlockCoord getOffsetCoordinate(IconRequest request, TextureDirection[] direction)
    {
        WorldBlockCoord coord = request.getWorldBlockCoord();
        for (TextureDirection textureDirection : direction)
        {
            coord = coord.offset(request.getForgeDirection(textureDirection));
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
    protected abstract boolean isCompatibleBlock(IconRequest request, Block block);

    public boolean isFeatureAtCoordCompatibleWith(IconRequest request, Layer layer, IProceduralWallFeature feature, boolean checkValidity, TextureDirection... direction)
    {
        WorldBlockCoord coord = getOffsetCoordinate(request, direction);
        final IProceduralWallFeature featureAtCoord = featureRegistry.getFeatureAt(coord, layer);
        boolean result = featureAtCoord != null && featureAtCoord.getFeatureId() == feature.getFeatureId();
        if (checkValidity && result)
        {
            result = featureAtCoord.isFeatureValid(request.forLocation(coord));
        }
        return result;
    }

    /**
     * gets a wall feature present on a given layer at a given location, or null if no feature found.
     *
     * @param request   The Icon Request
     * @param layer     The layer to check
     * @param direction The offsets to the block that is being checked
     * @return The potential feature at a point.
     */
    public IProceduralWallFeature getValidFeature(IconRequest request, Layer layer, TextureDirection... direction)
    {
        WorldBlockCoord coord = getOffsetCoordinate(request, direction);
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
     * @param direction     A series of offsets to apply
     * @return true if the feature is valid at the requested location.
     */
    public boolean isFeatureAtCoordVisibleAndCompatible(IconRequest request, Layer layer, IProceduralWallFeature wallFeature, boolean checkValidity, TextureDirection... direction)
    {
        return isBlockPartOfWallAndUnobstructed(request, direction) && isFeatureAtCoordCompatibleWith(request, layer, wallFeature, checkValidity, direction);
    }
}
