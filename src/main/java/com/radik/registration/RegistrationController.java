package com.radik.registration;

import com.radik.ModGroup;
import com.radik.ModTags;
import com.radik.Music;
import com.radik.Radik;
import com.radik.behavior.RegisterBehaviors;
import com.radik.block.RegisterBlocks;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.commands.RegisterCommands;
import com.radik.avancements.criteries.Criterias;
import com.radik.effect.RegisterEffect;
import com.radik.fluid.RegisterFluids;
import com.radik.item.RegisterItems;
import com.radik.logic.LogicInitialize;
import com.radik.packets.PacketRegistration;
import com.radik.world.WorldGenRegister;

public class RegistrationController {
    public static void init() {
        if (Radik.enablePackets) PacketRegistration.initialize();
        Criterias.initialize();
        RegisterItems.initialize();
        RegisterBehaviors.initialize();
        RegisterBlocks.initialize();
        Music.initialize();
        ModTags.initialize();
        ModGroup.initialize();
        LogicInitialize.initialize();
        RegisterCommands.initialize();
        RegisterFluids.initialize();
        BlockEntities.initialize();
        RegisterEffect.initialize();

        // Генерация озер с водородом/гелием
        WorldGenRegister.initialize();
    }
}
