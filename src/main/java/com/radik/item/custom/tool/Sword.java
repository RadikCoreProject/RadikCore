package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.property.base.EventProperty;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;
import static com.radik.property.base.BaseProperties.EVENT_PROPERTY;

/**
 * <b>1.4.3</b>
 * <p>Now tooltip about event tools are in client with other items, no hardcoding</p>
 * <b>To use tooltip use this class:</b>
 * @see Tools
 */
public class Sword extends Item implements Tools {
    private static Set<EntityType<?>> FOREST_ENTITIES = new HashSet<>();

    static {
        FOREST_ENTITIES.add(EntityType.IRON_GOLEM);
        FOREST_ENTITIES.add(EntityType.SLIME);
        FOREST_ENTITIES.add(EntityType.BOGGED);
        FOREST_ENTITIES.add(EntityType.WARDEN);
        FOREST_ENTITIES.add(EntityType.MOOSHROOM);
        FOREST_ENTITIES.add(EntityType.CREEPER);
    }

    // pls dont use this idiot
    @Deprecated
    public Sword(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings, ChallengeEvent event) {
        super(settings.sword(material, attackDamage, attackSpeed).component(EVENT_TYPE, event).component(BOOL, false));
    }

    public Sword(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.sword(material, attackDamage, attackSpeed).component(BOOL, false));
    }

    /**
     * <b>Flowery event logic is inside mixin class</b>
     * @see com.radik.mixin.entity.PlayerEntityMixin
     */
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof ServerPlayerEntity p && Boolean.TRUE.equals(stack.get(BOOL))) {
            ServerWorld world = p.getEntityWorld();
            if (!world.isClient()) {
                switch (stack.get(EVENT_TYPE)) {
                    case HALLOWEEN -> {
                        if (target instanceof PlayerEntity) return;
                        if (Radik.RANDOM.nextInt(1, EVENT_PROPERTY.getInt(EventProperty.YELLOW_CANDY_DROP_CHANCE) + 1) == 1) {
                            world.spawnEntity(new ItemEntity(world, target.getX(), target.getY(), target.getZ(),
                                new ItemStack(RegisterItems.SNOWFLAKE, Radik.RANDOM.nextInt(1, 5))));
                            Radik.sendEventToPlayers(0, target.getBlockPos(), 0, world);
                        }
                    }
                    // for future 1.5
                    case WINTER -> {}
                    case SUMMER -> {
                        if (target instanceof CreakingEntity) {
                            target.kill(world);
                            stack.damage(19, p);
                        }
                    }
                    case null, default -> {}
                }
            }
        }
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        ItemStack stack = damageSource.getWeaponStack();
        return Tools.activedPower(stack) &&
                Tools.toolType(stack) == ChallengeEvent.SUMMER &&
                FOREST_ENTITIES.contains(target.getType()) ? baseAttackDamage : super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }


    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Tools.appendTooltip(stack, textConsumer, Tool.SWORD);
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
