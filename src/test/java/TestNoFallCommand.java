//package com.radik.client;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.DoubleArgumentType;
//import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
//import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.ClientPlayerEntity;
//import net.minecraft.command.CommandRegistryAccess;
//import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//
//import java.util.Random;
//
//import static com.radik.client.RadikClient.CLIENT;
//
//public class TestNoFallCommand {
//    private static boolean noFallActive = false;
//    private static int mode = 0; // 0: Fake ground, 1: Teleport, 2: Crouch, 3: Smart
//    private static int tickCounter = 0;
//    private static double originalY = 0;
//    private static boolean wasFalling = false;
//    private static final Random random = new Random();
//    private static boolean isCrouching = false;
//
//    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
//        dispatcher.register(ClientCommandManager.literal("nofall")
//            .executes(context -> {
//                noFallActive = !noFallActive;
//                String status = noFallActive ? "§aВключено" : "§cВыключено";
//                context.getSource().sendFeedback(Text.of("NoFall тест: " + status + " (режим " + mode + ")"));
//                return 1;
//            })
//            .then(ClientCommandManager.literal("mode1")
//                .executes(context -> {
//                    mode = 0;
//                    context.getSource().sendFeedback(Text.of("§aРежим 1: Fake Ground Packets"));
//                    return 1;
//                })
//            )
//            .then(ClientCommandManager.literal("mode2")
//                .executes(context -> {
//                    mode = 1;
//                    context.getSource().sendFeedback(Text.of("§aРежим 2: Teleport Fall"));
//                    return 1;
//                })
//            )
//            .then(ClientCommandManager.literal("mode3")
//                .executes(context -> {
//                    mode = 2;
//                    context.getSource().sendFeedback(Text.of("§aРежим 3: Crouch Reset"));
//                    return 1;
//                })
//            )
//            .then(ClientCommandManager.literal("mode4")
//                .executes(context -> {
//                    mode = 3;
//                    context.getSource().sendFeedback(Text.of("§aРежим 4: Smart NoFall"));
//                    return 1;
//                })
//            )
//            .then(ClientCommandManager.literal("test")
//                .executes(context -> {
//                    triggerTestFall();
//                    return 1;
//                })
//            )
//            .then(ClientCommandManager.literal("spam")
//                .executes(context -> {
//                    spamGroundPackets();
//                    return 1;
//                })
//            )
//            .then(ClientCommandManager.literal("debug")
//                .executes(context -> {
//                    printDebugInfo();
//                    return 1;
//                })
//            )
//        );
//
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (noFallActive && client.player != null) {
//                tickCounter++;
//                performNoFall(client.player);
//            }
//        });
//
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (client.player != null && tickCounter % 20 == 0) {
//                ClientPlayerEntity player = client.player;
//                if (!player.isOnGround() && player.fallDistance > 1) {
//                    System.out.printf("[NoFallTester] Падение: fallDistance=%.2f, Y=%.2f, onGround=%b%n",
//                        player.fallDistance, player.getY(), player.isOnGround());
//                }
//            }
//        });
//    }
//
//    private static void performNoFall(ClientPlayerEntity player) {
//        switch (mode) {
//            case 0 -> fakeGroundPackets(player);     // Отправка фальшивых onGround пакетов
//            case 1 -> teleportFall(player);          // Телепортация при падении
//            case 2 -> crouchReset(player);           // Сброс через крауч
//            case 3 -> smartNoFall(player);           // Умный NoFall с различными техниками
//        }
//    }
//
//    /**
//     * Метод 1: Отправка фальшивых onGround пакетов
//     * Используем OnGroundOnly и Full пакеты
//     */
//    private static void fakeGroundPackets(ClientPlayerEntity player) {
//        if (!player.isOnGround() && player.fallDistance > 1.5f) {
//            // Отправляем OnGroundOnly пакет с onGround=true
//            PlayerMoveC2SPacket.OnGroundOnly groundPacket =
//                new PlayerMoveC2SPacket.OnGroundOnly(true, false);
//            player.networkHandler.sendPacket(groundPacket);
//
//            // Каждые 3 тика отправляем полный пакет с координатами
//            if (tickCounter % 3 == 0) {
//                PlayerMoveC2SPacket.Full fullPacket = new PlayerMoveC2SPacket.Full(
//                    player.getX(),
//                    player.getY(),
//                    player.getZ(),
//                    player.getYaw(),
//                    player.getPitch(),
//                    true,  // onGround=true
//                    false  // horizontalCollision
//                );
//                player.networkHandler.sendPacket(fullPacket);
//            }
//
//            // Показываем статус
//            if (tickCounter % 10 == 0) {
//                player.sendMessage(Text.of("§7[NoFall] §fОтправка fake ground пакетов"), true);
//            }
//        }
//    }
//
//    /**
//     * Метод 2: Телепортация на землю при падении
//     * Имитирует более сложные читы, которые телепортируют игрока
//     */
//    private static void teleportFall(ClientPlayerEntity player) {
//        if (!player.isOnGround() && player.fallDistance > 2.0f) {
//            if (!wasFalling) {
//                originalY = player.getY();
//                wasFalling = true;
//            }
//
//            // Каждые 4 тика пытаемся "приземлиться"
//            if (tickCounter % 4 == 0) {
//                double groundY = findGroundHeight(player);
//
//                if (groundY > 0 && originalY - groundY > 3) {
//                    // Телепортируемся на уровень земли + 0.001
//                    PlayerMoveC2SPacket.PositionAndOnGround teleportPacket =
//                        new PlayerMoveC2SPacket.PositionAndOnGround(
//                            player.getX(),
//                            groundY + 0.001,
//                            player.getZ(),
//                            true,   // onGround
//                            false   // horizontalCollision
//                        );
//                    player.networkHandler.sendPacket(teleportPacket);
//
//                    // Немедленно возвращаемся в воздух
//                    PlayerMoveC2SPacket.PositionAndOnGround returnPacket =
//                        new PlayerMoveC2SPacket.PositionAndOnGround(
//                            player.getX(),
//                            originalY - player.fallDistance + 2.0,
//                            player.getZ(),
//                            false,  // onGround
//                            false   // horizontalCollision
//                        );
//                    player.networkHandler.sendPacket(returnPacket);
//
//                    player.sendMessage(Text.of("§7[NoFall] §fТелепорт на землю и обратно"), true);
//                }
//            }
//        } else if (player.isOnGround()) {
//            wasFalling = false;
//        }
//    }
//
//    /**
//     * Метод 3: Сброс fallDistance через крауч
//     * Используем механику крауча для обмана сервера
//     */
//    private static void crouchReset(ClientPlayerEntity player) {
//        if (!player.isOnGround() && player.fallDistance > 1.0f) {
//            // Чередуем крауч и обычное состояние
//            if (tickCounter % 4 == 0) {
//                isCrouching = true;
//                player.setSneaking(true);
//
//                // Отправляем пакет с краучем и onGround=true
//                PlayerMoveC2SPacket.Full crouchPacket = new PlayerMoveC2SPacket.Full(
//                    player.getX(),
//                    player.getY(),
//                    player.getZ(),
//                    player.getYaw(),
//                    player.getPitch(),
//                    true,   // onGround=true при крауче
//                    false
//                );
//                player.networkHandler.sendPacket(crouchPacket);
//
//            } else if (tickCounter % 4 == 2) {
//                isCrouching = false;
//                player.setSneaking(false);
//
//                // Отправляем обычный пакет
//                PlayerMoveC2SPacket.Full normalPacket = new PlayerMoveC2SPacket.Full(
//                    player.getX(),
//                    player.getY(),
//                    player.getZ(),
//                    player.getYaw(),
//                    player.getPitch(),
//                    false,  // onGround=false
//                    false
//                );
//                player.networkHandler.sendPacket(normalPacket);
//            }
//
//            if (tickCounter % 20 == 0) {
//                player.sendMessage(Text.of("§7[NoFall] §fКрауч-сброс активен"), true);
//            }
//        }
//    }
//
//    /**
//     * Метод 4: Умный NoFall - комбинирует разные техники
//     */
//    private static void smartNoFall(ClientPlayerEntity player) {
//        if (!player.isOnGround() && player.fallDistance > 1.5f) {
//            // Случайно выбираем технику
//            int tech = random.nextInt(3);
//
//            switch (tech) {
//                case 0 -> {
//                    // Просто отправляем onGround=true
//                    PlayerMoveC2SPacket.OnGroundOnly packet =
//                        new PlayerMoveC2SPacket.OnGroundOnly(true, false);
//                    player.networkHandler.sendPacket(packet);
//                }
//                case 1 -> {
//                    // Отправляем полный пакет с небольшой коррекцией Y
//                    PlayerMoveC2SPacket.Full packet = new PlayerMoveC2SPacket.Full(
//                        player.getX(),
//                        player.getY() + 0.01, // Легкая коррекция
//                        player.getZ(),
//                        player.getYaw(),
//                        player.getPitch(),
//                        true,
//                        false
//                    );
//                    player.networkHandler.sendPacket(packet);
//                }
//                case 2 -> {
//                    // Используем PositionAndOnGround с onGround=true
//                    PlayerMoveC2SPacket.PositionAndOnGround packet =
//                        new PlayerMoveC2SPacket.PositionAndOnGround(
//                            player.getX(),
//                            player.getY(),
//                            player.getZ(),
//                            true,
//                            false
//                        );
//                    player.networkHandler.sendPacket(packet);
//                }
//            }
//
//            if (tickCounter % 15 == 0) {
//                player.sendMessage(Text.of("§7[NoFall] §fУмный режим (техника " + tech + ")"), true);
//            }
//        }
//    }
//
//    /**
//     * Находит высоту земли под игроком
//     */
//    private static double findGroundHeight(ClientPlayerEntity player) {
//        BlockPos playerPos = player.getBlockPos();
//
//        // Ищем ближайший твердый блок под игроком (максимум 256 блоков вниз)
//        for (int i = 0; i < 256; i++) {
//            BlockPos checkPos = playerPos.down(i);
//            if (player.getEntityWorld().getBlockState(checkPos).isSolid()) {
//                return checkPos.getY() + 1.0; // Верхняя часть блока
//            }
//        }
//
//        return -1;
//    }
//
//    /**
//     * Запускает тестовое падение
//     */
//    private static void triggerTestFall() {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client.player != null) {
//            ClientPlayerEntity player = client.player;
//
//            // Запоминаем текущую позицию
//            Vec3d currentPos = player.getEntityPos();
//
//            // Телепортируем игрока в воздух
//            player.setPosition(currentPos.x, currentPos.y + 15, currentPos.z);
//
//            // Даем импульс вниз
//            player.setVelocity(0, -0.5, 0);
//
//            player.sendMessage(Text.of("§c[Тест] §fТелепорт на высоту +15 блоков"), false);
//            player.sendMessage(Text.of("§c[Тест] §fNoFall будет активирован через 1 секунду"), false);
//
//            // Включаем NoFall через 1 секунду
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    noFallActive = true;
//                    player.sendMessage(Text.of("§a[Тест] §fNoFall активирован"), false);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//    }
//
//    /**
//     * Спам ground пакетами (для тестирования детекции спама)
//     */
//    private static void spamGroundPackets() {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client.player != null) {
//            ClientPlayerEntity player = client.player;
//
//            player.sendMessage(Text.of("§c[Спам] §fНачинаю спам ground пакетами"), false);
//
//            new Thread(() -> {
//                for (int i = 0; i < 50; i++) {
//                    // Чередуем true и false
//                    boolean onGround = i % 2 == 0;
//                    PlayerMoveC2SPacket.OnGroundOnly packet =
//                        new PlayerMoveC2SPacket.OnGroundOnly(onGround, false);
//                    player.networkHandler.sendPacket(packet);
//
//                    try {
//                        Thread.sleep(20); // 20ms между пакетами
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                player.sendMessage(Text.of("§a[Спам] §fСпам завершен"), false);
//            }).start();
//        }
//    }
//
//    /**
//     * Выводит отладочную информацию
//     */
//    private static void printDebugInfo() {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client.player != null) {
//            ClientPlayerEntity player = client.player;
//
//            String info = String.format(
//                "§e[Debug]§f fallDistance=%.2f, onGround=%b, Y=%.2f, NoFall=%b, Mode=%d",
//                player.fallDistance,
//                player.isOnGround(),
//                player.getY(),
//                noFallActive,
//                mode
//            );
//
//            player.sendMessage(Text.of(info), false);
//            System.out.println("[NoFallTester] " + info.replace("§e", "").replace("§f", ""));
//        }
//    }
//
//    /**
//     * Дополнительный класс для продвинутых техник NoFall
//     */
//    public static class AdvancedNoFall {
//
//        /**
//         * Метод 5: Пакетная отправка - отправляет несколько пакетов с разными onGround
//         */
//        public static void sendBatchPackets(ClientPlayerEntity player) {
//            // Пакет 1: onGround=true
//            PlayerMoveC2SPacket.Full packet1 = new PlayerMoveC2SPacket.Full(
//                player.getX(),
//                player.getY(),
//                player.getZ(),
//                player.getYaw(),
//                player.getPitch(),
//                true,
//                false
//            );
//
//            // Пакет 2: onGround=false (через 10мс)
//            PlayerMoveC2SPacket.Full packet2 = new PlayerMoveC2SPacket.Full(
//                player.getX(),
//                player.getY() + 0.001, // Минимальное смещение
//                player.getZ(),
//                player.getYaw(),
//                player.getPitch(),
//                false,
//                false
//            );
//
//            // Пакет 3: onGround=true (через еще 10мс)
//            PlayerMoveC2SPacket.OnGroundOnly packet3 =
//                new PlayerMoveC2SPacket.OnGroundOnly(true, false);
//
//            player.networkHandler.sendPacket(packet1);
//
//            new Thread(() -> {
//                try {
//                    Thread.sleep(10);
//                    player.networkHandler.sendPacket(packet2);
//                    Thread.sleep(10);
//                    player.networkHandler.sendPacket(packet3);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//
//        /**
//         * Метод 6: Имитация Lag-компенсации - отправляет старые пакеты с onGround=true
//         */
//        public static void sendLagCompensation(ClientPlayerEntity player, double oldX, double oldY, double oldZ) {
//            PlayerMoveC2SPacket.PositionAndOnGround lagPacket =
//                new PlayerMoveC2SPacket.PositionAndOnGround(
//                    oldX,
//                    oldY,
//                    oldZ,
//                    true,  // onGround=true для старой позиции
//                    false
//                );
//            player.networkHandler.sendPacket(lagPacket);
//        }
//    }
//}
