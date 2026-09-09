package io.github.runethread.customitems.runes;

import io.github.runethread.customitems.runes.functionrunes.RuneFunction;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.ScaleData;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static io.github.runethread.util.runeutils.ModifierRune.SCALE_RUNE;

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

    public double calculateScale(double energy, @Nullable ModifierMap modifiers, ItemStack mainRune){
        double maxScale = Math.floor(1 + (Math.log(energy / getCost(mainRune)) / Math.log(getScalingCost(mainRune))));

        if (modifiers != null && modifiers.hasModifier(SCALE_RUNE)) {
            ItemStack scaleRuneStack = modifiers.get(SCALE_RUNE.getName());
            double scale = scaleRuneStack.getOrDefault(DataComponentRegistry.SCALE_DATA, new ScaleData(0, ScaleData.Mode.SCALE)).scale();
            if (scale >= 1)
                maxScale = Math.min(maxScale, scale);
            else if (scale > 0)
                maxScale = maxScale * scale;
        }

        return maxScale;
    }

    public double calculateCost(double scale, ItemStack mainRune){
        return getCost(mainRune) * Math.pow(getScalingCost(mainRune), scale - 1);
    }

    public boolean requiresReference(ItemStack mainRune) {
        return false;
    }
}
