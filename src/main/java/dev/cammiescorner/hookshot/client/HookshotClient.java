package dev.cammiescorner.hookshot.client;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.client.entity.model.HookshotEntityModel;
import dev.cammiescorner.hookshot.client.entity.renderer.HookshotEntityRenderer;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import static dev.cammiescorner.hookshot.core.registry.ModItems.*;

@Environment(EnvType.CLIENT)
public class HookshotClient implements ClientModInitializer {
	public static final EntityModelLayer HOOKSHOT = new EntityModelLayer(new Identifier(Hookshot.MOD_ID, "hookshot"), "hookshot");

	@Override
	public void onInitializeClient() {
		// Entity Renderer Registry
		EntityRendererRegistry.register(ModEntities.HOOKSHOT_ENTITY, HookshotEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(HOOKSHOT, HookshotEntityModel::getTexturedModelData);

		// Colour Registry
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> getcolor(stack, tintIndex),HOOKSHOT_TOOL);

		// Predicate Registry
		ModelPredicateProviderRegistry.register(new Identifier(Hookshot.MOD_ID, "has_hook"), (stack, world, entity, seed) -> {
			if(entity instanceof PlayerEntity) {
				if(((PlayerProperties) entity).hasHook())
					return 1;
				else
					return 0;
			}

			return 0;
		});
	}
	private int getcolor(ItemStack stack, int tintIndex)
	{
		int colorvalue;
		//float[] rgb = {255f,255f,255f};
		if (tintIndex == 0 && stack.getSubNbt("display") != null)
		{
			colorvalue = stack.getOrCreateSubNbt("display").getInt("color");
			return colorvalue != 0 ? colorvalue:-1;
		}
		return -1;
	}
}
