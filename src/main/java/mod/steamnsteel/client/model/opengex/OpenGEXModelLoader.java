package mod.steamnsteel.client.model.opengex;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import mod.steamnsteel.client.model.opengex.ogex.*;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Set;

public enum OpenGEXModelLoader implements ICustomModelLoader {
    instance;

    private IResourceManager manager;
    private final Map<ResourceLocation, OgexScene> cache = Maps.newHashMap();

    OpenGEXModelLoader() {
        enabledDomains.add("steamnsteel_opengex");
    }

    private Set<String> enabledDomains = Sets.newHashSet();

    public void addDomain(String domain)
    {
        enabledDomains.add(domain.toLowerCase());
    }

    public boolean accepts(ResourceLocation modelLocation) {
        return enabledDomains.contains(modelLocation.getResourceDomain().toLowerCase()) &&
                modelLocation.getResourcePath().toLowerCase().endsWith(".ogex");
    }

    public void onResourceManagerReload(IResourceManager manager)
    {
        this.manager = manager;
        cache.clear();
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        if(!cache.containsKey(file))
        {
            try
            {
                IResource resource;
                try
                {
                    resource = manager.getResource(file);
                }
                catch(FileNotFoundException e)
                {
                    if(modelLocation.getResourcePath().startsWith("models/block/"))
                        resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/block/".length())));
                    else if(modelLocation.getResourcePath().startsWith("models/item/"))
                        resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/item/".length())));
                    else throw e;
                }

                final OgexParser ogexParser = new OgexParser();
                Reader reader = new InputStreamReader(resource.getInputStream());
                final OgexScene ogexScene = ogexParser.parseScene(reader);


                cache.put(file, ogexScene);
            }
            catch(IOException e)
            {
                FMLLog.log(Level.ERROR, e, "Exception loading model %s with OGEX loader, skipping", modelLocation);
                cache.put(file, null);
                throw e;
            }
        }

        final OgexScene scene = cache.get(file);
        if (scene == null) {
            return ModelLoaderRegistry.getMissingModel();
        }



        if (scene.getMetrics().getUp() == Axis.Z) {
            float[] cycleMatrix = {
                    0, 0, 1, 0,
                    1, 0, 0, 0,
                    0, 1, 0, 0,
                    0, 0, 0, 1
            };

            for (final OgexNode ogexNode : scene)
            {
                OgexMatrixTransform matrixTransform = new OgexMatrixTransform();
                matrixTransform.setMatrix(cycleMatrix);
                ogexNode.getTransforms().add(0, matrixTransform);
            }
        }

        //Just making it compile.
        return new OpenGEXModel(file, scene);
    }



}
