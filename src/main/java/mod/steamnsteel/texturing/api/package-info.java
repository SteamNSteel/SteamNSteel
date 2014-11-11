package mod.steamnsteel.texturing.api;

/**
 *
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
 */