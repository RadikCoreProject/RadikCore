package com.radik.block.custom.blockentity.event;

import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.client.RadikClient;
import com.radik.connecting.event.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;

public class EventBlockEntity extends BlockEntity {

    public EventBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.EVENT_BLOCK_ENTITY, pos, state);
    }

    public static ChallengeEvent getEventType() {
        int day = LocalDateTime.now().getDayOfYear();

        if (day >= 305) return ChallengeEvent.HALLOWEEN;
        if (day <= 59) return ChallengeEvent.WINTER;
        if (day >= 91 && day <= 151) return ChallengeEvent.FLOWERY;
        if (day >= 182 && day <= 223) return ChallengeEvent.HALLOWEEN;
        return ChallengeEvent.NONE;
    }

    @Environment(EnvType.CLIENT)
    public boolean claimReward(String playerName, int eventIndex) {
        if (this.world == null) return false;

        if (eventIndex <= 4 && eventIndex >= 0) {
            Eventer event = eventIndex == 4 ? RadikClient.GLOBAL_CHALLENGE : RadikClient.CHALLENGES[eventIndex];
            if (event != null && event.isCompleted() && !event.isClaimed()) {
                event.setClaimed(true);
                return true;
            } else return false;
        } else return false;
    }
}
