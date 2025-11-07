package com.radik.connecting.event;

import com.radik.connecting.event.factory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class Event<T extends EventData> implements Comparable<Event<?>>, Eventer {
    private ChallengeTime time;
    private final ChallengeType type;
    private ChallengeEvent event;
    private T data;
    private ItemStack reward;
    private int count;
    private int completion = 0;
    private static final EventDataFactory DATA_FACTORY = new EventDataFactoryImpl();
    private boolean claimed = false;

    public Event(ChallengeTime time, ChallengeType type,
                 ChallengeEvent event, T data, ItemStack reward, int count) {
        this.time = time;
        this.type = type;
        this.event = event;
        this.data = data;
        this.reward = reward;
        this.count = count;
    }

    @Override
    public int compareTo(@NotNull Event<?> o) {
        int timeComparison = Integer.compare(o.time().ordinal(), time().ordinal());
        return timeComparison != 0 ? timeComparison :
                Float.compare((float) o.completion() / o.count(), (float) completion() / count());
    }


    public Text getText() { return Text.translatable("radik.util." + type); }
    @Override
    public boolean isCompleted() { return completion >= count; }
    @Override
    public ItemStack getReward() { return reward; }
    @Override
    public ChallengeType getChallengeType() { return type; }

    public void increment() { if (!isCompleted()) completion++; }
    public void decrement() { if (completion > 0) completion--; }
    @Override
    public int getCount() { return count; }
    public void setCount(int c) { count = c; }
    public void setTime(ChallengeTime challengeTime) { time = challengeTime; }
    public void setData(T data) { this.data = data; }
    public void setReward(ItemStack itemStack) { reward = itemStack; }
    public void setEvent(ChallengeEvent challengeEvent) { event = challengeEvent; }
    @Override
    public void setValue(int value) { completion = value; }
    @Override
    public int getValue() { return completion; }
    public boolean isClaimed() { return claimed; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }

    public ChallengeTime time() { return time; }
    public ChallengeType type() { return type; }
    public ChallengeEvent event() { return event; }
    public T data() { return data; }
    public ItemStack reward() { return reward; }
    public int count() { return count; }
    public int completion() { return completion; }

    @Override
    public NbtCompound toNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("time", time.name());
        nbt.putString("type", type.name());
        nbt.putString("event", event.name());
        nbt.put("data", data.toNbt());

        ItemStack reward = this.reward.copy();
        if (reward.getCount() > reward.getMaxCount()) reward.setCount(reward.getMaxCount());
        nbt.put("reward", reward.toNbt(registries));

        nbt.putString("dataType", data.getType());
        nbt.putInt("count", count);
        nbt.putInt("completion", completion);
        nbt.putBoolean("rewardClaimed", claimed);
        return nbt;
    }

    public static @NotNull Event<?> fromNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        ChallengeTime time = ChallengeTime.valueOf(nbt.getString("time").orElse("DAY"));
        ChallengeType type = ChallengeType.valueOf(nbt.getString("type").orElse("BROKE"));
        ChallengeEvent event = ChallengeEvent.valueOf(nbt.getString("event").orElse("HALLOWEEN"));
        int count = nbt.getInt("count").orElse(0);
        int completion = nbt.getInt("completion").orElse(0);

        ItemStack reward = ItemStack.EMPTY;
        NbtElement rewardNbt = nbt.get("reward");
        if (rewardNbt != null) reward = ItemStack.fromNbt(registries, rewardNbt).orElse(ItemStack.EMPTY);

        NbtElement dataNbt = nbt.get("data");
        String dataType = nbt.getString("dataType").orElse("item");
        EventData data = DATA_FACTORY.fromNbt(dataNbt);

        boolean rewardClaimed = nbt.getBoolean("rewardClaimed").orElse(false);

        Event<?> result = switch (dataType) {
            case "block" -> new Event<>(time, type, event, (BlockEventData) data, reward, count);
            case "item" -> new Event<>(time, type, event, (ItemEventData) data, reward, count);
            case "entity" -> new Event<>(time, type, event, (EntityEventData) data, reward, count);
            default -> throw new IllegalArgumentException("Unknown data type: " + dataType);
        };

        result.completion = completion;
        result.claimed = rewardClaimed;
        return result;
    }

    @Override
    public int getType() {
        return switch (type) {
            case EAT -> 0;
            case BROKE, PLACE -> 1;
            case KILL -> 2;
        };
    }

    @Override
    public String toString() {
        return String.format("Event: {" +
            "time: [%s], " +
            "type: [%s], " +
            "event: [%s], " +
            "<T>data: [%s], " +
            "reward: [%s], " +
            "count: [%s], " +
            "claimed: [%s]}",
            time, type, event, data, reward, count, claimed);
    }
}