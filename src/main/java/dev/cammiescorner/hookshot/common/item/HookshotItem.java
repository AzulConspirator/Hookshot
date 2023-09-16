package dev.cammiescorner.hookshot.common.item;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import dev.cammiescorner.hookshot.core.integration.HookshotConfig;
import dev.cammiescorner.hookshot.core.registry.ModEntities;
import dev.cammiescorner.hookshot.core.util.PlayerProperties;
import dev.cammiescorner.hookshot.core.util.UpgradesHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class HookshotItem extends Item implements DyeableItem 
{	
	public static final int DEFAULT_COLOR = 16777215;

	public HookshotItem() {
		super(new Item.Settings().maxCount(1).maxDamage(HookshotConfig.defaultMaxDurability));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(this));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(!world.isClient) {
			if(!((PlayerProperties) user).hasHook()) {
				double maxRange = HookshotConfig.defaultMaxRange * (UpgradesHelper.hasRangeUpgrade(stack) ? HookshotConfig.rangeMultiplier : 1);
				double maxSpeed = HookshotConfig.defaultMaxSpeed * (UpgradesHelper.hasQuickUpgrade(stack) ? HookshotConfig.quickMultiplier : 1);

				HookshotEntity hookshot = new HookshotEntity(ModEntities.HOOKSHOT_ENTITY, user, world);
				hookshot.setProperties(stack, maxRange, maxSpeed, user.getPitch(), user.getYaw(), 0f, 1.5f * (float) (maxSpeed / 10));
				world.spawnEntity(hookshot);
			}

			if(!HookshotConfig.useClassicHookshotLogic) {
				user.setCurrentHand(hand);
				((PlayerProperties) user).setHasHook(true);
			}
			else {
				((PlayerProperties) user).setHasHook(!((PlayerProperties) user).hasHook());
			}
		}

		if(!((PlayerProperties) user).hasHook())
			world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1F, 1F);

		return super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if(!HookshotConfig.useClassicHookshotLogic)
			((PlayerProperties) user).setHasHook(false);

		return super.finishUsing(stack, world, user);
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if(!HookshotConfig.useClassicHookshotLogic)
			((PlayerProperties) user).setHasHook(false);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Registries.ITEM.get(new Identifier(HookshotConfig.hookshotRepairItem));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if(UpgradesHelper.hasDurabilityUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.durability").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasAutomaticUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.automatic").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasSwingingUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.swinging").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasAquaticUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.aquatic").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasEndericUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.enderic").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasQuickUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.quick").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasRangeUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.range").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasBleedUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.bleed").formatted(Formatting.GRAY));
		if(UpgradesHelper.hasVineUpgrade(stack))
			tooltip.add(Text.translatable(Hookshot.MOD_ID + ".modifier.vinelike").formatted(Formatting.GRAY));
	}

	@Override
	public Text getName(ItemStack stack) {
		boolean hasModifiers = UpgradesHelper.hasAquaticUpgrade(stack) || UpgradesHelper.hasEndericUpgrade(stack) || UpgradesHelper.hasQuickUpgrade(stack) || UpgradesHelper.hasRangeUpgrade(stack) || UpgradesHelper.hasAutomaticUpgrade(stack) || UpgradesHelper.hasBleedUpgrade(stack) || UpgradesHelper.hasSwingingUpgrade(stack) || UpgradesHelper.hasDurabilityUpgrade(stack);

		return hasModifiers ? super.getName(stack).copy().formatted(Formatting.AQUA) : super.getName(stack);
	}
}
