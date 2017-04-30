package zzz.tool.gui;

import java.util.LinkedList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import zss.tool.Version;

@Version("2012-11-15")
public class ModifiableListModel<E> implements ListModel<E>
{
    private final ListModelEventTrigger listeners = new ListModelEventTrigger();
    private final LinkedList<E> values = new LinkedList<>();
    private final Object source;

    @Override
    public int getSize()
    {
        return values.size();
    }

    public void add(final E value)
    {
        ListDataEvent event = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, values.size(), values.size());
        values.add(value);
        listeners.intervalAdded(event);
    }

    public void remove(final int index)
    {
        ListDataEvent event = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index, index);
        values.remove(index);
        listeners.intervalRemoved(event);
    }

    public void remove(final E value)
    {
        int index = values.indexOf(value);
        if (index < 0)
        {
            return;
        }
        ListDataEvent event = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index, index);
        values.remove(index);
        listeners.intervalRemoved(event);
    }

    public void clear()
    {
        int index = values.size() - 1;
        if (index < 0)
        {
            return;
        }
        values.clear();
        ListDataEvent event = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, 0, index);
        listeners.intervalRemoved(event);
    }

    @Override
    public E getElementAt(final int index)
    {
        return values.get(index);
    }

    public ModifiableListModel(final Object source)
    {
        this.source = source;
    }

    @Override
    public void addListDataListener(final ListDataListener listener)
    {
        listeners.bind(listener);
    }

    @Override
    public void removeListDataListener(final ListDataListener listener)
    {
        listeners.unbind(listener);
    }
}
