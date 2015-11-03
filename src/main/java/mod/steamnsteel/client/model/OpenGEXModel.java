package mod.steamnsteel.client.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import mod.steamnsteel.client.model.opengex.OpenGEXNode;
import mod.steamnsteel.client.model.opengex.ogex.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class OpenGEXModel implements IModelCustomData, IRetexturableModel {
    private final ImmutableMap<String, ResourceLocation> textureMap;
    OgexTexture defaultTexture = new OgexTexture();
    //FIXME: Is there somewhere more appropriate for this?
    public static OgexTexture white;

    static {
        white = new OgexTexture();
        white.setTexture("builtin/white");
        final OgexMatrixTransform ogexMatrixTransform = new OgexMatrixTransform();
        ogexMatrixTransform.setMatrix(OgexMatrixTransform.identity());
        white.addTransform(ogexMatrixTransform);
    }

    private final ResourceLocation location;
    private final OpenGEXNode node;

    public OpenGEXModel(ResourceLocation location, OgexScene scene) {
        this(location, scene, buildTextures(scene.getMaterials()));
        defaultTexture.setTexture("missingno");
    }

    public OpenGEXModel(ResourceLocation location, OpenGEXNode node, ImmutableMap<String, ResourceLocation> textures) {
        this.location = location;
        this.node = node;
        this.textureMap = textures;
    }

    private static ImmutableMap<String, ResourceLocation> buildTextures(List<OgexMaterial> materials)
    {
        Builder<String, ResourceLocation> builder = ImmutableMap.builder();

        for(OgexMaterial t : materials)
        {
            String path = t.getTexture("diffuse").getTexture();
            String location = getLocation(path);
            builder.put(path, new ResourceLocation(location));
        }
        return builder.build();
    }

    private static String getLocation(String path)
    {
        if(path.endsWith(".png")) path = path.substring(0, path.length() - ".png".length());
        if(!path.startsWith("#")) path = "#" + path;
        return path;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        // as per B3d loader
        // In addition, OpenGEX does not support external file references at all.
        // no dependencies for in-file models
        // FIXME maybe add child meshes
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return Collections2.filter(getTextureMap().values(), new Predicate<ResourceLocation>() {
            public boolean apply(ResourceLocation loc) {
                return !loc.getResourcePath().startsWith("#");
            }
        });
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
        TextureAtlasSprite missing = bakedTextureGetter.apply(new ResourceLocation("missingno"));

        for (Entry<String, ResourceLocation> e : getTextureMap().entrySet()) {
            final String key = e.getKey();
            if(e.getValue().getResourcePath().startsWith("#"))
            {
                FMLLog.severe("unresolved texture '%s' for b3d model '%s'", e.getValue().getResourcePath(), location);
                builder.put(key, missing);
            }
            else
            {
                final TextureAtlasSprite apply = bakedTextureGetter.apply(e.getValue());
                builder.put(key, apply);
            }
        }
        builder.put("missingno", missing);
        return new OpenGEXFlexibleBakedModel(this, state, format, builder.build());

    }

    @Override
    public IModelState getDefaultState() {
        return new OpenGEXState(null, 1);
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        //Follow up with Fry how this is supposed to work.
        return null;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        Builder<String, ResourceLocation> builder = ImmutableMap.builder();
        for(Entry<String, ResourceLocation> e : this.getTextureMap().entrySet())
        {
            String path = e.getKey();
            String loc = getLocation(path);
            if(textures.containsKey(loc))
            {
                String newLoc = textures.get(loc);
                if(newLoc == null) newLoc = getLocation(path);
                builder.put(e.getKey(), new ResourceLocation(newLoc));
            }
            else
            {
                builder.put(e);
            }
        }
        return new OpenGEXModel(location, this.getNode(), builder.build());
    }

    public OpenGEXNode getNode() {
        return node;
    }

    public ResourceLocation getLocation()
    {
        return location;
    }


    public ImmutableMap<String, ResourceLocation> getTextureMap()
    {
        return textureMap;
    }
}
