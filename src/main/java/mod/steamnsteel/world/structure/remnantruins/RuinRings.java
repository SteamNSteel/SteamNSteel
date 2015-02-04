package mod.steamnsteel.world.structure.remnantruins;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Maps;
import mod.steamnsteel.utility.BezierCurve;
import net.minecraft.world.World;
import java.awt.geom.Point2D;
import java.util.*;

public class RuinRings
{
    private final RuinLevel[] ruinLevels;
    private final double minimumRing;
    private final double ringDistance;
    private final double distanceBetweenRuins;

    public RuinRings(RuinLevel[] ruinLevels, double minimumRing, double ringDistance, double distanceBetweenRuins) {
        this.ruinLevels = ruinLevels;
        this.minimumRing = minimumRing;
        this.ringDistance = ringDistance;
        this.distanceBetweenRuins = distanceBetweenRuins;
    }

    Map<Long, List<RuinRing>> cachedResults = new HashMap<Long, List<RuinRing>>();

    public java.util.Iterator<RuinRing> iterate(long world) {
        List<RuinRing> cachedRings = cachedResults.get(world);
        if (cachedRings == null) {
            cachedRings = new ArrayList<RuinRing>(10);
            cachedResults.put(world, cachedRings);
        }
        return new Iterator(world, cachedRings);
    }

    public class Iterator extends AbstractIterator<RuinRing>
    {
        private final long worldSeed;
        private final List<RuinRing> cachedRings;
        private final Random random;
        private int ringNumber = 0;
        public Iterator(long worldSeed, List<RuinRing> cachedRings) {

            this.worldSeed = worldSeed;
            this.random = new Random(worldSeed);
            this.cachedRings = cachedRings;
        }

        @Override
        protected RuinRing computeNext()
        {
            ringNumber++;
            if (cachedRings.size() < ringNumber) {
                return cachedRings.get(ringNumber);
            }

            double ringSize = minimumRing + ringDistance*Math.pow(1 + .50, ringNumber);
            List<Map.Entry<RuinLevel, Double>> probabilities = GetProbabilities((int) ringSize, true);

            double maxRuinSize = 0;
            for (Map.Entry<RuinLevel, Double> pair : probabilities) {
                final Point2D.Double maxRuinSize1 = pair.getKey().getMaxRuinSize();
                double ruinSize = Math.sqrt(Math.pow(maxRuinSize1.x, 2) + Math.pow(maxRuinSize1.y, 2));
                maxRuinSize = ruinSize > maxRuinSize ? ruinSize : maxRuinSize;
            }

            double minRuinRing = (ringSize - maxRuinSize / 2);
            double maxRuinRing = (ringSize + maxRuinSize / 2);

            RuinRing newRing = new RuinRing(ringNumber, ringSize, minRuinRing, maxRuinRing);
            double circumference = 2*Math.PI*ringSize;
            int ruins = (int) (circumference/distanceBetweenRuins);
            double angleBetweenRuins = 360.0/ruins;
            for (double i = angleBetweenRuins/2; i < 360; i += angleBetweenRuins)
            {
                RuinLevel ruinLevel = GetRandomRuinLevel(probabilities, random);

                double x = Math.sin(i * (Math.PI / 180))*ringSize;
                double y = Math.cos(i*(Math.PI/180))*ringSize;

                final Point2D.Double maxRuinSize1 = ruinLevel.getMaxRuinSize();
                double ruinLeft = 0 + x - (maxRuinSize1.x/2);
                double ruinTop = 0 + y - (maxRuinSize1.y/2);

                newRing.addRuin(ruinLevel, ruinLeft, ruinTop);
            }


            cachedRings.add(newRing);

            return newRing;
        }

        private RuinLevel GetRandomRuinLevel(List<Map.Entry<RuinLevel, Double>> probabilities, Random r)
        {
            double totalProbabilities = 0;
            for(Map.Entry<RuinLevel, Double> x : probabilities) {
                totalProbabilities += x.getValue();
            }

            double val = r.nextDouble();
            double currentVal = 0;
            for (Map.Entry<RuinLevel, Double> level : probabilities)
            {
                double levelProbability = level.getValue()/totalProbabilities;

                currentVal += levelProbability;
                if (val < currentVal)
                {
                    return level.getKey();
                }
            }
            return null;
        }

        public List<Map.Entry<RuinLevel, Double>> GetProbabilities(int distanceFromSpawn, boolean onlyValidProbabilities)
        {
            final RuinLevel[] levels = ruinLevels;

            LinkedList<Map.Entry<RuinLevel, Double>> probabilities = new LinkedList<Map.Entry<RuinLevel, Double>>();
            for (RuinLevel level : levels)
            {

                KeyPoint[] keyPoints = level.getKeyPoints();
                if (keyPoints == null || keyPoints.length < 0) continue;

                KeyPoint firstEntry = keyPoints[0];
                KeyPoint lastEntry = keyPoints[keyPoints.length-1];

                double probability = 0;
                if (distanceFromSpawn <= firstEntry.getDistanceFromSpawn())
                {
                    probability = (firstEntry.getProbability());
                }
                else if (distanceFromSpawn >= lastEntry.getDistanceFromSpawn())
                {
                    probability = (lastEntry.getProbability());
                }
                else
                {
                    if (keyPoints.length < 1) continue;

                    KeyPoint pointA = keyPoints[0];
                    for (int i = 1; i < keyPoints.length; ++i) {
                        KeyPoint pointB = keyPoints[i];
                        double distance = ((pointB.getDistanceFromSpawn() - pointA.getDistanceFromSpawn())/2.0f)*pointA.getCurveProfile();
                        Point2D.Double p1 = new Point2D.Double(pointA.getDistanceFromSpawn(), pointA.getProbability());
                        Point2D.Double p2 = new Point2D.Double(((pointA.getDistanceFromSpawn() + distance)), pointA.getProbability());
                        Point2D.Double p3 = new Point2D.Double(((pointB.getDistanceFromSpawn() - distance)), pointB.getProbability());
                        Point2D.Double p4 = new Point2D.Double(pointB.getDistanceFromSpawn(), pointB.getProbability());

                        if (distanceFromSpawn >= p1.x && distanceFromSpawn <= p4.x)
                        {
                            double t = (distanceFromSpawn - p1.x) / (p4.x - p1.x);
                            probability = BezierCurve.GetPointOnCurve(p1, p2, p3, p4, t).y;
                            break;
                        }

                        pointA = pointB;
                    }
                }

                if (!onlyValidProbabilities || probability > 0)
                {
                    probabilities.add(Maps.immutableEntry(level, probability));
                }
            }
            return probabilities;
        }
    }
}
