package com.radik.connecting.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.radik.Radik;
import com.radik.connecting.event.factory.*;
import com.radik.util.Duplet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>глобальная переработка событий для зимнего обновления.</p>
 * <p>убрана типизация, добавлены кодеки для сериализации</p>
 * @see Eventer
 * @see EventData
 * @since 1.4.1
 * @author radik
 */
public class Event implements Comparable<Event>, Eventer {
    private ChallengeTime time;
    private final ChallengeType type;
    private ChallengeEvent event;
    private EventData data;
    private ItemStack reward;
    private int count;
    private int completion = 0;
    private boolean claimed = false;

    public static final Codec<Event> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.STRING.fieldOf("time").xmap(
                ChallengeTime::valueOf,
                ChallengeTime::name
            ).forGetter(Event::time),
            Codec.STRING.fieldOf("type").xmap(
                ChallengeType::valueOf,
                ChallengeType::name
            ).forGetter(Event::type),
            Codec.STRING.fieldOf("event").xmap(
                ChallengeEvent::valueOf,
                ChallengeEvent::name
            ).forGetter(Event::event),
            Codec.STRING.fieldOf("dataType").forGetter(e -> e.data.getType()),
            Codec.STRING.fieldOf("dataValue").forGetter(e -> e.data.getStringValue()),
            ItemStack.CODEC.fieldOf("reward").forGetter(Event::reward),
            Codec.INT.fieldOf("count").forGetter(Event::count),
            Codec.INT.fieldOf("completion").forGetter(Event::completion),
            Codec.BOOL.fieldOf("claimed").forGetter(Event::isClaimed)
        ).apply(instance, (timeStr, typeStr, eventStr, dataType, dataValue, reward, count, completion, claimed) -> {
            EventData data = createEventData(dataType, dataValue);
            Event eventObj = new Event(
                timeStr,
                typeStr,
                eventStr,
                data, reward, count
            );
            eventObj.completion = completion;
            eventObj.claimed = claimed;
            return eventObj;
        })
    );

    private static EventData createEventData(String type, String value) {
        Identifier id = Identifier.tryParse(value);
        if (id == null) return null;

        return switch (type) {
            case "block" -> new BlockEventData(id);
            case "item" -> new ItemEventData(id);
            case "entity" -> new EntityEventData(id);
            default -> null;
        };
    }

    public Event(ChallengeTime time, ChallengeType type,
                 ChallengeEvent event, EventData data, ItemStack reward, int count) {
        this.time = time;
        this.type = type;
        this.event = event;
        this.data = data;
        this.reward = reward;
        this.count = count;
    }

    @Override
    public int compareTo(@NotNull Event o) {
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
    public void setData(EventData data) { this.data = data; }
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
    public EventData data() { return data; }
    public ItemStack reward() { return reward; }
    public int count() { return count; }
    public int completion() { return completion; }

    @Override
    public NbtCompound toNbt(RegistryWrapper.WrapperLookup registries) {
        try (ErrorReporter.Logging logging = new ErrorReporter.Logging(ErrorReporter.Impl.CONTEXT, Radik.LOGGER)) {
            NbtWriteView writeView = NbtWriteView.create(logging, registries);
            writeView.put("event", CODEC, this);
            return writeView.getNbt();
        }
    }

    public static Event fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        try (ErrorReporter.Logging logging = new ErrorReporter.Logging(ErrorReporter.Impl.CONTEXT, Radik.LOGGER)) {
            NbtReadView readView = (NbtReadView) NbtReadView.create(logging, registries, nbt);
            return readView.read("event", CODEC).orElse(null);
        }
    }

    public static Event fromReadView(@NotNull ReadView view) {
        return view.read("event", CODEC).orElse(null);
    }

    public void toWriteView(@NotNull WriteView view) {
        view.put("event", CODEC, this);
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
                "data: [%s], " +
                "reward: [%s], " +
                "count: [%s], " +
                "claimed: [%s]}",
            time, type, event, data, reward, count, claimed);
    }

    private static final Codec<Duplet<String, Eventer[]>> SINGLE_ENTRY_CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("player").forGetter(Duplet::type),
            Codec.list(Event.CODEC)
                .xmap(
                    eventList -> {
                        Eventer[] array = new Eventer[4];
                        for (int i = 0; i < Math.min(eventList.size(), 4); i++) {
                            array[i] = eventList.get(i);
                        }
                        return array;
                    },
                    eventerArray -> {
                        return Arrays.stream(eventerArray)
                            .filter(eventer -> eventer instanceof Event)
                            .map(eventer -> (Event) eventer)
                            .collect(Collectors.toList());
                    }
                )
                .fieldOf("events").forGetter(Duplet::parametrize)
        ).apply(instance, Duplet::new));

    public static final Codec<Map<String, Eventer[]>> PLAYER_EVENTS_CODEC =
        Codec.list(SINGLE_ENTRY_CODEC)
            .xmap(
                dupletList -> dupletList.stream()
                    .collect(Collectors.toMap(
                        Duplet::type,
                        Duplet::parametrize
                    )),
                map -> map.entrySet().stream()
                    .map(entry -> new Duplet<>(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList())
            );

    public static final Codec<Duplet<Eventer, List<String>>> GLOBAL_EVENT_CODEC =
        RecordCodecBuilder.create(instance ->
            instance.group(
                Event.CODEC.fieldOf("event").forGetter(d -> (Event) d.type()),
                Codec.list(Codec.STRING).fieldOf("params").forGetter(Duplet::parametrize)
            ).apply(instance, Duplet::new)
        );
}