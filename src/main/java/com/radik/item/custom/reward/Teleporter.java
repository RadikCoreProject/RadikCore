package com.radik.item.custom.reward;

import com.radik.client.screen.game.TeleporterScreen;
import com.radik.util.Duplet;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

import static com.radik.Data.*;

public class Teleporter extends Item {
    public Teleporter(@NotNull Settings settings, int lvl, Vec3d pos, String owner) {
        super(settings.component(TELEPORTER, lvl).component(POSITION, pos).component(OWNER, owner));
    }

    @Override
    public ActionResult use(@NotNull World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) openTeleporterScreen(user);
        return ActionResult.SUCCESS;
    }

    @Environment(EnvType.CLIENT)
    private void openTeleporterScreen(@NotNull PlayerEntity player) {
        MinecraftClient.getInstance().setScreen(new TeleporterScreen(player.getStackInHand(Hand.MAIN_HAND)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer level = stack.get(TELEPORTER);
        String owner = stack.get(OWNER);
        if (level == null || owner == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik.capsule.level").append(String.valueOf(level + 1)));
        textConsumer.accept(Text.translatable("tooltip.radik.teleporter.max").append(getDistance((int) level)));
        textConsumer.accept(Text.translatable("tooltip.radik.owner").append(owner));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Duplet<Integer, Boolean> calculateCooldown(@NotNull ItemStack stack, @NotNull Vec3d pos1, Vec3d pos2, World world) {
        Integer type = stack.get(TELEPORTER);
        double distance = pos1.distanceTo(pos2);
        int max = getDistance(Objects.requireNonNull(type));
        int cooldown = (int) (distance / 5.612 * getTeleporterK(type));
        if (getDimension(world).equals("nether")) { max /= 8; cooldown *= 8; }
        if (cooldown == 0) cooldown = 30;
        return new Duplet<>(cooldown, max >= distance);
    }

    @Contract(pure = true)
    private static @NotNull String getDistance(int lvl) {
        return switch (lvl) {
            case 0 -> "2.000";
            case 1 -> "6.000";
            case 2 -> "12.000";
            default -> "0";
        };
    }

    private static int getDistance(@NotNull Integer lvl) {
        switch (lvl) {
            case 0 -> {return 2000;}
            case 1 -> {return 6000;}
            case 2 -> {return 12000;}
            default -> {return 0;}
        }
    }

    @Contract(pure = true)
    private static float getTeleporterK(@NotNull Integer type) {
        switch (type) {
            case 1 -> {return 0.9F;}
            case 2 -> {return 0.75F;}
            default -> {return 1;}
        }
    }
}
