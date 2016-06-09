package mod.steamnsteel.world.structure.remnantruins;

import com.google.common.collect.Lists;
import org.lwjgl.util.ReadableRectangle;
import java.util.Deque;
import java.util.Optional;

public class RuinRing
{

    public final double ringSize;
    public final double minRuinRing;
    public final double maxRuinRing;
    private final Deque<Ruin> ruins = Lists.newLinkedList();

    public RuinRing(double ringSize, double minRuinRing, double maxRuinRing)
    {
        this.ringSize = ringSize;
        this.minRuinRing = minRuinRing;
        this.maxRuinRing = maxRuinRing;
    }

    public void addRuin(RuinLevel ruinLevel, int ruinLeft, int ruinTop, RuinSchematic ruinSchematic)
    {
        ruins.add(new Ruin(ruinLevel, ruinLeft, ruinTop, ruinSchematic));
    }

    public Optional<Ruin> GetIntersectingRuin(ReadableRectangle chunkRect)
    {
        for (final Ruin ruin : ruins)
        {
            if (ruin.IntersectsChunk(chunkRect)) {
                return Optional.of(ruin);
            }
        }
        return Optional.empty();
    }
}
