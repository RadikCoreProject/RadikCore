package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.property.EventProperties;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.radik.Data.*;

public class Axe extends AxeItem implements Tools {
    public Axe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void postDamageEntity(@NotNull ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (Boolean.TRUE.equals(stack.get(BOOL))) {
            if (Radik.RANDOM.nextInt(1, EventProperties.get("axe_stun_chance") + 1) == 1) {
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
