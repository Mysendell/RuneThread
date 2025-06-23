package io.github.runethread.customitems.runes;

import net.minecraft.world.item.Item;

public class MainRuneItem extends Item {
    private final int cost;
    private final int scale;
    private final float scalingCost;
    private final RuneFunction runeFunction;

    public MainRuneItem(Properties properties, int cost, int scale, float scalingCost, RuneFunction runeFunction) {
        super(properties);
        this.cost = cost;
        this.scale = scale;
        this.scalingCost = scalingCost;
        this.runeFunction = runeFunction;
    }

    public RuneFunction getRuneFunction() {
        return runeFunction;
    }

    public int getCost() {
        return cost;
    }

    public int getScale() {
        return scale;
    }

    public float getScalingCost() {
        return scalingCost;
    }
}
