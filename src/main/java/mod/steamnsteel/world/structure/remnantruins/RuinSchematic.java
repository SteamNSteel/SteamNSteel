package mod.steamnsteel.world.structure.remnantruins;

import mod.steamnsteel.world.SchematicLoader;
import net.minecraft.util.ResourceLocation;

public class RuinSchematic
{
    public final SchematicLoader.ISchematicWorldMetadata schematicMetadata;
    public final ResourceLocation resource;
    public RuinSchematic(ResourceLocation resource, SchematicLoader.ISchematicWorldMetadata schematicMetadata) {
        this.resource = resource;
        this.schematicMetadata = schematicMetadata;
    }
}
