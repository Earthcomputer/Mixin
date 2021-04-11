package org.spongepowered.asm.mixin.test;

import com.google.common.io.Closeables;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.IClassProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TestClassProvider implements IClassProvider, IClassBytecodeProvider {
    private final MixinServiceTest mixinService;

    public TestClassProvider(MixinServiceTest mixinService) {
        this.mixinService = mixinService;
    }

    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        InputStream classStream = null;
        ClassReader reader;
        try {
            final String resourcePath = name.replace('.', '/').concat(".class");
            classStream = mixinService.getResourceAsStream(resourcePath);
            if (classStream == null) {
                throw new ClassNotFoundException(name);
            }
            reader = new ClassReader(classStream);
        } finally {
            Closeables.closeQuietly(classStream);
        }

        ClassNode node = new ClassNode();
        reader.accept(node, ClassReader.EXPAND_FRAMES);

        return node;
    }

    @Override
    public ClassNode getClassNode(String name, boolean runTransformers) throws ClassNotFoundException, IOException {
        return getClassNode(name); // never any transformers to run
    }

    @Override
    @Deprecated
    public URL[] getClassPath() {
        return null; // shouldn't be used
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Class<?> findClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Class<?> findAgentClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, MixinServiceTest.class.getClassLoader());
    }
}
