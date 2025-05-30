package io.github.runethread.customentities.cakegolem;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

import static io.github.runethread.RuneThread.MODID;

public class CakeGolemRenderer extends MobRenderer<CakeGolem, LivingEntityRenderState, CakeGolemModel> {

    public CakeGolemRenderer(EntityRendererProvider.Context p_174304_, CakeGolemModel p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }

    public CakeGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CakeGolemModel(context.bakeLayer(CakeGolemModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/cake_golem.png");
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
