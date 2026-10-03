package com.radik.connecting.event;

import com.radik.connecting.event.factory.EventData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;

public interface Eventer {
    int getType();
    NbtCompound toNbt(RegistryWrapper.WrapperLookup registries);
    default void toWriteView(WriteView view) {
        if (this instanceof Event event) event.toWriteView(view);
    }
    void setValue(int value);
    int getValue();
    boolean isCompleted();
    ItemStack getReward();
    ChallengeType getChallengeType();
    boolean isClaimed();
    void setClaimed(boolean claimed);
    int getCount();
    Text getText();
    void increment();
    void decrement();
    EventData data();
}
