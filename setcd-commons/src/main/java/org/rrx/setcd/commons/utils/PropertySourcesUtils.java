package org.rrx.setcd.commons.utils;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/20 14:34
 * @Description:
 */
public class PropertySourcesUtils {

    public static String normalizePrefix(String prefix) {
        return prefix.endsWith(".") ? prefix : prefix + ".";
    }

    public static Map<String, Object> getSubProperties(PropertySources propertySources, PropertyResolver propertyResolver, String prefix) {

        Map<String, Object> subProperties = new LinkedHashMap<String, Object>();

        String normalizedPrefix = normalizePrefix(prefix);

        Iterator<PropertySource<?>> iterator = propertySources.iterator();

        while (iterator.hasNext()) {
            PropertySource<?> source = iterator.next();
            if (source instanceof EnumerablePropertySource) {
                for (String name : ((EnumerablePropertySource<?>) source).getPropertyNames()) {
                    if (!subProperties.containsKey(name) && name.startsWith(normalizedPrefix)) {
                        String subName = name.substring(normalizedPrefix.length());
                        if (!subProperties.containsKey(subName)) { // take first one
                            Object value = source.getProperty(name);
                            if (value instanceof String) {
                                // Resolve placeholder
                                value = propertyResolver.resolvePlaceholders((String) value);
                            }
                            subProperties.put(subName, value);
                        }
                    }
                }
            }
        }

        return unmodifiableMap(subProperties);
    }
}
