package zzz.tool.gui.layout.dock;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import zss.tool.Version;
import zzz.tool.gui.Margin;

@Version("2013-12-22")
public class HorizontalDockLayout extends AbstractDockLayout
{
    public HorizontalDockLayout(final Margin margin)
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
        int height = 0;
        int fillCount = 0;
        int fillWidth = 0;
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
            height = Math.max(height, Math.min(maximumSize.height, Math.max(minimumSize.height, preferredSize.height)));
            if (FILL.equals(dock.type))
            {
                fillCount++;
                fillWidth = Math.max(fillWidth, Math.min(maximumSize.width, Math.max(minimumSize.width, preferredSize.width)));
            }
            else
            {
                size.width += Math.min(maximumSize.width, Math.max(minimumSize.width, preferredSize.width));
            }
        }
        size.height += height;
        size.width += fillCount * fillWidth;
        size.width += Math.max(0, count - 1) * margin.getCenter();
        return size;
    }

    public Dimension minimumLayoutSize(final Container parent)
    {
        int fillCount = 0;
        Component component;
        int fillMinimumWidth = 0;
        Dimension size;
        int maximumMinimumHeight = 0;
        Insets insets = parent.getInsets();
        int minimumHeight = margin.getTop() + margin.getBottom() + insets.top + insets.bottom;
        int minimumWidth = margin.getLeft() + margin.getRight() + insets.left + insets.right;
        int count = 0;
        for (DockComponent dock : components.values())
        {
            component = dock.component;
            if (!component.isVisible())
            {
                continue;
            }
            size = component.getMinimumSize();
            count++;
            if (maximumMinimumHeight < size.height)
            {
                maximumMinimumHeight = size.height;
            }
            if (FILL.equals(dock.type))
            {
                fillCount++;
                if (fillMinimumWidth < size.width)
                {
                    fillMinimumWidth = size.width;
                }
            }
            else
            {
                minimumWidth += size.width;
            }
        }
        minimumHeight += maximumMinimumHeight;
        minimumWidth += fillCount * fillMinimumWidth;
        minimumWidth += Math.max(0, count - 1) * margin.getCenter();
        return new Dimension(minimumWidth, minimumHeight);
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
        int fillWidth = 0;
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
                fillWidth += Math.min(maximumSize.width, Math.max(minimumSize.width, preferredSize.width));
            }
        }
        size.width -= Math.max(0, count - 1) * margin.getCenter();
        fillWidth = (fillCount == 0) ? 0 : ((size.width - fillWidth) / fillCount);
        fillWidth = Math.abs(fillWidth);
        int left = margin.getLeft() + insets.left;
        int width;
        int top = margin.getTop() + insets.top;
        for (DockComponent dock : components.values())
        {
            component = dock.component;
            if (!component.isVisible())
            {
                continue;
            }
            if (FILL.equals(dock.type))
            {
                width = fillWidth;
            }
            else
            {
                minimumSize = component.getMinimumSize();
                preferredSize = component.getPreferredSize();
                maximumSize = component.getMaximumSize();
                width = Math.min(maximumSize.width, Math.max(minimumSize.width, preferredSize.width));
            }
            component.setBounds(left, top, width, size.height);
            left += margin.getCenter() + width;
        }
    }
}
