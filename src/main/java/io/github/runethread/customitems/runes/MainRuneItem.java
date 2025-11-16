package io.github.runethread.customitems.runes;

import io.github.runethread.customitems.runes.functionrunes.RuneFunction;
import net.minecraft.world.item.Item;

public abstract class MainRuneItem extends Item implements RuneFunction {
    private final int cost;
    private final double scale;
    private final double scalingCost;
    private final double breakChance;

    public MainRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties);
        this.cost = cost;
        this.scale = scale;
        this.scalingCost = scalingCost;
        this.breakChance = breakChance;
    }

    public boolean insideRange(double scale, double range, double distance) {
        return distance <= range;
    }

    public int getCost() {
        return cost;
    }

    public double getScale() {
        return scale;
    }

    public double getScalingCost() {
        return scalingCost;
    }

    public double getBreakChance() {
        return breakChance;
    }

    public boolean requiresReference() {
        return false;
    }
}
