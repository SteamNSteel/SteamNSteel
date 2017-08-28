package mod.steamnsteel.world.structure.remnantruins;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Maps;
import mod.steamnsteel.utility.BezierCurve;
import mod.steamnsteel.utility.log.Logger;
import org.lwjgl.util.Dimension;
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
        private int ringNumber = 0;
        public Iterator(long worldSeed, List<RuinRing> cachedRings) {

            this.worldSeed = worldSeed;
            this.cachedRings = cachedRings;
        }

        @Override
        protected RuinRing computeNext()
        {
            ringNumber++;
            //Use the cache if we can.
            if (ringNumber < cachedRings.size()) {
                return cachedRings.get(ringNumber - 1);
            }

            //First we'll calculate the size of the ring.
            double ringSize = minimumRing + ringDistance * Math.pow(1 + .50, ringNumber);

            //There are multiple curves of "chances a ruin of tier x will spawn"
            //This gets only the probabilities that exist at this distance from spawn.
            List<Map.Entry<RuinLevel, Double>> probabilities = GetProbabilities((int) ringSize, true);
            //Calculates the minimum size of the ring bands.
            double maxRuinRadius = 0;
            for (Map.Entry<RuinLevel, Double> pair : probabilities) {
                final Dimension maxRuinSize1 = pair.getKey().getMaxRuinSize();
                double ruinSize = Math.sqrt(Math.pow(maxRuinSize1.getWidth(), 2) + Math.pow(maxRuinSize1.getHeight(), 2));
                maxRuinRadius = ruinSize > maxRuinRadius ? ruinSize : maxRuinRadius;
            }

            //Calculates two radii, that correspond to the minimum radius a ruin on this band can spawn in, and the maximum radius.
            double minRuinRing = (ringSize - maxRuinRadius / 2);
            double maxRuinRing = (ringSize + maxRuinRadius / 2);

            Logger.info("ring #%d %f -> %f", ringNumber, minRuinRing, maxRuinRing);

            //Create a seed specifically for this band. Not technically neccessary, but I feel it will improve determinism.
            Random random = new Random(worldSeed ^ (int) ringSize);

            RuinRing ruinRing = new RuinRing(ringSize, minRuinRing, maxRuinRing);

            //Now calculate the location of each ruin.
            double circumference = 2 * Math.PI * ruinRing.ringSize;
            int ruins = (int)(circumference / distanceBetweenRuins);
            double angleBetweenRuins = 360.0 / ruins;
            for (double i = angleBetweenRuins / 2; i < 360; i += angleBetweenRuins)
            {
                //Using the probabilities from earlier, select a ruin level.
                RuinLevel ruinLevel = GetRandomRuinLevel(probabilities, random);
                if (ruinLevel.schematics == null || ruinLevel.schematics.size() == 0) {
                    Logger.info("Unable to create ruin, no schematics available for RuinLevel %s", ruinLevel.getLevelName());
                    continue;
                }
                int x = (int)(Math.sin(i * (Math.PI / 180)) * ruinRing.ringSize);
                int z = (int)(Math.cos(i * (Math.PI / 180)) * ruinRing.ringSize);

                //Select a schematic
                RuinSchematic ruinSchematic = ruinLevel.schematics.get(random.nextInt(ruinLevel.schematics.size()));

                //Add it to the ring.
                ruinRing.addRuin(ruinLevel, x, z, ruinSchematic);
            }

            //cache the results for future chunk queries.
            cachedRings.add(ruinRing);

            return ruinRing;
        }

        private RuinLevel GetRandomRuinLevel(List<Map.Entry<RuinLevel, Double>> probabilities, Random r)
        {
            //First calculate the sum of all probabilities.
            double totalProbabilities = 0;
            for(Map.Entry<RuinLevel, Double> x : probabilities) {
                totalProbabilities += x.getValue();
            }


            double val = r.nextDouble();
            double currentVal = 0;
            for (Map.Entry<RuinLevel, Double> level : probabilities)
            {
                //Calculate the ratio of this probability to all probabilities
                double levelProbability = level.getValue()/totalProbabilities;

                currentVal += levelProbability;
                if (val < currentVal)
                {
                    //located a ruin with this probability.
                    return level.getKey();
                }
            }
            return null;
        }

        public List<Map.Entry<RuinLevel, Double>> GetProbabilities(int distanceFromSpawn, boolean onlyValidProbabilities)
        {
            final RuinLevel[] levels = ruinLevels;

            LinkedList<Map.Entry<RuinLevel, Double>> probabilities = new LinkedList<Map.Entry<RuinLevel, Double>>();
            //Each RuinLevel has many probabilities.
            for (RuinLevel level : levels)
            {

                //Probabilities are based on a set of KeyPoints, which essentially make up Bezier curves. The X Axis is
                //The distance from spawn. The Y axis is the probability of that ruinLevel at that distance from spawn.
                KeyPoint[] keyPoints = level.getKeyPoints();
                if (keyPoints == null || keyPoints.length < 0) continue;

                KeyPoint firstEntry = keyPoints[0];
                KeyPoint lastEntry = keyPoints[keyPoints.length-1];

                double probability = 0;
                //The first keyPoint defines the probability leading up to it's distance from spawn, likewise
                if (distanceFromSpawn <= firstEntry.getDistanceFromSpawn())
                {
                    probability = (firstEntry.getProbability());
                }
                //the last KeyPoint defines the probability after this curve has ended onwards.
                else if (distanceFromSpawn >= lastEntry.getDistanceFromSpawn())
                {
                    probability = (lastEntry.getProbability());
                }
                else
                {
                    //We need more than one KeyPoint to calculate a curve.
                    if (keyPoints.length < 1) continue;

                    KeyPoint pointA = keyPoints[0];
                    for (int i = 1; i < keyPoints.length; ++i) {
                        KeyPoint pointB = keyPoints[i];
                        double distance = ((pointB.getDistanceFromSpawn() - pointA.getDistanceFromSpawn())/2.0f)*pointA.getCurveProfile();
                        //Curve Start
                        Point2D.Double p1 = new Point2D.Double(pointA.getDistanceFromSpawn(), pointA.getProbability());
                        //Curve Control
                        Point2D.Double p2 = new Point2D.Double(((pointA.getDistanceFromSpawn() + distance)), pointA.getProbability());
                        Point2D.Double p3 = new Point2D.Double(((pointB.getDistanceFromSpawn() - distance)), pointB.getProbability());
                        //Curve End
                        Point2D.Double p4 = new Point2D.Double(pointB.getDistanceFromSpawn(), pointB.getProbability());

                        //If we've successfully matched the distance from spawn on this curve
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
