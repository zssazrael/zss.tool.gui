package zzz.tool.gui.layout.table;

import java.awt.Component;

import zss.tool.Version;

@Version("2014-03-09")
class Row
{
    final Component[] components;
    final Style style;

    Row(final Style style, final int size)
    {
        this.style = (style == null) ? Style.GENERAL : style;
        components = new Component[size];
    }
}
