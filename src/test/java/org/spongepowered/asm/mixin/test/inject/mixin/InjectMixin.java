package org.spongepowered.asm.mixin.test.inject.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.test.inject.InjectTargetClass;

@Mixin(InjectTargetClass.class)
public class InjectMixin {
    @Shadow private String value;

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lorg/spongepowered/asm/mixin/test/inject/InjectTargetClass;addB()V"))
    private void addC(CallbackInfo ci) {
        value += "c";
    }
}
