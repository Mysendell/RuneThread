package io.github.runethread.customitems.runes;

import net.minecraft.world.item.Item;

public class MainRuneItem extends Item {
    private final int cost;
    private final int scale;
    private final double scalingCost;
    private final RuneFunction runeFunction;
    private final double breakChance;

    public MainRuneItem(Properties properties, int cost, int scale, double scalingCost, double breakChance , RuneFunction runeFunction) {
        super(properties);
        this.cost = cost;
        this.scale = scale;
        this.scalingCost = scalingCost;
        this.runeFunction = runeFunction;
        this.breakChance = breakChance;
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

    public double getScalingCost() {
        return scalingCost;
    }

    public double getBreakChance() {
        return breakChance;
    }
}
