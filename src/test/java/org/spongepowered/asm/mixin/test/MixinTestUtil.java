package org.spongepowered.asm.mixin.test;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.service.MixinService;

public class MixinTestUtil {
    static String currentConfigTestPackage;
    static String currentConfigMixin;
    private static int nextConfigId;

    private MixinTestUtil() {
    }

    private static void setMixins(Class<?>... mixins) {
        for (Class<?> mixin : mixins) {
            String mixinName = mixin.getName();
            int dotIndex = mixinName.lastIndexOf('.');
            currentConfigTestPackage = mixinName.substring(0, dotIndex);
            currentConfigMixin = mixinName.substring(dotIndex + 1);
            Mixins.addConfiguration(MixinServiceTest.TEST_MIXIN_CONFIG + "_" + (nextConfigId++));
        }
    }

    public static ITestableTarget createTestableTarget(Class<? extends ITestableTarget> testClass, Class<?>... mixins) {
        MixinBootstrap.init();
        setMixins(mixins);
        ((MixinServiceTest) MixinService.getService()).onStartup();
        TestClassLoader classLoader = new TestClassLoader(Thread.currentThread().getContextClassLoader(), testClass);
        try {
            Class<?> mixinedClass = classLoader.findClass(testClass.getName());
            return (ITestableTarget) mixinedClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
