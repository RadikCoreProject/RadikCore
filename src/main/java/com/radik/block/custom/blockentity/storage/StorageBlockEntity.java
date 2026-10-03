package com.radik.block.custom.blockentity.storage;

import com.radik.Radik;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.connecting.game.StorageBlockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StorageBlockEntity extends BlockEntity {
    private static final String OWNER = "Owner";
    private static final String STORAGE = "Storage";
    private static final String FILLING = "Filling";
    private static final String TYPE = "Type";

    public String owner;
    public int storage;
    public int filling;
    public ItemStack type;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.STORAGE_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        if (view.contains(OWNER)) this.owner = view.getString(OWNER, "SkyGlue555");
        if (view.contains(STORAGE)) this.storage = view.getInt(STORAGE, 0);
        if (view.contains(FILLING)) this.filling = view.getInt(FILLING, 0);
        if (view.contains(TYPE)) this.type = view.read(TYPE, ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (this.owner != null) view.putString(OWNER, this.owner);
        view.putInt(STORAGE, this.storage);
        view.putInt(FILLING, this.filling);
        if (this.type != null && !this.type.isEmpty()) view.put(TYPE, ItemStack.CODEC, this.type);
    }

    public StorageBlockData getData() {
        return new StorageBlockData(
            this.owner != null ? this.owner : "SkyGlue555",
            this.storage,
            this.filling,
            this.type != null ? this.type : Items.AIR.getDefaultStack()
        );
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    public void setType(ItemStack stack) {
        this.type = stack.copy();
        this.markDirty();
        if (this.world == null) return;
        this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public void setFilling(int count) {
        this.filling = count;
        this.markDirty();
        if (this.world == null) return;
        this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }
}
