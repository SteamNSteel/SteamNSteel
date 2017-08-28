package mod.steamnsteel.client.model.opengex;

import mod.steamnsteel.client.model.opengex.ogex.*;
import mod.steamnsteel.utility.log.Logger;

/**
 * Created by codew on 4/11/2015.
 */
public class Animation
{
    //WARNING: THIS METHOD IS NOT THREAD SAFE. DO NOT GET CREATIVE WITH IT.
    public static float[][] calculateTransforms(OgexScene scene, float animationTime, OgexNode[] nodes, Integer[] nodeParentMap) {
        //long currentAnimationFrameTime = 0;
        //long animationTime = 0;

        final float[][] nodeTransforms = new float[nodes.length][16];
        final float[][] parentTransforms = new float[nodes.length][16];

        for (int nodeIndex = 0; nodeIndex < nodes.length; nodeIndex++) {
            final OgexNode node = nodes[nodeIndex];

            float[] nodeMatrix = OgexMatrixTransform.identity();
            final Integer parentNodeIndex = nodeParentMap[nodeIndex];
            if (parentNodeIndex != null) {
                final float[] parentMatrix = parentTransforms[parentNodeIndex];
                nodeMatrix = parentMatrix.clone();
            }

            for (final OgexAnimation animation : node.getAnimations()) {
                float actualAnimationTime = animation.getBegin() + animationTime;
                while (actualAnimationTime > animation.getEnd()) {
                    actualAnimationTime -= animation.getEnd();
                }

                for (final OgexTrack track : animation) {
                    Object target = track.getTarget();
                    float value = getCurrentValueForTrack(scene, track, actualAnimationTime);

                    if (target instanceof OgexTranslation.ComponentTranslation) {
                        ((OgexTranslation.ComponentTranslation) target).setTranslation(value);
                    } else if (target instanceof OgexRotation.ComponentRotation) {
                        ((OgexRotation.ComponentRotation) target).setAngle(value);
                    } else {
                        Logger.info("Breakpoint");
                    }
                }
            }

            float[] parentMatrix = nodeMatrix.clone();
            for (OgexTransform transform : node.getTransforms()) {
                final float[] right = transform.toMatrix();
                nodeMatrix = OgexMatrixTransform.multiply(nodeMatrix, right);

                if (!transform.isObjectOnly()) {
                    parentMatrix = OgexMatrixTransform.multiply(parentMatrix, right);
                }
            }

            nodeTransforms[nodeIndex] = nodeMatrix;
            parentTransforms[nodeIndex] = parentMatrix;
        }

        //lastAnimationFrameTime = currentAnimationFrameTime;

        return nodeTransforms;
    }

    private static float getCurrentValueForTrack(OgexScene model, OgexTrack track, float animationTime) {
        final TimeData keyData = getKeyData(track.getTime());
        final ValueData valueData = getValueData(track.getValue());
        float timeScale = model.getMetrics().getTime();
        int index = getKeyIndexForTime(keyData, timeScale, animationTime);

        float adjustedTime = 1;
        if (track.getTime().getCurve() == Curve.Bezier) {
            adjustedTime = getAdjustedBezierTime(keyData, timeScale, animationTime, index);
        } else if (track.getTime().getCurve() == Curve.Linear) {
            adjustedTime = getAdjustedLinearTime(keyData, timeScale, animationTime, index);
        }

        float trackValue = 1;
        if (track.getValue().getCurve() == Curve.Bezier) {
            trackValue = getAdjustedBezierValue(valueData, index, adjustedTime);
        } else if (track.getValue().getCurve() == Curve.Linear) {
            trackValue = getAdjustedLinearValue(valueData, index, adjustedTime);
        }


        return trackValue;
    }

    private static float getAdjustedLinearValue(ValueData valueData, int index, float adjustedTime) {
        final float[] value = valueData.value;
        final float s = adjustedTime;
        final float v1 = value[index - 1];
        final float v2 = value[index];

        //float si = (s - v1) / (v2 - v1);

        return (s * (v2 - v1)) + v1;
    }

    private static float getAdjustedBezierValue(ValueData valueData, int index, float adjustedTime) {
        final float[] value = valueData.value;
        final float s = adjustedTime;
        final float v1 = value[index - 1];
        final float p1 = valueData.positiveControl[index - 1];
        final float p2 = valueData.negativeControl[index];
        final float v2 = value[index];

        float v = (((1 - s) * (1 - s) * (1 - s)) * v1) +
                (3 * s * ((1 - s) * (1 - s)) * p1) +
                (3 * (s * s) * (1 - s) * p2) +
                (( s * s * s) * v2);

        return v;
    }


