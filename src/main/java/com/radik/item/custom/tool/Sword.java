package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.property.EventProperties;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

public class Sword extends Item implements Tools {
    public Sword(ToolMaterial material, float attackDamage, float attackSpeed, Item.@NotNull Settings settings) {
        super(settings.sword(material, attackDamage, attackSpeed));
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(target instanceof PlayerEntity) && (attacker instanceof PlayerEntity) && (target instanceof LivingEntity) && Boolean.TRUE.equals(stack.get(BOOL))) {
            if (Radik.RANDOM.nextInt(1, EventProperties.get("yellow_candy_drop_chance") + 1) == 1) {
                ServerWorld world = (ServerWorld) target.getWorld();
                world.spawnEntity(new ItemEntity(world, target.getX(), target.getY(), target.getZ(), new ItemStack(RegisterItems.CANDY_YELLOW, 1)));
                Radik.sendEventToPlayers(0, target.getBlockPos(), 0, world);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        if (event == null || bool == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik." + event.name().toLowerCase()));
        textConsumer.accept(Text.translatable("tooltip.radik.instrument." + event.name().toLowerCase() + ".sword"));
        textConsumer.accept(Text.translatable("tooltip.radik.super").append(Text.translatable("tooltip.radik." + bool)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
