package com.radik.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.Stack;

import static net.minecraft.server.command.CommandManager.*;

public class LabyrinthCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredRegistryAccess, CommandManager.RegistrationEnvironment ignoredEnvironment) {
        dispatcher.register(literal("labyrinth")
            .then(argument("width", IntegerArgumentType.integer(5, 150))
            .then(argument("height", IntegerArgumentType.integer(1, 64))
            .then(argument("length", IntegerArgumentType.integer(5, 150))
            .executes(context -> {
                int width = IntegerArgumentType.getInteger(context, "width");
                int height = IntegerArgumentType.getInteger(context, "height");
                int length = IntegerArgumentType.getInteger(context, "length");

                ServerCommandSource source = context.getSource();
                ServerWorld world = source.getWorld();
                ServerPlayerEntity player = source.getPlayer();
                if (player == null) return 0;
                BlockPos start = new BlockPos(player.getBlockPos());

                generateMazeAdvanced(world, start, width, length, height);
                source.sendMessage(Text.literal("Сложный лабиринт (циклы + изолированные стены) " + width + "x" + length + " сгенерирован"));
                return 1;
            })))));
    }

    private static void generateMazeAdvanced(ServerWorld world, BlockPos start, int width, int length, int wallHeight) {
        Random rand = Random.create();
        // 1. Идеальный лабиринт (дерево)
        boolean[][] maze = generatePerfectMaze(width, length, rand);
        
        // 2. Добавляем циклы: пробиваем случайные внутренние стены (15% от всех стен, не на границе)
        addCycles(maze, width, length, rand);
        
        // 3. Добавляем изолированные стены-островки (внутри проходов, не касаясь основных стен)
        addIsolatedWalls(maze, width, length, rand);
        
        // 4. Отрисовка в мире
        int floorY = start.getY();
        int startX = start.getX();
        int startZ = start.getZ();
        
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                BlockPos groundPos = new BlockPos(startX + x, floorY, startZ + z);
                world.setBlockState(groundPos, Blocks.STONE.getDefaultState());
                
                if (!maze[x][z]) {
                    for (int y = 1; y <= wallHeight; y++) {
                        BlockPos wallPos = new BlockPos(startX + x, floorY + y, startZ + z);
                        world.setBlockState(wallPos, Blocks.COBBLESTONE.getDefaultState());
                    }
                }
            }
        }
        
        // Гарантируем вход и выход (первые и последние клетки проходы, если вдруг стали стеной)
        maze[0][0] = true;
        maze[width-1][length-1] = true;
        world.setBlockState(new BlockPos(startX, floorY, startZ), Blocks.STONE.getDefaultState());
        world.setBlockState(new BlockPos(startX + width - 1, floorY, startZ + length - 1), Blocks.STONE.getDefaultState());
    }
    
    // Идеальный лабиринт (Recursive Backtracker)
    private static boolean[][] generatePerfectMaze(int width, int length, Random rand) {
        boolean[][] maze = new boolean[width][length];
        boolean[][] visited = new boolean[width][length];
        // Изначально все клетки стены (false)
        for (int i = 0; i < width; i++)
            for (int j = 0; j < length; j++)
                maze[i][j] = false;
        
        int startX = rand.nextInt(width);
        int startZ = rand.nextInt(length);
        visited[startX][startZ] = true;
        maze[startX][startZ] = true;
        
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startZ});
        
        while (!stack.isEmpty()) {
            int[] cur = stack.peek();
            int x = cur[0], z = cur[1];
            ArrayList<int[]> neighbors = new ArrayList<>();
            if (x > 0 && !visited[x-1][z]) neighbors.add(new int[]{x-1, z});
            if (x < width-1 && !visited[x+1][z]) neighbors.add(new int[]{x+1, z});
            if (z > 0 && !visited[x][z-1]) neighbors.add(new int[]{x, z-1});
            if (z < length-1 && !visited[x][z+1]) neighbors.add(new int[]{x, z+1});
            
            if (!neighbors.isEmpty()) {
                int[] next = neighbors.get(rand.nextInt(neighbors.size()));
                int nx = next[0], nz = next[1];
                maze[nx][nz] = true;
                visited[nx][nz] = true;
                stack.push(new int[]{nx, nz});
            } else {
                stack.pop();
            }
        }
        return maze;
    }
    
    // Пробиваем дополнительные проходы – создаём циклы
    private static void addCycles(boolean[][] maze, int width, int length, Random rand) {
        // Проходим по всем внутренним стенам (не на границе лабиринта)
        for (int x = 1; x < width-1; x++) {
            for (int z = 1; z < length-1; z++) {
                if (!maze[x][z] && rand.nextFloat() < 0.15f) {
                    // Превращаем стену в проход, но только если это не создаст изолированный остров?
                    // Просто убираем стену
                    maze[x][z] = true;
                }
            }
        }
    }
    
    // Создание изолированных стен (островки внутри проходов, не касающиеся основных стен)
    private static void addIsolatedWalls(boolean[][] maze, int width, int length, Random rand) {
        // Количество островков ~ 5% от площади
        int islandCount = (width * length) / 50; 
        for (int i = 0; i < islandCount; i++) {
            // Ищем позицию, которая сейчас проход (true), не на границе лабиринта, и все 4 соседа (по вертикали/горизонтали) – тоже проходы (чтобы островок был полностью окружён воздухом)
            int attempts = 100;
            while (attempts-- > 0) {
                int x = 1 + rand.nextInt(width-2);
                int z = 1 + rand.nextInt(length-2);
                if (maze[x][z] &&
                    maze[x-1][z] && maze[x+1][z] && maze[x][z-1] && maze[x][z+1]) {
                    // Ставим стену (false)
                    maze[x][z] = false;
                    // Опционально: можно создать группу из 2-3 стен рядом (маленький остров)
                    if (rand.nextFloat() < 0.3f) {
                        int[] dx = {0,1,-1,0,0};
                        int[] dz = {0,0,0,1,-1};
                        for (int d = 1; d < dx.length; d++) {
                            int nx = x + dx[d];
                            int nz = z + dz[d];
                            if (nx > 0 && nx < width-1 && nz > 0 && nz < length-1 && maze[nx][nz]) {
                                // Проверим, что у этого нового соседа тоже 4 соседа-прохода (чтобы остров не соединился с основной стеной)
                                if (maze[nx-1][nz] && maze[nx+1][nz] && maze[nx][nz-1] && maze[nx][nz+1]) {
                                    maze[nx][nz] = false;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}