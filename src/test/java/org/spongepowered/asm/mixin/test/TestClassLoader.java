package org.spongepowered.asm.mixin.test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.transformers.MixinClassWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class TestClassLoader extends ClassLoader {
    private final Set<String> targetClasses = Sets.newHashSet();
    private final Map<String, Class<?>> classCache = Maps.newHashMap();

    private final MixinTransformer transformer = new MixinTransformer();

    public TestClassLoader(ClassLoader parent, Class<?>... targetClasses) {
        super(parent);

        for (Class<?> clazz : targetClasses) {
            this.targetClasses.add(clazz.getName());
        }
    }

    @Override
    protected synchronized Class<?> findClass(String name) throws ClassNotFoundException {
        if (!targetClasses.contains(name)) {
            return super.findClass(name);
        }

        Class<?> clazz = classCache.get(name);
        if (clazz != null) {
            return clazz;
        }

        ClassNode node;
        try {
            InputStream classStream = getResourceAsStream(name.replace('.', '/') + ".class");
            if (classStream == null) {
                throw new ClassNotFoundException(name);
            }

            ClassReader reader = new ClassReader(classStream);
            node = new ClassNode();
            reader.accept(node, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }

        transformer.transformClass(MixinEnvironment.getCurrentEnvironment(), name, node);

        ClassWriter writer = new MixinClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        byte[] bytecode = writer.toByteArray();
        clazz = defineClass(name, bytecode, 0, bytecode.length);

        classCache.put(name, clazz);
        return clazz;
    }
}
