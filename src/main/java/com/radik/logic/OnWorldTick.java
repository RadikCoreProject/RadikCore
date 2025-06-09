package com.radik.logic;

import bot.callback.Server;
import com.radik.util.Duplet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.TitleCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.radik.Radik.*;
import static com.radik.commands.MinigamesCommand.*;

public class OnWorldTick {
    private static final ArrayList<Duplet<int[], String>> ACTIONBAR_OVERWORLD = new ArrayList<>();
    private static final ArrayList<Duplet<int[], String>> ACTIONBAR_NETHER = new ArrayList<>();
    protected static HashMap<ServerPlayerEntity, Duplet<Integer, Text>> KICK = new HashMap<>();
    private static final Duplet<Integer, Text> KICKS = new Duplet<>(0, Text.of("Disconnected by server"));

    static {
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3905, 4111, 4257, 4461}, "СССР-2"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{4128, 4169, 4158, 4204}, "СССР-2"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{4513, 4637, 4625, 4738}, "Село «Новый Саратов»"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3793, 3920, 4431, 4524}, "Г. Киев"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3904, 4607, 4513, 4673}, "Г. Киев"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3952, 4260, 4673, 4722}, "Г. Киев"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{4064, 4215, 4722, 4813}, "Г. Киев"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{4117, 4604, 4227, 4524}, "Г. Киев"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3969, 4360, 4769, 5193}, "Г. Екатеринбург"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3808, 4044, 4994, 5340}, "Г. Екатеринбург"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3712, 3850, 5011, 5194}, "Г. Екатеринбург"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3564, 3735, 4764, 4838}, "Недостроенный поселок «Буча»"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{3562, 4597, 4249, 5057}, "Киевская агломерация"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{7210, 7294, 4241, 4394}, "Исторический центр, г. Квазис"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{7280, 7320, 3920, 4063}, "Харитоновский парк"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{6944, 7503, 4224, 4512}, "Г. Квазис"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{7154, 7470, 3912, 4237}, "Г. Квазис"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{6611, 7808, 3894, 4796}, "Окрестности, г. Квазис"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-4016, -3899, -543, -295}, "Красочный остров"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-3887, -3793, -489, -387}, "Красочный остров"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-4558, -4386, -144, -2}, "Цветущий сад"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-5153, -5009, -801, -674}, "Нефритовый город"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-4382, -3795, -736, -212}, "PixLand"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-4493, -4000, -270, 91}, "PixLand"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{-5148, -3573, -932, 60}, "Окрестности, PixLand"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{22483, 22585, -4951, -4874}, "GML"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{22274, 22414, -4660, -4473}, "GSL"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{21905, 22781, -5183, -4340}, "Окрестности, GML"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{1011650, 1011779, 1011798, 1011918}, "Лярдный городок"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{1011688, 1011796, 1011963, 1012025}, "Лярдный городок"));
        ACTIONBAR_OVERWORLD.add(new Duplet<>(new int[]{1011547, 1012090, 1011748, 1012189}, "Лярдные земли"));

        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{299, 522, 556, 559}, "Ледянка_Киевская агломерация"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{538, 861, 556, 559}, "Киевская агломерация_г. Квазис"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{529, 532, 4, 551}, "Лесная_Киевская агломерация"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{529, 532, -614, -4}, "Граничная_Лесная"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{534, 2801, -619, -616}, "Граничная_Water isles"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{-525, 0, -1, 2}, "PixLand_Спавн"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{4, 526, -1, 2}, "Спавн_Лесная"));

        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{526, 535, -624, -614}, "Граничная"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{2801, 2812, -624, -614}, "Water isles"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{526, 535, -4, 4}, "Лесная"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{1, 4, -5, 1}, "Спавн"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{-528, -525, -8, 1}, "PixLand"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{-536, -517, -27, -9}, "PixLand"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{523, 538, 552, 572}, "Киевская агломерация"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{295, 299, 556, 558}, "Ледянка"));
        ACTIONBAR_NETHER.add(new Duplet<>(new int[]{862, 865, 556, 558}, "г. Квазис"));
    }

    protected static void register() {
        ServerTickEvents.END_WORLD_TICK.register(OnWorldTick::onTick);
    }

    private static void onTick(@NotNull ServerWorld serverWorld) {
        List<ServerPlayerEntity> players = SERVER.getPlayerManager().getPlayerList();

        if (!KICK.isEmpty()) {
            CompletableFuture.runAsync(() -> {
                Set<ServerPlayerEntity> players1 = new HashSet<>(KICK.keySet());

                for (ServerPlayerEntity player : players1) {
                    if (player == null || !player.isAlive() || player.isDisconnected()) {
                        KICK.remove(player);
                        continue;
                    }
                    Duplet<Integer, Text> kickData = KICK.get(player);
                    if (kickData == null || kickData.getType() == null) {
                        KICK.remove(player);
                        continue;
                    }
                    try {
                        int remainingTicks = kickData.getType();
                        Text message = kickData.getParametrize() != null ? kickData.getParametrize() : KICKS.getParametrize();

                        if (remainingTicks <= 1) {
                            LOGGER.info("Kicking player: {}", player.getName().getString());
                            player.networkHandler.disconnect(message);
                            KICK.remove(player);
                        } else {
                            KICK.put(player, new Duplet<>(remainingTicks - 1, message));
                        }
                    } catch (Exception e) {
                        LOGGER.error("Failed to process kick for player {}", player.getName().getString(), e);
                        KICK.remove(player);
                    }
                }
            });
        }

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
                for (int i : FLOOR_IS_LAVA_INFO.keySet().stream().sorted().toList()) {
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

        else if (SERVER.getTicks() % 40 == 0) {
            CompletableFuture.runAsync(() -> {
                for (ServerPlayerEntity q : players) {

                    String n = q.getWorld().getDimension().toString().split("/ ")[1].split("]")[0];
                    if (n.equals("minecraft:infiniburn_end")) continue;
                    double x = q.getX(), y = q.getY(), z = q.getZ();

                    for (Duplet<int[], String> duplet : n.equals("minecraft:infiniburn_overworld") ? ACTIONBAR_OVERWORLD : ACTIONBAR_NETHER) {
                        int[] coords = duplet.getType();
                        boolean t = false;
                        if (coords == null || duplet.getParametrize() == null) {
                            LOGGER.error("ERROR: duplet type is null {}", duplet);
                            continue;
                        }

                        switch (n) {
                            case "minecraft:infiniburn_overworld" -> {
                                if (coords[0] <= x && coords[1] >= x && coords[2] <= z && coords[3] >= z) {
//                                    command(String.format("title %s actionbar \"%s\"", q.getName().getString(), duplet.getParametrize()));
                                    t = true;
                                }
                            }
                            case "minecraft:infiniburn_nether" -> {
                                if (coords[0] <= x && coords[1] >= x && coords[2] <= z && coords[3] >= z) {
                                    String p = duplet.getParametrize();

                                    if (Math.abs(coords[0] - coords[1]) == 3 && Math.abs(y - 121) <= 3) {
                                        int fz = q.getFacing().getVector().getZ();
                                        if (fz == 0) continue;
                                        switch (fz) {
                                            case 1 -> p = String.join(" -> ", p.split("_"));
                                            case -1 -> p = String.join(" -> ", Arrays.stream(p.split("_")).toList().reversed());
                                        }
                                    } else if (Math.abs(coords[2] - coords[3]) == 3 && Math.abs(y - 121) <= 3) {
                                        int fx = q.getFacing().getVector().getX();
                                        if (fx == 0) continue;
                                        switch (fx) {
                                            case 1: p = String.join(" -> ", p.split("_"));
                                            case -1: p = String.join(" -> ", Arrays.stream(p.split("_")).toList().reversed());
                                        }
                                    } else {
                                        p = duplet.getParametrize();
                                    }
//                                    command(String.format("title %s actionbar \"%s\"", q.getName().getString(), p));
                                    t = true;
                                }
                            }
                        }
                        if (t) break;
                    }
                }
            });
        }
    }
}
