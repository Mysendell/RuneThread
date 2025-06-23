package io.github.runethread.customentities.cakegolem;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

import static io.github.runethread.customentities.cakegolem.CakeGolemAnimation.cake_golem_walk;

public class CakeGolemModel extends EntityModel<LivingEntityRenderState> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("runethread","textures/entities/cake_golem"), "main");
    /*private final ModelPart left_leg;
    private final ModelPart right_leg;
    private final ModelPart torso;
    private final ModelPart gut;
    private final ModelPart right_arm;
    private final ModelPart left_arm;*/

    public CakeGolemModel(ModelPart root) {
        super(root);
        /*this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
        this.torso = root.getChild("torso");
        this.gut = root.getChild("gut");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");*/
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(52, 14).addBox(-4.0F, -9.0F, -1.0F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 24.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(52, 0).addBox(-4.0F, -9.0F, -1.0F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 24.0F, 0.0F));

        PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -20.0F, -1.0F, 16.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 15.0F, -3.0F));

        PartDefinition gut = partdefinition.addOrReplaceChild("gut", CubeListBuilder.create().texOffs(0, 30).addBox(-13.0F, -16.0F, -2.0F, 14.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 15.0F, -7.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 30).addBox(-4.0F, -24.0F, -1.0F, 5.0F, 24.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 18.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 52).addBox(-4.0F, -24.0F, -1.0F, 5.0F, 24.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, 18.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        this.resetPose();
        this.animateWalk(
                cake_golem_walk, // your AnimationDefinition
                state.walkAnimationPos * 5.5f,
                state.walkAnimationSpeed * 150f,
                1.0f,
                1.0f
        );
    }
}
