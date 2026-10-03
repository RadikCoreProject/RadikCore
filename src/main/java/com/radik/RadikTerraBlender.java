package com.radik;

import com.radik.block.RegisterBlocks;
import com.radik.world.biome.GingerForestRegion;
import com.radik.world.biome.RegisterBiomes;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class RadikTerraBlender implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Radik.LOGGER.info("RadikTerraBlender загружен!");
        Regions.register(new GingerForestRegion());

        MaterialRules.MaterialCondition isSurface = MaterialRules.surface();
        MaterialRules.MaterialCondition isAboveWater = MaterialRules.water(-1, 0);

        MaterialRules.MaterialRule gingerForestRule = MaterialRules.condition(
            MaterialRules.biome(RegisterBiomes.GINGER_FOREST),
            MaterialRules.sequence(
                MaterialRules.condition(
                    MaterialRules.STONE_DEPTH_FLOOR,
                    MaterialRules.condition(
                        isSurface,
                        MaterialRules.condition(
                            isAboveWater,
                            MaterialRules.sequence(
                                MaterialRules.block(RegisterBlocks.RADIOACTIVE_GRASS.getDefaultState()),
                                MaterialRules.block(Blocks.DIRT.getDefaultState())
                            )
                        )
                    )
                ),

                MaterialRules.condition(
                    isSurface,
                    MaterialRules.condition(
                        MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                        MaterialRules.block(Blocks.DIRT.getDefaultState())
                    )
                )
            )
        );

        SurfaceRuleManager.addSurfaceRules(
            SurfaceRuleManager.RuleCategory.OVERWORLD,
            Radik.MOD_ID,
            gingerForestRule
        );
    }


}