package zzz.tool.gui;

import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import zss.tool.Version;

public class ThemeLoader
{
    private static final FactoryMap FACTORIES = new FactoryMap();

    static
    {
        FACTORIES.put(new ColorFactory());
        FACTORIES.put(new InsetsFactory());
        FACTORIES.put(new FontFactory());
    }

    public static void load(final Properties properties)
    {
        load(UIManager.getDefaults(), properties);
    }

    public static void load(final UIDefaults defaults, final Properties properties)
    {
        final Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements())
        {
            final String name = (String) names.nextElement();
            final String value = properties.getProperty(name);
            final UIResource resource = newResource(value);
            if (resource != null)
            {
                defaults.put(name, resource);
            }
        }
    }

    private static UIResource newResource(final String value)
    {
        final String[] values = StringUtils.split(value, '|');
        final IResourceFactory factory = FACTORIES.get(values[0]);
        if (factory == null)
        {
            return null;
        }
        return factory.newResource(values);
    }

    @Version("2016-01-05")
    private static class FontFactory implements IResourceFactory
    {
        @Override
        public String type()
        {
            return "Font";
        }

        @Override
        public UIResource newResource(final String... values)
        {
            if (values.length < 4)
            {
                return null;
            }

            String nameString = values[1];
            String styleString = values[2];
            String sizeString = values[3];
            if (StringUtils.isEmpty(nameString) || StringUtils.isEmpty(styleString) || StringUtils.isEmpty(sizeString))
            {
                return null;
            }

            return new FontUIResource(nameString, NumberUtils.toInt(styleString, 1), NumberUtils.toInt(sizeString, 12));
        }
    }

    @Version("2016-01-05")
    private static class InsetsFactory implements IResourceFactory
    {
        @Override
        public String type()
        {
            return "Insets";
        }

        @Override
        public UIResource newResource(final String... values)
        {
            if (values.length < 5)
            {
                return null;
            }

            final String topString = values[1];
            final String leftString = values[2];
            final String bottomString = values[3];
            final String rightString = values[5];
            if (StringUtils.isEmpty(topString) || StringUtils.isEmpty(leftString) || StringUtils.isEmpty(bottomString) || StringUtils.isEmpty(rightString))
            {
                return null;
            }

            return new InsetsUIResource(NumberUtils.toInt(topString, 0), NumberUtils.toInt(leftString, 0), NumberUtils.toInt(bottomString, 0), NumberUtils.toInt(rightString, 0));
        }
    }

    @Version("2016-01-05")
    private static class ColorFactory implements IResourceFactory
    {
        @Override
        public String type()
        {
            return "Color";
        }

        @Override
        public UIResource newResource(final String... values)
        {
            if (values.length < 4)
            {
                return null;
            }
            final String redString = values[1];
            final String greenString = values[2];
            final String blueString = values[3];
            if (StringUtils.isEmpty(redString) || StringUtils.isEmpty(greenString) || StringUtils.isEmpty(blueString))
            {
                return null;
            }
            return new ColorUIResource(NumberUtils.toInt(redString, 0), NumberUtils.toInt(greenString, 0), NumberUtils.toInt(blueString, 0));
        }
    }

    @Version("2016-01-05")
    private static class FactoryMap extends TreeMap<String, IResourceFactory>
    {
        private static final long serialVersionUID = 20160105213413129L;

        private void put(final IResourceFactory factory)
        {
            put(factory.type(), factory);
        }
    }

    @Version("2016-01-05")
    private static interface IResourceFactory
    {
        String type();

        UIResource newResource(String... values);
    }
}
