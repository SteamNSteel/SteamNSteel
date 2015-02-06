package mod.steamnsteel.world.structure.remnantruins;

import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;

public class Ruin
{
    public final RuinLevel ruinLevel;
    public final Schematic schematic;
    public final Point location;

    public Ruin(RuinLevel ruinLevel, int ruinX, int ruinY, Schematic ruinSchematic)
    {
        this.ruinLevel = ruinLevel;
        this.location = new Point(ruinX - ruinLevel.getMaxRuinSize().getWidth() / 2, ruinY - ruinLevel.getMaxRuinSize().getHeight() / 2);
        this.schematic = ruinSchematic;
    }

    public Rectangle getBoundingRectangle()
    {
        return new Rectangle(
                location,
                ruinLevel.getMaxRuinSize()
        );
    }

    public boolean IntersectsChunk(Rectangle chunkRect)
    {
        Rectangle bounds = getBoundingRectangle();

        return !(chunkRect.getX() > (bounds.getX() + bounds.getWidth()) ||
                (chunkRect.getX() + chunkRect.getWidth()) < bounds.getX()||
                chunkRect.getY() > (bounds.getY() + bounds.getHeight()) ||
                (chunkRect.getY() + chunkRect.getHeight()) < bounds.getY()
        );
    }

    public Rectangle GetIntersectingRect(Rectangle chunkRect)
    {
        Rectangle bounds = getBoundingRectangle();

        int x1 = Math.max(bounds.getX(), chunkRect.getX());
        int x2 = Math.min(bounds.getX() + bounds.getWidth(), chunkRect.getX() + chunkRect.getWidth());

        int y1 = Math.max(bounds.getY(), chunkRect.getY());
        int y2 = Math.min(bounds.getY() + bounds.getHeight(), chunkRect.getY() + chunkRect.getHeight());

        if (x2 >= x1 && y2 >= y1)
        {
            return new Rectangle(x1, y1, x2 - x1, y2 - y1);
        }
        return new Rectangle(0, 0, 0, 0);
    }
}
