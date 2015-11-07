package mod.steamnsteel.texturing.api;

/**
 * Trait:
 *      A single bit that describes the condition of the block
 * TraitSet:
 *      A long of 'Or'd traits that make up an icon selection
 * Icon:
 *      The texture applied to the side of a block.
 * Feature:
 *      An item of interest applied "on top" of a texture.
 * IconRequest: (TextureContext)
 *      A request object that represents the location of the block, the side, and the
 *          TextureDirections relative to that side. It also tracks the object by which
 *          the world around can be examined.
 *
 * <h1>Procedural Texturing</h1>
 * Minecraft uses getIcon(IBlockAccess blockAccess, BlockPos pos int side) to determine what texture (IIcon) to
 * apply to each side of a block. The results are cached. This gives us the opportunity to do some somewhat intensive
 * calculations to dynamically build a set of textures across a wall, following the same principals as Minecraft's
 * procedural world generation. By basing randomness on the x, y and z we can generate pseudo-random features on the
 * side of a block, participating with the features of the blocks around it.
 *
 * Procedural Connected Textures are defined by a set of features, layers, icons and traits and how they relate with
 * one another.
 *
 * A feature is anything that constitutes a visible thing you can see. An example might be a vent, screen, a metal
 * plate or perhaps a band across the top or the bottom of the wall.
 *
 * It's possible that many features may be layered on top of one another. It may be possible to have a plate and a band
 * at the top. Perhaps you can have a vent on top of a plate (as long as all sections of the vent are on a plate)
 *
 * Traits are metadata that help you decide what features are available at a point. Examples of a trait would be the
 * left hand side of a plate, or a trait that represents an alternative version of a block.
 *
 * Traits are very important in deciding what you are going to display. A block that has the TOP trait, PLATE_LEFT,
 * PLATE_RIGHT and PLATE_BOTTOM may tell you that you have a plate that is a single block wide at the top of the wall.
 * You can use this to determine that this might not be a valid place to have a plate, if your plates are at minimum 2x2
 * in size.
 *
 * <h1>How it works</h1>
 * The process starts with the Block.registerBlockIcons(IIconRegister iconRegister) method. When this method is called,
 * an instance of your ProceduralConnectedTexture should be instantiated, and you should call .registerIcons(iconRegister)
 * on it to configure the texture manager. This means that it will participate well with texture packs and aid in
 * debugging any issues.
 *
 * When minecraft calls Block.getIcon(IBlockAccess blockAccess, BlockPos pos int side), the call should be
 * proxied to your texture using texture.getIconForSide(blockAccess, WorldBlockCoord.of(pos), side).
 *
 * Inside of that method, we start by calculating some initial traits, TOP, BOTTOM, LEFT and RIGHT which is done by
 * checking the blocks in the appropriate directions. The traits are set if the block is not compatible with the current
 * wall (as returned by ProceduralConnectedTexture.isCompatibleBlock, or if the side is being obscured by a block that
 * matches the ProceduralConnectedTexture.canBlockObscure() method.
 *
 * Once we have the initial traits, we start examining the layers in the order they were defined. For each layer, we
 * check to see if a feature is available by retrieving a set of FeatureInstances using
 * IProcedualWallFeature.getFeatureInstanceFor(), which should scatter the feature around a 16xNx16 area. FeatureInstances
 * have a location, width, height and depth, and so can range anywhere from 1x1x1 to 16xNx16.
 *
 * If your layer has randomization enabled, you should generate within a 16x16x16 area, otherwise you can use 16x256x16.
 * Randomization works by adding an offset in the x and z directions dependant on the y coordinate, and then wrapping the
 * resulting block around when it goes outside of the chunk.
 *
 * Generating and iterating over the list of features is potentially one of the slowest parts of procedural walls, it is
 * recommended to use the smaller area with randomization if possible Basically, the less FeatureInstances, the less lag
 * will be witnessed by end users. In practice, we cache as much as we can to avoid recalculating these instances.
 *
 * The bands used by the PlotoniumRuinWalls exist as a single 16x256x16 that theoretically exists everywhere given the
 * right traits.
 *
 * Once we have determined the presence of a feature on a layer, we check if it is valid. This is usually feature specific
 * and varies a lot in complexity. A feature might simply be valid if it is part of a wall, and is visible. Otherwise
 * it might be dependant on a roof being directly above the block, or a more complicated example would be a feature
 * that is at least 1x3 blocks wide, which would need to check blocks 2 to the left and 2 to the right to determine if
 * it is valid or not.
 *
 * If feature is valid, we then check against the features that are being applied on other layers. Some features may
 * conflict with one another. If you don't have a texture that has a monitor and a vent on it, one of them would take
 * preference. Conflicts are calculated using IProceduralWallFeature.getBehaviourAgainst, which returns a Behaviour that
 * determines whether the feature can coexist, replaces an existing feature, or is not allowed to exist.
 *
 * If a feature is allowed to exist, we retrieve the list of traits that apply to that feature. This is where features
 * will determine if any additional traits should be applied, such as detecting the edge of a feature or giving a random
 * chance of an alternate version. We also collect a set of traits that are incompatible with the feature that may have
 * been added by other layers. This is retrieved using IProceduralWallFeature.getIncompatibleTraits()
 *
 * Finally, once this has been done for all of the layers, we 'Or' the featureTraitIds and the traits, and reset any
 * incompatible traits to make up the Trait Set
 *
 * With the Trait Set finally resolved, we can attempt to resolve an Icon for the Trait Set and return that back to
 * Minecraft to be the side of the block.
 *
 *
 *
 * <h1>Creating a Texture</h1>
 * Textures start by implementing a ProceduralWallTexture and implementing the following methods:
 *
 * <h2>boolean isCompatibleBlock(IconRequest request, Block block)</h2>
 * this method should return true for every type of block you wish to participate in the connected texturing.
 *
 * <h2>void registerFeatures(IFeatureRegistry features)</h2>
 * Use the IFeatureRegistry to register Layers. Layers can be randomized, or not, the definition of which will be
 * described later. The order that you define layers in determines the order that Features are applied in.
 * Once you have a set of layers, you can define Features to go on those layers, and then register them against the
 * IFeatureRegistry.
 * Finally, register any additional traits you wish to use.
 *
 * <h2>void registerIcons(IIconDefinitionStart textures)</h2>
 * This is where you assign TraitSets to Icons. TraitSets are defined as a series of 'Or'd traitIds together. A simple
 * assignment of a texture to a TraitSet looks like this:
 *      final long plateTraitId = featurePlate.getFeatureTraitId();
 *      textures.useIconNamed("remnantRuinWall")
 *              .forTraitSet(DEFAULT)
 *              .andTraitSet(pipesTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM)
 * This particular example uses the remnantRuinWall texture for the default texture, as well as a feature with the
 * traits FEATURE_EDGE_TOP and FEATURE_EDGE_BOTTOM.
 *
 * <h1>Creating a feature</h1>
 * To create a Feature, extend ProceduralWallFeatureBase, and implement the following methods:
 *
 * <h2>Collection&lt;FeatureInstance&rt; getFeaturesIn(ChunkCoord chunkCoord)</h2>
 * Based on a given chunk, this method should return a set of FeatureInstances that occupy space in a chunk. Some features
 * may occupy the entire chunk, others may randomly scatter themselves around the chunk in various clumps. If a feature
 * is going to be part of a layer that has randomization enabled, you should limit your randomization in the Y axis to
 * 16 as well.
 *
 * <h2>long getTraits(IconRequest request)</h2>
 * This method should return traits 'Or'd together that describe the feature at that location. If other traits have
 * been specified, such as the edges of a plate, then if a block has the plate feature, but the block to the left does
 * not, getTraits might return a trait that represents an edge on the left hand side of the plate.
 *
 * <h2>boolean isFeatureValid(IconRequest request)</h2>
 * This method is called to verify if a feature can be used at a location. It is possible that a feature should exist
 * at a point, but the blocks surrounding it may prevent it from being usable. For example, a 2x2 texture on a wall that
 * is 1x2.
 *
 * <h2>long getIncompatibleTraits()</h2>
 * If there are any traits that are incompatible with this feature, they can be 'Or'd together and returned from this
 * method, which will force them turned off when this feature is applied.
 *
 * <h1>Methods useful in determine feature validity and traits</h1>
 * While ProceduralWallFeatureBase does not use it, if you pass an instance of ProceduralConnectedTexture to your
 * feature the following methods will become available to aid in writing your feature. Many of these methods take in a
 * parameter list of TextureDirections, which allows you to apply multiple transformations against the request location
 * when calculating offsets.
 *
 * <h2>isFeatureAtOffsetOfType(IconRequest request, Layer layer, IProceduralWallFeature wallFeature, boolean checkValidity, TextureDirection... direction)</h2>
 * This method will verify that the block at an offset from the request location has a feature available on the layer specified of the specified type.
 * If checkValidity is true, then the feature will also be checked to see if it is acutally valid at that offset.
 *
 * <h2>boolean isBlockPartOfWallAndUnobstructed(IconRequest request, TextureDirection... direction)</h2>
 * This method will verify that the block at an offset from the request location is a block that is compatible with the
 * wall, and is not being obstructed by a block does not pass ProceduralConnectedTexture.canBlockObscure()
 *
 * <h2>isFeatureAtOffsetPartOfWallUnobstructedAndOfType(IconRequest request, Layer layer, IProceduralWallFeature wallFeature, boolean checkValidity, TextureDirection... offsets)</h2>
 * This method combines the above two methods into a single call.
 *
 */