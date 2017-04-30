package zzz.tool.gui;

import java.awt.Window;

import javax.swing.JFrame;

import zss.tool.Version;

@Version("2014-01-15")
public abstract class AbstractFrame extends JFrame
{
    private static final long serialVersionUID = 1308444246669L;

    public void addOwnedWindow(final Window child)
    {
        GUITool.addOwnedWindow(this, child);
    }

    public void removeOwnedWindow(final Window child)
    {
        GUITool.removeOwnedWindow(this, child);
    }
}
