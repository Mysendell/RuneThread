package io.github.runethread;

import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class ModelTemplate extends net.minecraft.client.data.models.model.ModelTemplate {
    public static final TextureSlot BASE = TextureSlot.create("base", TextureSlot.ALL);

    // Assume there exists some model 'examplemod:block/example_template'
    public static final ModelTemplate EXAMPLE_TEMPLATE = new ModelTemplate(
            // The parent model location
            Optional.of(
                    ModelLocationUtils.decorateBlockModelLocation("runethread:example_template")
            ),
            // The suffix to apply to the end of any model that uses this template
            Optional.of("_example"),
            // All texture slots that must be defined
            // Should be as specific as possible based on what's undefined in the parent model
            TextureSlot.PARTICLE,
            BASE
    );

    public ModelTemplate(Optional<ResourceLocation> model, Optional<String> suffix, TextureSlot... requiredSlots) {
        super(model, suffix, requiredSlots);
    }
}
