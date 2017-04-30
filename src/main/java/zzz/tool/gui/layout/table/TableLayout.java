package zzz.tool.gui.layout.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import zss.tool.Version;
import zzz.tool.gui.Margin;

@Version("2014-03-10")
public class TableLayout implements LayoutManager2
{
    public static final Style FILL = Style.FILL;
    public static final Style GENERAL = Style.GENERAL;

    private final RowList rows = new RowList();

    private final Style[] columns;
    private final Container target;
    private final Margin margin;
    private final int fillColumnSize;

    private int fillRowSize = 0;

    public int getColumnSize()
    {
        return columns.length;
    }

    public synchronized int getRowSize()
    {
        return rows.size();
    }

    @Override
    public float getLayoutAlignmentX(Container target)
    {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target)
    {
        return 0;
    }

    public TableLayout(final Container target, final Margin margin, final Style... columns)
    {
        this.margin = margin;
        this.target = target;
        this.columns = new Style[columns.length];
        int fillColumnSize = 0;
        for (int i = 0; i < columns.length; i++)
        {
            if (columns[i] == FILL)
            {
                fillColumnSize++;
                this.columns[i] = FILL;
            }
            else
            {
                this.columns[i] = GENERAL;
            }
        }
        target.setLayout(this);
        this.fillColumnSize = fillColumnSize;
    }

    public synchronized void addRow(final Style style, final Component... components)
    {
        Row row = new Row(style, columns.length);
        int length = Math.min(components.length, columns.length);
        for (int i = 0; i < length; i++)
        {
            Component component = components[i];
            if (component != null)
            {
                row.components[i] = component;
                target.add(component);
            }
        }
        if (row.style == FILL)
        {
            fillRowSize++;
        }
        rows.add(row);
    }

    public synchronized void clear()
    {
        for (Row row : rows)
        {
            for (int i = 0; i < row.components.length; i++)
            {
                Component component = row.components[i];
                if (component != null)
                {
                    target.remove(component);
                }
            }
        }
        fillRowSize = 0;
        rows.clear();
    }

    public synchronized Component[] removeRow(final int index)
    {
        Row row = rows.remove(index);
        for (int i = 0; i < row.components.length; i++)
        {
            Component component = row.components[i];
            if (component != null)
            {
                target.remove(component);
            }
        }
        if (row.style == FILL)
        {
            fillRowSize--;
        }
        return row.components;
    }

    @Override
    public void addLayoutComponent(final String name, final Component component)
    {
    }

    @Override
    public void addLayoutComponent(final Component component, Object constraints)
    {
    }

    @Override
    public void removeLayoutComponent(final Component component)
    {
    }

    @Override
    public synchronized Dimension preferredLayoutSize(final Container parent)
    {
        return minimumLayoutSize(parent);
    }

    @Override
    public synchronized Dimension minimumLayoutSize(final Container parent)
    {
        int[] widths = new int[columns.length];
        int[] heights = new int[rows.size()];
        int fillHeight = 0;
        int fillWidth = 0;
        for (int y = 0; y < heights.length; y++)
        {
            Row row = rows.get(y);
            for (int x = 0; x < columns.length; x++)
            {
                Component component = row.components[x];
                if (component != null)
                {
                    Dimension preferredSize = component.getPreferredSize();
                    Dimension minimumSize = component.getMinimumSize();
                    int width = Math.max(preferredSize.width, minimumSize.width);
                    int height = Math.max(preferredSize.height, minimumSize.height);
                    if (row.style == FILL)
                    {
                        fillHeight = Math.max(fillHeight, height);
                    }
                    else
                    {
                        heights[y] = Math.max(heights[y], height);
                    }
                    if (columns[x] == FILL)
                    {
                        fillWidth = Math.max(fillWidth, width);
                    }
                    else
                    {
                        widths[x] = Math.max(widths[x], width);
                    }
                }
            }
        }
        int width = margin.getLeft() + margin.getRight() + margin.getCenter() * (columns.length - 1);
        for (int x = 0; x < columns.length; x++)
        {
            width += widths[x];
        }
        width += fillWidth * fillColumnSize;
        int height = margin.getTop() + margin.getBottom() + margin.getCenter() * (heights.length - 1);
        for (int y = 0; y < heights.length; y++)
        {
            height += heights[y];
        }
        height += fillHeight * fillRowSize;
        return new Dimension(Math.max(0, width), Math.max(0, height));
    }

    @Override
    public Dimension maximumLayoutSize(Container target)
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public synchronized void layoutContainer(final Container parent)
    {
        Dimension size = parent.getSize();
        Dimension minimumSize = minimumLayoutSize(parent);
        size.width = Math.max(size.width, minimumSize.width);
        size.height = Math.max(size.height, minimumSize.height);
        int[] widths = new int[columns.length];
        int[] heights = new int[rows.size()];
        for (int y = 0; y < heights.length; y++)
        {
            Row row = rows.get(y);
            for (int x = 0; x < columns.length; x++)
            {
                Component component = row.components[x];
                if (component != null)
                {
                    Dimension preferredSize = component.getPreferredSize();
                    if (row.style != FILL)
                    {
                        heights[y] = Math.max(heights[y], preferredSize.height);
                    }
                    if (columns[x] != FILL)
                    {
                        widths[x] = Math.max(widths[x], preferredSize.width);
                    }
                }
            }
        }

        if (fillColumnSize > 0)
        {
            size.width -= margin.getLeft() + margin.getRight() + margin.getCenter() * (columns.length - 1);
            for (int i = 0; i < widths.length; i++)
            {
                size.width -= widths[i];
            }
            int fillWidth = size.width / fillColumnSize;
            int fillWidthOffset = size.width % fillColumnSize;
            for (int x = 0; x < columns.length; x++)
            {
                if (columns[x] == FILL)
                {
                    if (fillWidthOffset > 0)
                    {
                        widths[x] = fillWidth + 1;
                        fillWidthOffset--;
                    }
                    else
                    {
                        widths[x] = fillWidth;
                    }
                }
            }
        }

        if (fillRowSize > 0)
        {
            size.height -= margin.getTop() + margin.getBottom() + margin.getCenter() * (heights.length - 1);
            for (int i = 0; i < heights.length; i++)
            {
                size.height -= heights[i];
            }
            int fillHeight = size.height / fillRowSize;
            int fillHeightOffset = size.height % fillRowSize;
            for (int y = 0; y < heights.length; y++)
            {
                Row row = rows.get(y);
                if (row.style == FILL)
                {
                    if (fillHeightOffset > 0)
                    {
                        heights[y] = fillHeight + 1;
                        fillHeightOffset--;
                    }
                    else
                    {
                        heights[y] = fillHeight;
                    }
                }
            }
        }

        int top = margin.getTop();
        for (int y = 0; y < heights.length; y++)
        {
            Row row = rows.get(y);
            int height = heights[y];
            int left = margin.getLeft();
            for (int x = 0; x < columns.length; x++)
            {
                int width = widths[x];
                Component component = row.components[x];
                if (component != null)
                {
                    component.setBounds(left, top, width, height);
                }
                left += width + margin.getCenter();
            }
            top += height + margin.getCenter();
        }
    }

    @Override
    public void invalidateLayout(Container target)
    {
    }
}
