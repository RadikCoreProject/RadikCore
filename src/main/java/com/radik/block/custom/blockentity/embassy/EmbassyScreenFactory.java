package com.radik.block.custom.blockentity.embassy;

import com.radik.connecting.game.EmbassyData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmbassyScreenFactory implements ExtendedScreenHandlerFactory<EmbassyData> {
    private final EmbassyData data;
    private final Text title;

    public EmbassyScreenFactory(EmbassyData data, Text title) {
        this.data = data;
        this.title = title;
    }

    @Override
    public @NotNull EmbassyData getScreenOpeningData(ServerPlayerEntity player) {
        return data;
    }

    @Override
    public Text getDisplayName() {
        return title;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new EmbassyScreenHandler(syncId, inv, data);
    }
}
