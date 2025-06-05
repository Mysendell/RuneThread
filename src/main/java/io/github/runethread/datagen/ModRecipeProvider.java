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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider implements DataProvider {
    private final PackOutput output;

    public ModRecipeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path path = output.getOutputFolder().resolve("data").resolve("runethread").resolve("recipe");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        futures.add(saveRecipe(cache, "dough", createArcaneRecipeJson(
                3, 3,
                List.of(
                        "minecraft:spider_eye", "minecraft:rabbit", "minecraft:spider_eye",
                        "minecraft:rabbit", "minecraft:bone", "minecraft:rabbit",
                        "minecraft:rabbit", "minecraft:rabbit", "minecraft:rabbit"
                ),
                List.of(
                        Map.of("item", "runethread:dough", "count", 3)
                )
                ,"arcane_shaped"
        ), path.resolve("dough.json")));

        futures.add(saveRecipe(cache, "cake", createArcaneRecipeJson(
                3, 2,
                List.of(
                        "runethread:dough",
                        "minecraft:milk_bucket",
                        "minecraft:egg",
                        "minecraft:egg",
                        "minecraft:sugar",
                        "minecraft:sugar"
                ),
                List.of(
                        Map.of("item", "runethread:cake", "count", 1)
                ),
                "arcane_shapeless"
        ), path.resolve("cake.json")));

        futures.add(saveRecipe(cache, "hampter", createPhilosphalFurnaceRecipe(
                1, 1,
                List.of(
                        "runethread:dough"
                ),
                List.of(
                        Map.of("item", "runethread:hampter", "count", 1)
                ),
                "animator",
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

    private static JsonElement createArcaneRecipeJson(
            int width, int height, List<String> ingredients, List<Map<String, Object>> results, String type) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "runethread:"+ type);
        json.addProperty("width", width);
        json.addProperty("height", height);

        JsonArray ingArr = new JsonArray();
        for (String s : ingredients) ingArr.add(s);
        json.add("ingredients", ingArr);

        JsonArray resArr = new JsonArray();
        for (Map<String, Object> res : results) {
            JsonObject resObj = new JsonObject();
            resObj.addProperty("item", (String) res.get("item"));
            resObj.addProperty("count", (int) res.get("count"));
            resArr.add(resObj);
        }
        json.add("results", resArr);

        return json;
    }

    private static JsonElement createPhilosphalFurnaceRecipe(
            int width, int height, List<String> ingredients, List<Map<String, Object>> results, String type,
    int burnTime, int fuelBurnMultiplier) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "runethread:"+ type);
        json.addProperty("width", width);
        json.addProperty("height", height);

        json.addProperty("burnTime", burnTime);
        json.addProperty("fuelBurnMultiplier", fuelBurnMultiplier);

        JsonArray ingArr = new JsonArray();
        for (String s : ingredients) ingArr.add(s);
        json.add("ingredients", ingArr);

        JsonArray resArr = new JsonArray();
        for (Map<String, Object> res : results) {
            JsonObject resObj = new JsonObject();
            resObj.addProperty("item", (String) res.get("item"));
            resObj.addProperty("count", (int) res.get("count"));
            resArr.add(resObj);
        }
        json.add("results", resArr);

        return json;
    }
}
