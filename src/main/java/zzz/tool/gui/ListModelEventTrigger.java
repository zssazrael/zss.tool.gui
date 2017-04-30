package zzz.tool.gui;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import zss.tool.AbstractEventTrigger;
import zss.tool.Version;

@Version("2012-11-15")
public class ListModelEventTrigger extends AbstractEventTrigger<ListDataListener> implements ListDataListener
{
    @Override
    public void intervalAdded(final ListDataEvent event)
    {
        for (ListDataListener listener : listeners)
        {
            listener.intervalAdded(event);
        }
    }

    @Override
    public void intervalRemoved(final ListDataEvent event)
    {
        for (ListDataListener listener : listeners)
        {
            listener.intervalRemoved(event);
        }
    }

    @Override
    public void contentsChanged(final ListDataEvent event)
    {
        for (ListDataListener listener : listeners)
        {
            listener.contentsChanged(event);
        }
    }
}
