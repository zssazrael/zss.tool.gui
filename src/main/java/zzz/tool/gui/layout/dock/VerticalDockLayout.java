package zzz.tool.gui.layout.dock;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import zss.tool.Version;
import zzz.tool.gui.Margin;

@Version("2013-12-22")
public class VerticalDockLayout extends AbstractDockLayout
{
    public VerticalDockLayout(final Margin margin)
    {
        super(margin);
    }

    public Dimension preferredLayoutSize(final Container parent)
    {
        Component component;
        Dimension minimumSize;
        Dimension preferredSize;
        Dimension maximumSize;
        int count = 0;
        Insets insets = parent.getInsets();
        Dimension size = new Dimension(margin.getLeft() + margin.getRight() + insets.left + insets.right, margin.getTop() + margin.getBottom() + insets.top + insets.bottom);
        int width = 0;
        int fillCount = 0;
        int fillHeight = 0;
        for (DockComponent dock : components.values())
        {
            component = dock.component;
            if (!component.isVisible())
            {
                continue;
            }
            minimumSize = component.getMinimumSize();
            preferredSize = component.getPreferredSize();
            maximumSize = component.getMaximumSize();
            count++;

            width = Math.max(width, Math.min(maximumSize.width, Math.max(minimumSize.width, preferredSize.width)));
            if (FILL.equals(dock.type))
            {
                fillCount++;
                fillHeight = Math.max(fillHeight, Math.min(maximumSize.height, Math.max(minimumSize.height, preferredSize.height)));
            }
            else
            {
                size.height += Math.min(maximumSize.height, Math.max(minimumSize.height, preferredSize.height));
            }
        }
        size.width += width;
        size.height += fillCount * fillHeight;
        size.height += Math.max(0, count - 1) * margin.getCenter();
        return size;
    }

    public Dimension minimumLayoutSize(final Container parent)
    {
        Insets insets = parent.getInsets();
        Dimension size = new Dimension(margin.getLeft() + margin.getRight() + insets.left + insets.right, margin.getTop() + margin.getBottom() + insets.top + insets.bottom);
        Component component;
        int count = 0;
        Dimension minimumSize;
        int fillCount = 0;
        int fillHeight = 0;
        int width = 0;
        for (DockComponent dock : components.values())
        {
            component = dock.component;
            if (!component.isVisible())
            {
                continue;
            }
            count++;
            minimumSize = component.getMinimumSize();
            width = Math.max(width, minimumSize.width);
            if (FILL.equals(dock.type))
            {
                fillCount++;
                fillHeight = Math.max(fillHeight, minimumSize.height);
            }
            else
            {
                size.height += minimumSize.height;
            }
        }
        size.width += width;
        size.height += Math.max(0, count - 1) * margin.getCenter();
        size.height += fillCount * fillHeight;
        return size;
    }

    public void layoutContainer(final Container parent)
    {
        Dimension size = parent.getSize();
        Dimension minimumSize;
        Dimension preferredSize;
        Dimension maximumSize;
        Insets insets = parent.getInsets();
        size.width -= margin.getLeft() + margin.getRight() + insets.left + insets.right;
        size.height -= margin.getTop() + margin.getBottom() + insets.top + insets.bottom;
        Component component;
        int fillHeight = 0;
        int fillCount = 0;
        int count = 0;
        for (DockComponent dock : components.values())
        {
            component = dock.component;
            if (!component.isVisible())
            {
                continue;
            }
            count++;
            if (FILL.equals(dock.type))
            {
                fillCount++;
            }
            else
            {
                minimumSize = component.getMinimumSize();
                preferredSize = component.getPreferredSize();
                maximumSize = component.getMaximumSize();
                fillHeight += Math.min(maximumSize.height, Math.max(minimumSize.height, preferredSize.height));
            }
        }
        size.height -= Math.max(0, count - 1) * margin.getCenter();
        fillHeight = (fillCount == 0) ? 0 : ((size.height - fillHeight) / fillCount);
        fillHeight = Math.abs(fillHeight);
        int top = margin.getTop() + insets.top;
        int height;
        int left = margin.getLeft() + insets.left;
        for (DockComponent dock : components.values())
        {
            component = dock.component;
            if (!component.isVisible())
            {
                continue;
            }
            if (FILL.equals(dock.type))
            {
                height = fillHeight;
            }
            else
            {
                minimumSize = component.getMinimumSize();
                preferredSize = component.getPreferredSize();
                maximumSize = component.getMaximumSize();
                height = Math.min(maximumSize.height, Math.max(minimumSize.height, preferredSize.height));
            }
            component.setBounds(left, top, size.width, height);
            top += margin.getCenter() + height;
        }
    }
}
