package com.radik.item.custom;

import com.radik.client.screen.game.TeleporterScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class Teleporter extends Item {
    public Teleporter(@NotNull Settings settings, int lvl, Vec3d pos, String owner) {
        super(settings.component(TELEPORTER, lvl).component(POSITION, pos).component(OWNER, owner));
    }

    @Override
    public ActionResult use(@NotNull World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            openTeleporterScreen(user);
        }
        return ActionResult.SUCCESS;
    }

    @Environment(EnvType.CLIENT)
    private void openTeleporterScreen(@NotNull PlayerEntity player) {
        MinecraftClient.getInstance().setScreen(new TeleporterScreen(player.getStackInHand(Hand.MAIN_HAND)));
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer level = stack.get(TELEPORTER);
        String owner = stack.get(OWNER);
        if (level == null || owner == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik.capsule.level").append(String.valueOf(level + 1)));
        textConsumer.accept(Text.translatable("tooltip.radik.teleporter.max").append(getDistance((int) level)));
        textConsumer.accept(Text.translatable("tooltip.radik.teleporter.owner").append(owner));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
