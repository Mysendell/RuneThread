package io.github.runethread.recipes;

// CustomShapelessRecipeSerializer.java

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class CustomShapelessRecipeSerializer implements RecipeSerializer<CustomShapelessRecipe> {

    @Override
    public CustomShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
        JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
        List<ItemStack> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientsArray.size(); i++) {
            JsonObject ingredientObj = ingredientsArray.get(i).getAsJsonObject();
            Ingredient ingredient = Ingredient.fromJson(ingredientObj);
            ingredients.add(ingredient.getItems().length > 0 ? ingredient.getItems()[0] : ItemStack.EMPTY);
        }

        ItemStack result = ItemStack.fromJson(GsonHelper.getAsJsonObject(json, "result"));

        return new CustomShapelessRecipe(id, result, ingredients);
    }

    @Override
    public CustomShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        int count = buffer.readInt();
        List<ItemStack> ingredients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ingredients.add(buffer.readItem());
        }

        ItemStack output = buffer.readItem();
        return new CustomShapelessRecipe(id, output, ingredients);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CustomShapelessRecipe recipe) {
        buffer.writeInt(recipe.ingredients.size());
        for (ItemStack ingredient : recipe.ingredients) {
            buffer.writeItem(ingredient);
        }
        buffer.writeItem(recipe.output);
    }
}

