package io.github.runethread.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.runethread.util.Barrier;
import io.github.runethread.util.BarrierManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

public class BarrierRenderManager {
    public static void renderBarriers(PoseStack poseStack, Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        LevelRenderer levelRenderer = mc.levelRenderer;

        // Example: Render all active barriers
        List<Barrier> barriers = BarrierManager.getActiveBarriers();
        synchronized (barriers) {
            for (Barrier barrier : BarrierManager.getActiveBarriers()) {
                BlockPos center = barrier.getCenter();
                int radius = (int) barrier.getRadius();
                Vec3 camPos = camera.getPosition();

                double minX = center.getX() - radius - camPos.x;
                double minY = center.getY() - radius - camPos.y;
                double minZ = center.getZ() - radius - camPos.z;
                double maxX = center.getX() + radius + 1 - camPos.x;
                double maxY = center.getY() + radius + 1 - camPos.y;
                double maxZ = center.getZ() + radius + 1 - camPos.z;

                RenderSystem.lineWidth(3.0F);
                float r = 1.0F, g = 0.0F, b = 0.0F;

                VertexConsumer vcLine = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                drawCubeEdges(vcLine, poseStack,
                        minX, minY, minZ,
                        maxX, maxY, maxZ,
                        r, g, b, 0.7F);

                VertexConsumer vc = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.translucent());
                drawCube(vc, poseStack,
                        minX, minY, minZ,
                        maxX, maxY, maxZ,
                        r, g, b, 0.3F);


                Minecraft.getInstance().renderBuffers().bufferSource().endBatch(RenderType.lines());
            }
        }
    }

    private static void drawCubeEdges(VertexConsumer builder, PoseStack poseStack,
                                      double minX, double minY, double minZ,
                                      double maxX, double maxY, double maxZ,
                                      float r, float g, float b, float a){
        drawLine(builder, poseStack, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        drawLine(builder, poseStack, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        drawLine(builder, poseStack, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        drawLine(builder, poseStack, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        drawLine(builder, poseStack, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        drawLine(builder, poseStack, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        drawLine(builder, poseStack, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        drawLine(builder, poseStack, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        drawLine(builder, poseStack, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        drawLine(builder, poseStack, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        drawLine(builder, poseStack, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        drawLine(builder, poseStack, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    private static void drawLine(VertexConsumer builder, PoseStack poseStack, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();
        builder.addVertex(matrix, (float) x1, (float) y1, (float) z1)
                .setColor(r, g, b, a)
                .setNormal(0, 1, 0);
        builder.addVertex(matrix, (float) x2, (float) y2, (float) z2)
                .setColor(r, g, b, a)
                .setNormal(0, 1, 0);
    }

    private static void drawCube(VertexConsumer builder, PoseStack poseStack,
                                 double minX, double minY, double minZ,
                                 double maxX, double maxY, double maxZ,
                                 float r, float g, float b, float a) {
        // Normals for each face
        float[][] normals = {
                { 0,  1,  0}, // up
                { 0, -1,  0}, // down
                { 0,  0, -1}, // north
                { 0,  0,  1}, // south
                {-1,  0,  0}, // west
                { 1,  0,  0}  // east
        };

        Matrix4f matrix = poseStack.last().pose();

        addQuad(builder, matrix, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a, normals[0], 0.0f, 0.0f);
        addQuad(builder, matrix, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b, a, new float[]{-normals[0][0], -normals[0][1], -normals[0][2]}, 0.0f, 0.0f);

        addQuad(builder, matrix, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, r, g, b, a, normals[1], 0.0f, 0.0f);
        addQuad(builder, matrix, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, minX, minY, minZ, r, g, b, a, new float[]{-normals[1][0], -normals[1][1], -normals[1][2]}, 0.0f, 0.0f);

        addQuad(builder, matrix, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b, a, normals[2], 0.0f, 0.0f);
        addQuad(builder, matrix, minX, maxY, minZ, maxX, maxY, minZ, maxX, minY, minZ, minX, minY, minZ, r, g, b, a, new float[]{-normals[2][0], -normals[2][1], -normals[2][2]}, 0.0f, 0.0f);

        addQuad(builder, matrix, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ, r, g, b, a, normals[3], 0.0f, 0.0f);
        addQuad(builder, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, minX, minY, maxZ, r, g, b, a, new float[]{-normals[3][0], -normals[3][1], -normals[3][2]}, 0.0f, 0.0f);

        addQuad(builder, matrix, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ, r, g, b, a, normals[4], 0.0f, 0.0f);
        addQuad(builder, matrix, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, minX, minY, minZ, r, g, b, a, new float[]{-normals[4][0], -normals[4][1], -normals[4][2]}, 0.0f, 0.0f);

        addQuad(builder, matrix, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, r, g, b, a, normals[5], 0.0f, 0.0f);
        addQuad(builder, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, r, g, b, a, new float[]{-normals[5][0], -normals[5][1], -normals[5][2]}, 0.0f, 0.0f);
    }

    // Add a quad (4 vertices)
    private static void addQuad(VertexConsumer builder, Matrix4f matrix,
                                double x1, double y1, double z1,
                                double x2, double y2, double z2,
                                double x3, double y3, double z3,
                                double x4, double y4, double z4,
                                float r, float g, float b, float a,
                                float[] normal,
                                float u, float v) {

        builder.addVertex(matrix, (float)x1, (float)y1, (float)z1).setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(normal[0], normal[1], normal[2]);
        builder.addVertex(matrix, (float)x2, (float)y2, (float)z2).setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(normal[0], normal[1], normal[2]);
        builder.addVertex(matrix, (float)x3, (float)y3, (float)z3).setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(normal[0], normal[1], normal[2]);
        builder.addVertex(matrix, (float)x4, (float)y4, (float)z4).setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(normal[0], normal[1], normal[2]);
    }
}

