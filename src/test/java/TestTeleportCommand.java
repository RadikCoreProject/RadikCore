//package com.radik.client;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.DoubleArgumentType;
//import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
//import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//import net.minecraft.command.CommandRegistryAccess;
//import net.minecraft.text.Text;
//
//import static com.radik.client.RadikClient.CLIENT;
//
//public class TestTeleportCommand {
//    private static boolean orbitActive = false;
//    private static double radius = 2.0; // Радиус орбиты
//
//    private static void performOrbitBoost() {
//        double time = System.currentTimeMillis() * 0.001; // Время в секундах
//        double centerX = CLIENT.player.getX();
//        double centerZ = CLIENT.player.getZ();
//
//        double x = Math.cos(time) * radius;
//        double z = Math.sin(time) * radius;
//
//        // Обновляем позицию игрока
//        CLIENT.player.setPosition(centerX + x, CLIENT.player.getY(), centerZ + z);
//    }
//
//    public static void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
//        fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal("orbit")
//            .executes(context -> {
//                orbitActive = !orbitActive;
//                String status = orbitActive ? "§aВключено" : "§cВыключено";
//                context.getSource().sendFeedback(Text.of("Орбитальное движение: " + status));
//                return 1;
//            })
//            .then(ClientCommandManager.argument("radius", DoubleArgumentType.doubleArg(0.5, 10.0))
//                .executes(context -> {
//                    radius = DoubleArgumentType.getDouble(context, "radius");
//                    orbitActive = true;
//                    context.getSource().sendFeedback(Text.of("§aОрбита включена с радиусом: " + radius));
//                    return 1;
//                })
//            )
//        );
//
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (orbitActive && client.player != null) {
//                performOrbitBoost();
//            }
//        });
//    }
//}
