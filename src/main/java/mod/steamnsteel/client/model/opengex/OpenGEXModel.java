package mod.steamnsteel.client.model.opengex;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mod.steamnsteel.client.model.opengex.ogex.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.common.FMLLog;

import java.util.*;
import java.util.Map.Entry;

public class OpenGEXModel implements IModelCustomData, IRetexturableModel {
    //FIXME: Is there somewhere more appropriate for this?
    public static OgexTexture white;

    static {
        white = new OgexTexture();
        white.setTexture("builtin/white");
        final OgexMatrixTransform ogexMatrixTransform = new OgexMatrixTransform();
        ogexMatrixTransform.setMatrix(OgexMatrixTransform.identity());
        white.addTransform(ogexMatrixTransform);
    }

    private final OgexScene scene;
    private final ImmutableMap<String, ResourceLocation> textureMap;
    private final List<String> enabledNodes;
    private final OgexTexture defaultTexture = new OgexTexture();
    private final ResourceLocation location;
    private final OpenGEXNode node;
    private Integer[] nodeParents;
    private OgexNode[] allNodes;

    public OpenGEXModel(ResourceLocation location, OgexScene scene) {
        this(location, scene, scene, buildTextures(scene.getMaterials()), null);
        defaultTexture.setTexture("missingno");

    }

    public OpenGEXModel(ResourceLocation location, OpenGEXNode node, OgexScene scene, ImmutableMap<String, ResourceLocation> textures, List<String> enabledNodes) {
        this.location = location;
        this.node = node;
        this.scene = scene;
        this.textureMap = textures;
        this.enabledNodes = enabledNodes;
        arrangeForRendering();
    }

    private void arrangeForRendering()
    {
        //FIXME: Might not need multiple instances of the renderer. They should be stateless.
        Stack<Iterable<OgexNode>> queue = new Stack<Iterable<OgexNode>>();
        queue.push(scene);

        BiMap<OgexNode, Integer> nodeIndices = HashBiMap.create();
        Map<Integer, Integer> nodeParents = Maps.newHashMap();
        int currentIndex = 0;

        while (!queue.empty()) {
            Iterable<OgexNode> node = queue.pop();
            if (node instanceof OgexNode) {
                OgexNode ogexNode = (OgexNode) node;

                if (!nodeIndices.containsKey(node)) {
                    nodeIndices.put(ogexNode, currentIndex);
                    (ogexNode).setIndex(currentIndex);
                    currentIndex++;
                }
            }

            for (OgexNode childNode : node) {
                nodeIndices.put(childNode, currentIndex);
                nodeParents.put(currentIndex, nodeIndices.get(node));
                queue.push(childNode);
                childNode.setIndex(currentIndex);
                currentIndex++;
            }
        }

        allNodes = new OgexNode[nodeParents.size()];
        this.nodeParents = new Integer[nodeParents.size()];
        final BiMap<Integer, OgexNode> inverse = nodeIndices.inverse();
        for (Entry<Integer, Integer> family : nodeParents.entrySet()) {
            final Integer key = family.getKey();
            final OgexNode ogexNode = inverse.get(key);
            final Integer value = family.getValue();
            allNodes[key] = ogexNode;
            this.nodeParents[key] = value;
        }
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
        return new OpenGEXModelInstance(this, state, format, builder.build(), null);

    }

    @Override
    public IModelState getDefaultState() {
        return new OpenGEXState(null, 1);
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        List<String> enabledNodes = null;

        for (final Entry<String, String> entrySet : customData.entrySet())
        {
            if ("enabled-nodes".equals(entrySet.getKey())) {
                enabledNodes = Lists.newArrayList();
                for (final JsonElement jsonElement : (new JsonParser()).parse(entrySet.getValue()).getAsJsonArray())
                {
                    enabledNodes.add(jsonElement.getAsString());
                }
            }
        }
        return new OpenGEXModel(location, node, scene, textureMap, enabledNodes);
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
        return new OpenGEXModel(location, this.getNode(), scene, builder.build(), getEnabledNodes());
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

    public OgexNode[] getAllNodes() {
        return allNodes;
    }
    public Integer[] getNodeParents() {
        return nodeParents;
    }

    public OgexScene getScene()
    {
        return scene;
    }

    public List<String> getEnabledNodes()
    {
        return enabledNodes;
    }
}
