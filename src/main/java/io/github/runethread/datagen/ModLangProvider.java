package io.github.runethread.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static io.github.runethread.RuneThread.MODID;
import static io.github.runethread.customitems.CustomItems.simpleItems;

public class ModLangProvider implements DataProvider {
    private final PackOutput output;

    public ModLangProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path path = output.getOutputFolder().resolve("assets/runethread/lang/en_us.json");
        JsonObject json = new JsonObject();
        for(DeferredItem<Item> item : simpleItems) {
            String name = item.getRegisteredName();
            name = name.replace(MODID+":", "");
            String key = "item.runethread." + name;
            StringBuilder translation = new StringBuilder();

            String[] nameParts = name.split("_");
            for(String part : nameParts) {
                char firstChar = part.charAt(0);
                translation.append(part.replaceFirst(Character.toString(firstChar), Character.toString(Character.toUpperCase(firstChar))));
                translation.append(" ");
            }

            json.addProperty(key, translation.toString().trim());
        }
        Path expectionsPath = output.getOutputFolder().resolve("../../main/resources/temp/lang/en_us.json");
        if(Files.notExists(expectionsPath))
            return DataProvider.saveStable(cache, json, path);
        Reader reader;
        try {
            reader = Files.newBufferedReader(expectionsPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonElement extraJson = JsonParser.parseReader(reader);
        if(extraJson.isJsonObject()) {
            JsonObject extra = extraJson.getAsJsonObject();
            for(String key : extra.keySet()) {
                json.add(key, extra.get(key));
            }
        } else {
            throw new RuntimeException("Expected a JSON object in " + expectionsPath);
        }
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Runethread Lang Provider";
    }
}
