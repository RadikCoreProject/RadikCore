package com.radik.block.custom.blockentity.embassy;

import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.connecting.game.EmbassyData;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;

import static com.radik.block.custom.blockentity.embassy.EmbassyBlock.ACTIVE;

public class EmbassyBlockEntity extends BlockEntity {
    private static final String OWNER = "Owner";
    private static final String LEGATE = "Legate";
    private static final String COOLDOWN = "Cooldown";
    private static final String CONNECT = "ConnectedEmbassy";

    public String owner;
    public String legate;
    public LocalDateTime cooldown;
    public BlockPos connect;

    public EmbassyBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.EMBASSY_BLOCK_ENTITY, pos, state);
    }

    public static void tick(ServerWorld world, BlockPos pos, BlockState state, EmbassyBlockEntity blockEntity) {
        if (!world.isClient() && world.getTime() % 20 == 0) {
            if (blockEntity.connect == null) return;
            if (blockEntity.cooldown == null) {
                blockEntity.cooldown = LocalDateTime.MIN;
            }
        }
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        if (view.contains(OWNER)) this.owner = view.getString(OWNER, "SkyGlue555");
        if (view.contains(LEGATE)) this.legate = view.getString(LEGATE, "");
        if (view.contains(COOLDOWN))
            this.cooldown = LocalDateTime.parse(view.getString(COOLDOWN, LocalDateTime.MAX.toString()));
        else this.cooldown = LocalDateTime.MIN;
        if (view.contains(CONNECT)) {
            int[] pos = view.getOptionalIntArray(CONNECT).orElse(new int[3]);
            this.connect = new BlockPos(pos[0], pos[1], pos[2]);
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (this.owner != null) view.putString(OWNER, this.owner);
        if (this.legate != null) view.putString(LEGATE, this.legate);
        if (this.cooldown != null) view.putString(COOLDOWN, this.cooldown.toString());
        if (connect != null) {
            int[] pos = new int[]{this.connect.getX(), this.connect.getY(), this.connect.getZ()};
            view.putIntArray(CONNECT, pos);
        }
    }

    public EmbassyData getData(BlockState state, String name) {
        return new EmbassyData(
            this.owner != null ? this.owner : "SkyGlue555",
            this.legate != null ? this.legate : "",
            pos,
            this.connect != null ? this.connect : new BlockPos(0, 0, 0),
            this.cooldown != null ? this.cooldown : LocalDateTime.MIN,
            name.equals(this.owner) ? 2 : (byte) (name.equals(this.legate) ? 1 : 0),
            state.get(ACTIVE, 0).byteValue()
        );
    }

    public void setData(BlockState state, EmbassyData data) {
        this.legate = data.legate;
        this.connect = data.to;
        if (world == null) return;
        world.setBlockState(this.pos, state.with(ACTIVE, (int) data.powered));
        this.markDirty();
    }
}
