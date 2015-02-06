package mod.steamnsteel.world.structure.remnantruins;

import org.lwjgl.util.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RuinRing
{

    public final double ringSize;
    public final double minRuinRing;
    public final double maxRuinRing;
    private final LinkedList<Ruin> ruins = new LinkedList<Ruin>();;

    public RuinRing(double ringSize, double minRuinRing, double maxRuinRing)
    {
        this.ringSize = ringSize;
        this.minRuinRing = minRuinRing;
        this.maxRuinRing = maxRuinRing;
    }

    public void addRuin(RuinLevel ruinLevel, int ruinLeft, int ruinTop, Schematic ruinSchematic)
    {
        ruins.add(new Ruin(ruinLevel, ruinLeft, ruinTop, ruinSchematic));
    }

    public Ruin GetIntersectingRuin(Rectangle chunkRect)
    {
        for (final Ruin ruin : ruins)
        {
            if (ruin.IntersectsChunk(chunkRect)) {
                return ruin;
            }
        }
        return null;
    }

}
