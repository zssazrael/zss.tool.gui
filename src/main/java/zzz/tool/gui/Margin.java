package zzz.tool.gui;

import java.awt.Insets;

import zss.tool.Version;

@Version("2012-11-15")
public class Margin
{
    public static final Margin M_8_8_8_8_8 = new Margin(8);
    public static final Margin M_0_0_0_0_0 = new Margin(0);
    public static final Margin M_0_0_0_0_8 = new Margin(0, 0, 8);
    public static final Margin M_0_8_8_8_8 = new Margin(0, 8, 8, 8, 8);
    public static final Insets I_0_8_0_8 = new Insets(0, 8, 0, 8);
    public static final Insets I_0_0_0_0 = new Insets(0, 0, 0, 0);

    private final int center;
    private final int top;
    private final int left;
    private final int bottom;
    private final int right;

    public int getBottom()
    {
        return bottom;
    }

    public int getCenter()
    {
        return center;
    }

    public int getLeft()
    {
        return left;
    }

    public int getRight()
    {
        return right;
    }

    public int getTop()
    {
        return top;
    }

    public Margin(int top, int bottom, int left, int right, int center)
    {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.center = center;
    }

    public Margin(int vertical, int horizontal, int center)
    {
        this(vertical, vertical, horizontal, horizontal, center);
    }

    public Margin(int border, int center)
    {
        this(border, border, center);
    }

    public Margin(int space)
    {
        this(space, space);
    }

    public Margin()
    {
        this(0);
    }
}
