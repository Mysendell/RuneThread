package io.github.runethread.customitems.runes;

import io.github.runethread.customitems.runes.functionrunes.RuneFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    protected int getCost() {
        return cost;
    }

    protected double getScale() {
        return scale;
    }

    protected double getScalingCost() {
        return scalingCost;
    }

    protected double getBreakChance() {
        return breakChance;
    }

    protected boolean requiresReference() {
        return false;
    }

    public int getCost(ItemStack mainRune) {
        return cost;
    }

    public double getScale(ItemStack mainRune) {
        return scale;
    }

    public double getScalingCost(ItemStack mainRune) {
        return scalingCost;
    }

    public double getBreakChance(ItemStack mainRune) {
        return breakChance;
    }

    public boolean requiresReference(ItemStack mainRune) {
        return false;
    }
}
