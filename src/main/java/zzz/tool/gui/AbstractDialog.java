package zzz.tool.gui;

import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import zss.tool.Version;

@Version("2012-11-15")
public abstract class AbstractDialog extends JDialog
{
    private static final long serialVersionUID = 1308842834850L;

    public AbstractDialog()
    {
        contentPane = new JPanel();
        getRootPane().getContentPane().add(contentPane);
    }

    public void addOwnedWindow(final Window child)
    {
        GUITool.addOwnedWindow(this, child);
    }

    public void removeOwnedWindow(final Window child)
    {
        GUITool.removeOwnedWindow(this, child);
    }
    
    @Override
    public void setContentPane(final Container contentPane)
    {
    }

    protected final JPanel contentPane;
}
