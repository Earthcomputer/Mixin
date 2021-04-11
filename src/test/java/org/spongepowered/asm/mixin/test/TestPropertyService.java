package org.spongepowered.asm.mixin.test;

import com.google.common.collect.Maps;
import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

import java.util.Map;

public class TestPropertyService implements IGlobalPropertyService {
    private final Map<String, Object> properties = Maps.newHashMap();

    @Override
    public IPropertyKey resolveKey(String name) {
        return new SimplePropertyKey(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(IPropertyKey key) {
        return (T) properties.get(((SimplePropertyKey) key).key);
    }

    @Override
    public void setProperty(IPropertyKey key, Object value) {
        properties.put(((SimplePropertyKey) key).key, value);
    }

    @Override
    public <T> T getProperty(IPropertyKey key, T defaultValue) {
        T value = getProperty(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public String getPropertyString(IPropertyKey key, String defaultValue) {
        return getProperty(key, defaultValue);
    }

    private static class SimplePropertyKey implements IPropertyKey {
        private final String key;

        private SimplePropertyKey(String key) {
            this.key = key;
        }
    }
}
