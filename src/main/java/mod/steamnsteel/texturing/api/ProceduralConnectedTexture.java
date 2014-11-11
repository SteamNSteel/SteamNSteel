package mod.steamnsteel.texturing.api;

import mod.steamnsteel.texturing.api.iconregistry.ITextureConditionSet;
import mod.steamnsteel.texturing.api.iconregistry.ProceduralTextureRegistry;
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
        TextureContext context = new TextureContext(blockAccess, worldBlockCoord, side);

        long blockProperties = getTexturePropertiesForSide(context);

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
     * @param context The Texture Context
     * @return the feature mask
     */
    private long getTexturePropertiesForSide(TextureContext context)
    {
        long blockProperties = 0;
        ForgeDirection orientation = context.getOrientation();
        if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN || isBlockPartOfWallAndUnobstructed(context, TextureDirection.BACKWARDS))
        {
            return DEFAULT;
        }

        boolean leftIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(context, TextureDirection.LEFT);
        boolean rightIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(context, TextureDirection.RIGHT);
        boolean aboveIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(context, TextureDirection.ABOVE);
        boolean belowIsRuinWallAndNotObscured = isBlockPartOfWallAndUnobstructed(context, TextureDirection.BELOW);

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

        blockProperties = featureRegistry.getFeatureBits(context, blockProperties);

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
        TextureContext context = new TextureContext(blockAccess, worldBlockCoord, side);

        long blockProperties = getTexturePropertiesForSide(context);
        return featureRegistry.describeSide(blockProperties);

    }

    /**
     * Checks if a block offset from the context is part of the wall and is not obstructed by an opaque block
     *
     * @param context   The Texture Context
     * @param direction The directions to apply
     * @return true if the block is part of the wall and can be seen.
     */
    public final boolean isBlockPartOfWallAndUnobstructed(TextureContext context, TextureDirection... direction)
    {
        WorldBlockCoord coord = getOffsetCoordinate(context, direction);

        if (!isCompatibleBlock(context, coord.getBlock(context.getBlockAccess())))
        {
            return false;
        }

        final Block obscuringBlock = coord.offset(context.getBackwardDirection()).getBlock(context.getBlockAccess());

        return !canBlockObscure(context, obscuringBlock);

    }

    /**
     * Override this method to change the behaviour that only compatible blocks count as obscuring a texture.
     *
     * @param context        The Texture Context
     * @param obscuringBlock the block that is beinc checked
     * @return true if the block is obscuring a texture.
     */
    protected boolean canBlockObscure(TextureContext context, Block obscuringBlock)
    {
        return isCompatibleBlock(context, obscuringBlock);
    }

    /**
     * Takes a series of TextureDirections and applies them to the context to return a world coordinate.
     *
     * @param context   The Texture Context
     * @param direction The directions to apply
     * @return the world space coordinates after transformation
     */
    private WorldBlockCoord getOffsetCoordinate(TextureContext context, TextureDirection[] direction)
    {
        WorldBlockCoord coord = context.getWorldBlockCoord();
        for (TextureDirection textureDirection : direction)
        {
            coord = coord.offset(context.getForgeDirection(textureDirection));
        }
        return coord;
    }

    /**
     * Detects if a given block can be considered for texturing.
     *
     * @param context the context that the search is being taken under
     * @param block   the block to check compatibility with
     * @return true if the block is considered compatible with this texture.
     */
    protected abstract boolean isCompatibleBlock(TextureContext context, Block block);

    public boolean isFeatureAtCoordCompatibleWith(TextureContext context, Layer layer, IProceduralWallFeature feature, boolean checkValidity, TextureDirection... direction)
    {
        WorldBlockCoord coord = getOffsetCoordinate(context, direction);
        final IProceduralWallFeature featureAtCoord = featureRegistry.getFeatureAt(coord, layer);
        boolean result = featureAtCoord != null && featureAtCoord.getFeatureId() == feature.getFeatureId();
        if (checkValidity && result)
        {
            result = featureAtCoord.isFeatureValid(context.forLocation(coord));
        }
        return result;
    }

    /**
     * gets a wall feature present on a given layer at a given location, or null if no feature found.
     *
     * @param context   The Texture Context
     * @param layer     The layer to check
     * @param direction The offsets to the block that is being checked
     * @return The potential feature at a point.
     */
    public IProceduralWallFeature getValidFeature(TextureContext context, Layer layer, TextureDirection... direction)
    {
        WorldBlockCoord coord = getOffsetCoordinate(context, direction);
        IProceduralWallFeature desiredFeature = featureRegistry.getFeatureAt(coord, layer);
        if (desiredFeature == null)
        {
            return null;
        }

        if (desiredFeature.isFeatureValid(context))
        {
            return desiredFeature;
        }
        return null;
    }

    /**
     * Checks that a feature can be used at a given location.
     *
     * @param context       The texture context
     * @param layer         The layer of the feature
     * @param wallFeature   The feature to check
     * @param checkValidity when true, this call will also check that the feature is valid
     * @param direction     A series of offsets to apply
     * @return true if the feature is valid at the requested location.
     */
    public boolean isFeatureAtCoordVisibleAndCompatible(TextureContext context, Layer layer, IProceduralWallFeature wallFeature, boolean checkValidity, TextureDirection... direction)
    {
        return isBlockPartOfWallAndUnobstructed(context, direction) && isFeatureAtCoordCompatibleWith(context, layer, wallFeature, checkValidity, direction);
    }
}
