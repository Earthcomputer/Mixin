package org.spongepowered.asm.mixin.test.inject;

import org.bitstrings.test.junit.runner.ClassLoaderPerTestRunner;
import org.bitstrings.test.junit.runner.ClptrExclude;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongepowered.asm.mixin.test.ITestableTarget;
import org.spongepowered.asm.mixin.test.MixinTestUtil;
import org.spongepowered.asm.mixin.test.inject.mixin.InjectMixin;

import static org.junit.Assert.*;

@RunWith(ClassLoaderPerTestRunner.class)
@ClptrExclude("org.spongepowered.asm.mixin.throwables.")
public class InjectTest {
    @Test
    public void testInject() {
        ITestableTarget object = MixinTestUtil.createTestableTarget(InjectTargetClass.class, InjectMixin.class);
        object.run();
        assertEquals("acb", object.getValue());
    }
}