    private static float getAdjustedLinearTime(TimeData keyData, float scale, float currentTime, int index) {
        final float[] value = keyData.value;
        float si;

        float t = currentTime;

        final float t1 = value[index - 1] * scale;
        final float t2 = value[index] * scale;

        // Returns 0 when (t1 < t2) fails.
        if (t1 >= t2)
            return 0;

        si = (t - t1) / (t2 - t1);

        float finalTime = value[value.length - 1];
        if (si > finalTime) {
            si = finalTime;
        }

        return si;
    }

    private static float getAdjustedBezierTime(TimeData keyData, float scale, float currentTime, int index) {
        final float[] value = keyData.value;
        float[] positiveControl = keyData.positiveControl;
        float[] negativeControl = keyData.negativeControl;

        //TODO: This is a nasty hack
        float si = 0;

        float t = currentTime;

        final float t1 = value[index - 1] * scale;
        final float c1 = positiveControl[index - 1] * scale;
        final float c2 = negativeControl[index] * scale;
        final float t2 = value[index] * scale;

        // Returns 0 when (t1 < c1 < c2 < t2) fails.
        if (t1 >= c1 || c1 >= c2 || c2 >= t2)
            return 0;

        int i = 0;
        float oldSi = 0;

        // Newton's Method to calculate the closet value for the time
        while (true) {
            if (i == 0) {
                si = (t - t1) / (t2 - t1);
            } else {
                float splusone = si - (
                        ((t2 - (3 * c2) + (3 * c1) - t1) * (si * si * si)) +
                                (3 * (c2 - (2 * c1) + t1) * (si * si)) +
                                (3 * (c1 - t1) * si) +
                                t1 - t
                ) / (
                        (3 * (t2 - (3 * c2) + (3 * c1) - t1) * (si * si)) +
                                (6 * (c2 - (2 * c1) + t1) * si) +
                                3 * (c1 - t1)
                );

                si = splusone;
            }

            // If both are old s and s are equal within 4 decimal points then we reached the value,
            // Limited by 4 attempts. Testing suggests 1 is usually enough.
            if ( Math.floor(oldSi * 10000) == Math.floor(si * 10000) || i >= 4)
                break;
            i++;
            oldSi = si;
        }

        float finalTime = value[value.length - 1];
        if (si > finalTime) {
            si = finalTime;
        }

        return si;
    }

    private static int getKeyIndexForTime(TimeData keyData, float scale, float currentTime) {
        final float[] value = keyData.value;

        int i = 0;
        for (; i < value.length; i++) {
            if ((value[i] * scale) > currentTime) {
                break;
            }
        }

        if (i >= value.length) return value.length-1;
        if (i <= 0) return 0;
        return i;
    }

    private static TimeData getKeyData(OgexTime time) {
        final OgexKey[] keys = time.getKeys();
        TimeData timeData = new TimeData(keys);

        for (OgexKey key : keys) {
            switch (key.getKind()) {
                case Value:
                    timeData.value = (float[]) key.getData();
                    break;
                case PositiveControl:
                    timeData.positiveControl = (float[]) key.getData();
                    break;
                case NegativeControl:
                    timeData.negativeControl = (float[]) key.getData();
                    break;
            }
        }
        return timeData;
    }

    private static ValueData getValueData(OgexValue value) {
        final OgexKey[] keys = value.getKeys();
        ValueData timeData = new ValueData(keys);

        for (OgexKey key : keys) {
            switch (key.getKind()) {
                case Value:
                    timeData.value = (float[]) key.getData();
                    break;
                case PositiveControl:
                    timeData.positiveControl = (float[]) key.getData();
                    break;
                case NegativeControl:
                    timeData.negativeControl = (float[]) key.getData();
                    break;
                case Bias:
                    timeData.bias = (float[]) key.getData();
                    break;
                case Continuity:
                    timeData.continuity = (float[]) key.getData();
                    break;
                case Tension:
                    timeData.tension = (float[]) key.getData();
                    break;
            }
        }
        return timeData;
    }

    private static class ValueData {
        OgexKey[] keys;
        float[] value;
        float[] positiveControl;
        float[] negativeControl;
        float[] bias;
        float[] continuity;
        float[] tension;

        public ValueData(OgexKey[] keys) {
            this.keys = keys;
        }
    }

    private static class TimeData {
        OgexKey[] keys;
        float[] value;
        float[] positiveControl;
        float[] negativeControl;

        public TimeData(OgexKey[] keys) {
            this.keys = keys;
        }
    }
}
