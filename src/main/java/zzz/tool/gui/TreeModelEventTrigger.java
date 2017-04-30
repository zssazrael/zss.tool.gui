package zzz.tool.gui;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import zss.tool.AbstractEventTrigger;
import zss.tool.Version;

@Version("2011-11-20")
public class TreeModelEventTrigger extends AbstractEventTrigger<TreeModelListener> implements TreeModelListener
{
    @Override
    public void treeNodesChanged(final TreeModelEvent event)
    {
        for (TreeModelListener listener : listeners)
        {
            listener.treeNodesChanged(event);
        }
    }

    @Override
    public void treeNodesInserted(final TreeModelEvent event)
    {
        for (TreeModelListener listener : listeners)
        {
            listener.treeNodesInserted(event);
        }
    }

    @Override
    public void treeNodesRemoved(final TreeModelEvent event)
    {
        for (TreeModelListener listener : listeners)
        {
            listener.treeNodesRemoved(event);
        }
    }

    @Override
    public void treeStructureChanged(final TreeModelEvent event)
    {
        for (TreeModelListener listener : listeners)
        {
            listener.treeStructureChanged(event);
        }
    }
}
