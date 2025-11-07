package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class Axe extends AxeItem implements Tools {
    public Axe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void postDamageEntity(@NotNull ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (Boolean.TRUE.equals(stack.get(BOOL))) {
            if (Radik.RANDOM.nextInt(1, 100) == 1) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1, true, false));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 255, true, false));
                Radik.sendEventToPlayers(0, target.getBlockPos().up(), 0, (ServerWorld) target.getWorld());
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
        textConsumer.accept(Text.translatable("tooltip.radik.instrument." + event.name().toLowerCase() + ".axe"));
        textConsumer.accept(Text.translatable("tooltip.radik.super").append(Text.translatable("tooltip.radik." + bool)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
