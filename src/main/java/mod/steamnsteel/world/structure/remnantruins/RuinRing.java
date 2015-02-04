package mod.steamnsteel.world.structure.remnantruins;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RuinRing
{

    public final int ringNumber;
    public final double ringSize;
    public final double minRuinRing;
    public final double maxRuinRing;
    private final LinkedList<Ruin> ruins;

    public RuinRing(int ringNumber, double ringSize, double minRuinRing, double maxRuinRing)
    {
        this.ringNumber = ringNumber;
        this.ringSize = ringSize;
        this.minRuinRing = minRuinRing;
        this.maxRuinRing = maxRuinRing;
        this.ruins = new LinkedList<Ruin>();
    }

    public void addRuin(RuinLevel ruinLevel, Random r, double ruinLeft, double ruinTop)
    {
        final List<Schematic> schematics = ruinLevel.getSchematics();
        final Schematic schematic = schematics.get(r.nextInt(schematics.size()));

        ruins.add(new Ruin(ruinLevel, schematic, ruinLeft, ruinTop));
    }

    private class Ruin {
        public final RuinLevel ruinLevel;
        public final Schematic schematic;
        public final double ruinLeft;
        public final double ruinTop;

        public Ruin(RuinLevel ruinLevel, Schematic schematic, double ruinLeft, double ruinTop)
        {
            this.ruinLevel = ruinLevel;
            this.schematic = schematic;
            this.ruinLeft = ruinLeft;
            this.ruinTop = ruinTop;
        }
    }
}
