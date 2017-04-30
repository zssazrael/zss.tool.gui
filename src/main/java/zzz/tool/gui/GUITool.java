package zzz.tool.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zss.tool.HexTool;
import zss.tool.ReflectTool;
import zss.tool.Version;

@Version("2016-09-28")
public final class GUITool
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GUITool.class);
    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private static Method connectOwnedWindowMethod;
    private static Field ownedWindowListField;

    public static Color newColor(final String hexColor)
    {
        char[] chars = hexColor.toCharArray();
        return new Color(HexTool.transform(chars[1], chars[2]) & 255, HexTool.transform(chars[3], chars[4]) & 255, HexTool.transform(chars[5], chars[6]) & 255);
    }

    public static Dimension getScreenSize()
    {
        Dimension size = TOOLKIT.getScreenSize();
        Insets insets = TOOLKIT.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        size.height -= insets.bottom + insets.top;
        size.width -= insets.left + insets.right;
        return size;
    }

    public static void fullScreen(final Window window)
    {
        Dimension size = TOOLKIT.getScreenSize();
        Insets insets = TOOLKIT.getScreenInsets(window.getGraphicsConfiguration());
        window.setLocation(insets.left, insets.top);
        window.setSize(size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
    }

    public static Window findWindow(final Component component)
    {
        if (component == null)
        {
            return null;
        }
        if (component instanceof Window)
        {
            return (Window) component;
        }
        return findWindow(component.getParent());
    }

    public static Vector<?> getOwnedWindows(final Window window)
    {
        Field field = getOwnedWindowListField();
        if (field == null)
        {
            return null;
        }
        try
        {
            return ReflectTool.cast(field.get(window), Vector.class);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.warn(e.getMessage(), e);
        }
        catch (IllegalAccessException e)
        {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    private synchronized static Field getOwnedWindowListField()
    {
        if (ownedWindowListField == null)
        {
            try
            {
                ownedWindowListField = Window.class.getDeclaredField("ownedWindowList");
                ownedWindowListField.setAccessible(true);
            }
            catch (SecurityException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            catch (NoSuchFieldException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        return ownedWindowListField;
    }

    public static void removeOwnedWindow(final Window window, final Window child)
    {
        Vector<?> windows = getOwnedWindows(window);
        if (windows == null)
        {
            return;
        }
        Iterator<?> iterator = windows.iterator();
        WeakReference<?> reference;
        while (iterator.hasNext())
        {
            reference = ReflectTool.cast(iterator.next(), WeakReference.class);
            if (reference == null)
            {
                continue;
            }
            if (child.equals(reference.get()))
            {
                iterator.remove();
            }
        }
    }

    public static void addOwnedWindow(final Window window, final Window child)
    {
        if (window == null || child == null)
        {
            return;
        }
        removeOwnedWindow(window, child);
        Method method = getConnectOwnedWindowMethod();
        if (method != null)
        {
            try
            {
                method.invoke(window, child);
            }
            catch (IllegalArgumentException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            catch (IllegalAccessException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            catch (InvocationTargetException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    private synchronized static Method getConnectOwnedWindowMethod()
    {
        if (connectOwnedWindowMethod == null)
        {
            try
            {
                connectOwnedWindowMethod = Window.class.getDeclaredMethod("connectOwnedWindow", Window.class);
                connectOwnedWindowMethod.setAccessible(true);
            }
            catch (SecurityException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            catch (NoSuchMethodException e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        return connectOwnedWindowMethod;
    }

    public static void loadSynth(final File file)
    {
        InputStream stream;
        try
        {
            stream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.warn(e.getMessage(), e);
            return;
        }
        try
        {
            loadSynth(stream);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void loadSynth(final String path)
    {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (stream == null)
        {
            return;
        }
        try
        {
            loadSynth(stream);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void loadSynth(final InputStream stream)
    {
        SynthLookAndFeel synth = new SynthLookAndFeel();
        try
        {
            synth.load(stream, GUITool.class);
            UIManager.setLookAndFeel(synth);
        }
        catch (UnsupportedLookAndFeelException e)
        {
            LOGGER.warn(e.getMessage(), e);
        }
        catch (ParseException e)
        {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    public static void moveToCenter(final Window window)
    {
        Window owner = window.getOwner();
        if (owner == null)
        {
            moveToScreenCenter(window);
        }
        else
        {
            moveToCenter(window, owner);
        }
    }

    public static void moveToScreenCenter(final Window window)
    {
        Dimension size = TOOLKIT.getScreenSize();
        window.setLocation((size.width - window.getWidth()) / 2, (size.height - window.getHeight()) / 2);
    }

    public static void moveToCenter(final Window window, final Window owner)
    {
        window.setLocation((owner.getWidth() - window.getWidth()) / 2 + owner.getX(), (owner.getHeight() - window.getHeight()) / 2 + owner.getX());
    }

    public static FontRenderContext newFontRenderContext()
    {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = image.createGraphics();
        graphics.dispose();
        return graphics.getFontRenderContext();
    }
}
