package io.github.runethread.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomShapedRecipeSerializer implements RecipeSerializer<CustomShapedRecipe> {

    public CustomShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
        JsonObject key = GsonHelper.getAsJsonObject(json, "key");
        JsonArray pattern = GsonHelper.getAsJsonArray(json, "pattern");
        JsonObject result = GsonHelper.getAsJsonObject(json, "result");

        int height = pattern.size();
        int width = pattern.get(0).getAsString().length();

        List<ItemStack> ingredients = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            String row = pattern.get(y).getAsString();
            for (int x = 0; x < row.length(); x++) {
                char keyChar = row.charAt(x);
                JsonObject keyObj = GsonHelper.getAsJsonObject(key, String.valueOf(keyChar));
                Ingredient ingredient = Ingredient.fromJson(keyObj);
                ingredients.add(ingredient.isEmpty() ? ItemStack.EMPTY : ingredient.getItems()[0]);
            }
        }

        ItemStack output = ShapedRecipe.itemStackFromJson(result);


        return new CustomShapedRecipe(id, output, width, height, ingredients);
    }

    public CustomShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        int width = buffer.readInt();
        int height = buffer.readInt();
        int count = buffer.readInt();

        List<ItemStack> ingredients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ingredients.add(buffer.readItemStack());
        }

        ItemStack output = buffer.readItemStack();
        return new CustomShapedRecipe(id, output, width, height, ingredients);
    }

    public void toNetwork(FriendlyByteBuf buffer, CustomShapedRecipe recipe) {
        buffer.writeInt(recipe.width);
        buffer.writeInt(recipe.height);
        buffer.writeInt(recipe.ingredients.size());
        for (ItemStack ingredient : recipe.ingredients) {
            buffer.writeItemStack(ingredient);
        }
        buffer.writeItemStack(recipe.output);
    }
}