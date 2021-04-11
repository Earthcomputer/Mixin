package org.spongepowered.asm.mixin.test;

import com.google.common.base.Charsets;
import org.spongepowered.asm.launch.platform.container.ContainerHandleVirtual;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.IClassTracker;
import org.spongepowered.asm.service.IMixinAuditTrail;
import org.spongepowered.asm.service.ITransformerProvider;
import org.spongepowered.asm.service.MixinServiceAbstract;
import org.spongepowered.asm.util.IConsumer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

public class MixinServiceTest extends MixinServiceAbstract {
    public static final String TEST_MIXIN_CONFIG = "/basic_test_config.json";
    private static final String TEST_MIXIN_CONFIG_CONTENT = "{\n" +
            "  \"required\": true,\n" +
            "  \"minVersion\": \"MIN_VERSION\",\n" +
            "  \"package\": \"PACKAGE\",\n" +
            "  \"compatibilityLevel\": \"JAVA_8\",\n" +
            "  \"mixins\": [\n" +
            "    \"MIXIN\"\n" +
            "  ]\n" +
            "}\n";

    private final TestClassProvider classProvider = new TestClassProvider(this);
    private final IContainerHandle containerHandle = new ContainerHandleVirtual("root");
    private IConsumer<MixinEnvironment.Phase> phaseConsumer;

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public IClassProvider getClassProvider() {
        return classProvider;
    }

    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return classProvider;
    }

    @Override
    public ITransformerProvider getTransformerProvider() {
        return null;
    }

    @Override
    public IClassTracker getClassTracker() {
        return null;
    }

    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }

    @Override
    public Collection<String> getPlatformAgents() {
        return Collections.emptyList();
    }

    @Override
    public IContainerHandle getPrimaryContainer() {
        return containerHandle;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (name.startsWith(TEST_MIXIN_CONFIG)) {
            String configJson = TEST_MIXIN_CONFIG_CONTENT;
            configJson = configJson.replace("PACKAGE", MixinTestUtil.currentConfigTestPackage);
            configJson = configJson.replace("MIXIN", MixinTestUtil.currentConfigMixin);
            configJson = configJson.replace("MIN_VERSION", MixinEnvironment.getCurrentEnvironment().getVersion());
            return new ByteArrayInputStream(configJson.getBytes(Charsets.UTF_8));
        }

        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void wire(MixinEnvironment.Phase phase, IConsumer<MixinEnvironment.Phase> phaseConsumer) {
        super.wire(phase, phaseConsumer);
        this.phaseConsumer = phaseConsumer;
    }

    public void onStartup() {
        phaseConsumer.accept(MixinEnvironment.Phase.DEFAULT);
    }
}
