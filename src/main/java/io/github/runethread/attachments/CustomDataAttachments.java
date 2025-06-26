package io.github.runethread.attachments;

import io.github.runethread.RuneThread;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CustomDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RuneThread.MODID);

    public static final Supplier<AttachmentType<BarrierTracker>> BARRIER_TRACKER_STORAGE =
            ATTACHMENT_TYPES.register("entity_storage",
                    () -> AttachmentType.serializable(BarrierTracker::new)
                            .copyOnDeath()
                            .build());
}
