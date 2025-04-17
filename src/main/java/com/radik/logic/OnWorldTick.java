package com.radik.logic;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.radik.Radik.SERVER;
import static com.radik.Radik.command;
import static com.radik.commands.MinigamesCommand.*;

public class OnWorldTick {

    protected static void register() {
        ServerTickEvents.END_WORLD_TICK.register(OnWorldTick::onTick);
    }

    private static void onTick(@NotNull ServerWorld serverWorld) {
        List<ServerPlayerEntity> players = SERVER.getPlayerManager().getPlayerList();

        if (FLOOR_IS_LAVA_EVENT) {
            if(FLOOR_IS_LAVA_LVL == 315) {
                FLOOR_IS_LAVA_EVENT = false;
                FLOOR_IS_LAVA_TICK = 0;
                FLOOR_IS_LAVA_LVL = -64;
                MINIGAME = false;
                for(ServerPlayerEntity q: players) {
                    q.sendMessage(Text.literal("Этап X: подъём лавы завершен.\nДлительность: ∞"));
                }
            }
            else if(++FLOOR_IS_LAVA_TICK % 20 == 0 && FLOOR_IS_LAVA_TICK >= 20) {
                for (int i : FLOOR_IS_LAVA_INFO.keySet().stream().toList().stream().sorted().toList()) {
                    if (FLOOR_IS_LAVA_LVL <= i && new ArrayList<>(FLOOR_IS_LAVA_INFO.get(i).keySet()).getFirst() <= FLOOR_IS_LAVA_TICK) {
                        command(String.format("title @a actionbar \"%sLAVA LEVEL: %d\"", FLOOR_IS_LAVA_INFO.get(i).get(FLOOR_IS_LAVA_TICK), FLOOR_IS_LAVA_LVL));
                        command(String.format("fill -64 %d -64 63 %d 63 lava", ++FLOOR_IS_LAVA_LVL, FLOOR_IS_LAVA_LVL));
                        FLOOR_IS_LAVA_TICK = 0;

                        String message = "";
                        switch (FLOOR_IS_LAVA_LVL) {
                            case -63:
                                message = "§1§lЭтап II: начало.\nДлительность: 256 секунд";
                                break;
                            case 0:
                                message = "§b§lЭтап III: стремление.\nХодите в шахту осторожно.\nДлительность: 240 секунд";
                                break;
                            case 40:
                                message = "§2§lЭтап IV: неожиданность.\nВыдано х5 маленьких подарков.\nДлительность: 600 секунд";
                                command("give @a radik:present_small 5");
                                break;
                            case 80:
                                message = "§e§lЭтап V: вытеснение.\nПосле смерти вы не сможете продолжить играть.\nДлительность: 150 секунд";
                                break;
                            case 110:
                                message = "§6§lЭтап VI: страх.\nВыдано x5 средних подарков.\nДлительность: 120 секунд";
                                command("give @a radik:present_medium 5");
                                break;
                            case 150:
                                message = "§c§lЭтап VII: боль.\nВыдано x5 больших подарков. Возможно включение пвп.\nДлительность: 100 секунд";
                                command("give @a radik:present_big 5");
                                break;
                            case 200:
                                message = "§4§lЭтап VIII: вражда.\nВыдано x5 подарков каждого типа.\nДлительность: 75 секунд";
                                command("give @a radik:present_small 5");
                                command("give @a radik:present_medium 5");
                                command("give @a radik:present_big 5");
                                command("give @a radik:present_winter 5");
                                command("give @a radik:present_instrument 5");
                                break;
                            case 275:
                                message = "§0§lЭтап IX: конец?...\nВыдано x16 хлеба и x64 блоков\nДлительность: 80 секунд";
                                command("give @a radik:winter_stone_10 64");
                                command("give @a bread 16");
                        }
                        if(!message.isEmpty()) {
                            System.out.println("OK");
                            for(ServerPlayerEntity q: players) {
                                q.sendMessage(Text.literal(message));
                            }
                        }
                        break;
                    }
                    else if(FLOOR_IS_LAVA_LVL <= i) { break; }
                }
            }
        }
    }
}
