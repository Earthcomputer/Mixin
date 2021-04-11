package org.spongepowered.asm.mixin.test;

import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class MixinServiceTestBootstrap implements IMixinServiceBootstrap {
    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public String getServiceClassName() {
        return "org.spongepowered.asm.mixin.test.MixinServiceTest";
    }

    @Override
    public void bootstrap() {
    }
}
