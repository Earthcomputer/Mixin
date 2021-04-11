package org.spongepowered.asm.mixin.test.inject;

import org.spongepowered.asm.mixin.test.ITestableTarget;

public class InjectTargetClass implements ITestableTarget {
    private String value = "";

    @Override
    public void run() {
        addA();
        addB();
    }

    private void addA() {
        value += "a";
    }

    private void addB() {
        value += "b";
    }

    @Override
    public String getValue() {
        return value;
    }
}
