package com.radik.item.custom.weapon;

import com.radik.item.custom.projectile.Bullet;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class Magazine extends Item {
    public Magazine(@NotNull Settings settings) {
        super(settings.maxCount(16).component(STORAGE, 0).component(BULLET_TYPE, null));
    }

    public boolean onClicked(@NotNull ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        Integer c = stack.get(STORAGE);
        Item item = otherStack.getItem();
        if (c == null || c == 100 || !(item instanceof Bullet)) return false;
        Bullet.BulletType type = stack.get(BULLET_TYPE);
        Bullet.BulletType typeOther = otherStack.get(BULLET_TYPE);
        if (type != null && type != otherStack.get(BULLET_TYPE)) return false;
        int sc = stack.getCount();

        int stackCount = otherStack.getCount();
        int p = Math.min((100 - c) * sc, stackCount);
        stack.set(STORAGE, c + (p / sc));
        if (type == null) stack.set(BULLET_TYPE, typeOther);
        cursorStackReference.set(item.getDefaultStack().copyWithCount(stackCount - p + p % sc));
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer count = stack.get(STORAGE);
        Bullet.BulletType typ = stack.get(BULLET_TYPE);
        if (count == null) return;

        textConsumer.accept(Text.of((count == 100 ? "§2" : "§4") + count + " / 100"));
        if (typ != null) textConsumer.accept(Text.translatable("tooltip.radik.bullet." + typ.name().toLowerCase()));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
