package zzz.tool.gui.layout.dock;

import java.awt.Component;

import zss.tool.Version;

@Version("2011-05-26")
public class DockComponent
{
    public final Component component;
    public final String type;

    public DockComponent(final Component component, final String type)
    {
        this.component = component;
        this.type = type;
    }
}
