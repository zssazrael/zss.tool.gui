package zzz.tool.gui.layout.dock;

import java.awt.Component;
import java.awt.LayoutManager;

import zss.tool.Version;
import zzz.tool.gui.Margin;

@Version("2011-11-20")
public abstract class AbstractDockLayout implements LayoutManager
{
    protected final ComponentMap components = new ComponentMap();
    protected final Margin margin;
    public static final String FILL = "FILL";
    public static final String GENERAL = "GENERAL";

    public AbstractDockLayout(final Margin margin)
    {
        this.margin = (margin == null) ? new Margin() : margin;
    }

    public void addLayoutComponent(final String type, final Component component)
    {
        components.put(component, new DockComponent(component, type));
    }

    public void removeLayoutComponent(final Component component)
    {
        components.remove(component);
    }
}
