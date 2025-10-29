package io.github.runethread.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider implements DataProvider {
    private final PackOutput output;

    public ModRecipeProvider(PackOutput output) {
        this.output = output;
    }
    private static JsonObject ingredient(String type, String value, int count) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", type);
        obj.addProperty("value", value);
        obj.addProperty("count", count);
        return obj;
    }
    private static JsonObject ingredient(String value) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "item");
        obj.addProperty("value", value);
        obj.addProperty("count", 1);
        return obj;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path path = output.getOutputFolder().resolve("data").resolve("runethread").resolve("recipe");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        futures.add(saveRecipe(cache, "dough", createRecipeJson(
                3, 3,
                List.of(
                        ingredient("minecraft:spider_eye"),
                        ingredient("minecraft:rabbit"),
                        ingredient("minecraft:spider_eye"),
                        ingredient("minecraft:rabbit"),
                        ingredient("minecraft:bone"),
                        ingredient("minecraft:rabbit"),
                        ingredient("minecraft:rabbit"),
                        ingredient("minecraft:rabbit"),
                        ingredient("minecraft:rabbit")
                ),
                List.of(
                        ingredient("item", "runethread:dough", 3)
                )
                ,"recipe_shaped"
        ), path.resolve("dough.json")));

        futures.add(saveRecipe(cache, "cake", createRecipeJson(
                3, 2,
                List.of(
                        ingredient("runethread:dough"),
                        ingredient("minecraft:milk_bucket"),
                        ingredient("minecraft:egg"),
                        ingredient("minecraft:egg"),
                        ingredient("minecraft:sugar"),
                        ingredient("minecraft:sugar")
                ),
                List.of(
                        ingredient("runethread:cake")
                ),
                "recipe_shapeless"
        ), path.resolve("cake.json")));

        futures.add(saveRecipe(cache, "altar", createRecipeJson(
                3, 2,
                List.of(
                        ingredient("runethread:marble_bricks"),
                        ingredient("runethread:marble_bricks"),
                        ingredient("runethread:marble_bricks"),
                        ingredient("runethread:marble_bricks"),
                        ingredient("runethread:marble_bricks"),
                        ingredient("tag", "minecraft:logs", 1),
                        ingredient("runethread:marble_bricks")
                ),
                List.of(
                        ingredient("runethread:runic_altar")
                ),
                "recipe_shapeless"
        ), path.resolve("altar.json")));

        futures.add(saveRecipe(cache, "hampter", createPhilosophalFurnaceRecipe(
                1, 1,
                List.of(
                        ingredient("runethread:dough")
                ),
                List.of(
                        ingredient("runethread:hampter")
                ),
                "philosophal",
                0, 1
        ), path.resolve("hampter.json")));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<?> saveRecipe(CachedOutput cache, String name, JsonElement recipeJson, Path path) {
        return DataProvider.saveStable(cache, recipeJson, path)
                .thenRun(() -> System.out.println("[ModRecipeProvider] Wrote recipe: " + path));
    }

    @Override
    public String getName() {
        return "My Custom Recipes";
    }

    private static JsonObject createRecipeJson(
            int width, int height, List<JsonObject> ingredients, List<JsonObject> results, String type) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "runethread:"+ type);
        json.addProperty("width", width);
        json.addProperty("height", height);

        JsonArray ingArr = new JsonArray();
        for (JsonObject obj : ingredients) ingArr.add(obj);
        json.add("ingredients", ingArr);

        JsonArray resArr = new JsonArray();
        for (JsonObject res : results) resArr.add(res);
        json.add("results", resArr);

        return json;
    }

    private static JsonObject createSmeltingRecipe(
            int width, int height, List<JsonObject> ingredients, List<JsonObject> results, String type,
            int burnTime, int fuelBurnMultiplier) {
        JsonObject json = createRecipeJson(width, height, ingredients, results, type);
        json.addProperty("burnTime", burnTime);
        json.addProperty("fuelBurnMultiplier", fuelBurnMultiplier);
        return  json;
    }

    private static JsonObject createPhilosophalFurnaceRecipe(
            int width, int height, List<JsonObject> ingredients, List<JsonObject> results, String type,
    int burnTime, int fuelBurnMultiplier) {
        return  createSmeltingRecipe(
                width, height, ingredients, results, type,
                burnTime, fuelBurnMultiplier);
    }
}
