package com.radik.client.screen;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.client.screen.widget.TutorialListWidget;
import com.radik.item.RegisterItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TutorialScreen extends Screen {
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final Screen parent;
    private final byte scrn;

    public TutorialScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
        this.scrn = 0;
    }

    public TutorialScreen(Screen parent, byte scrn) {
        super(parent.getTitle());
        this.parent = parent;
        this.scrn = scrn;
    }

    @Override
    protected void init() {
        super.init();
        TutorialListWidget list = new TutorialListWidget(this.client, this.width, this.height - 80, 50, 90);
        renderList(list, this.scrn);

        DirectionalLayoutWidget footer = new DirectionalLayoutWidget(1, 3, DirectionalLayoutWidget.DisplayAxis.HORIZONTAL);
        footer.spacing(20);
        footer.add(ButtonWidget.builder(Text.literal("<<<"), b -> close()).build());
        footer.add(ButtonWidget.builder(ScreenTexts.DONE, b -> this.client.setScreen(new TitleScreen())).build());
        footer.add(ButtonWidget.builder(Text.literal(">>>"), b -> this.client.setScreen(new TutorialScreen(this, (byte) (this.scrn + 1)))).build());
        TextWidget text = new TextWidget(this.title, textRenderer);
        text.setPosition((width - this.textRenderer.getWidth(text.getMessage())) / 2, height / 8);
        this.addDrawableChild(text);
        this.layout.addHeader(text);
        this.layout.addBody(list);
        this.layout.addFooter(footer);

        this.layout.forEachChild(this::addDrawableChild);
        this.layout.refreshPositions();
    }



    @Override
    public void close() {
        super.close();
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    private void renderList(TutorialListWidget list, byte scrn) {
        switch (scrn) {
            case 0 -> r0(list);
            case 1 -> r1(list);
        }
    }

    private void r1(TutorialListWidget list) {
        list.addEntry(new TutorialListWidget.TextEntry(
            textRenderer,
            Text.literal("Основное"),
            Text.literal("Индустриализация - важный компонент сервера, существующий ещё с давних пор. " +
                "Несмотря на это, проект продолжает смотреть в сторону индустриализации. " +
                "Здесь представлено множество способов индустриального развития, но вы всегда вправе требовать от разработчиков продолжать развитие в эту сторону."),
            list.getRowWidth()
        ));

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Предметы"), list.getRowWidth())
                .description(Text.literal("Часть комьюнити проекта - любители чего-то промышленного. " +
                "В проекте представлено несколько различных сфер индустриального развития на сервере. " +
                "В этом разделе можно познакомиться о некоторых из них."))
                .bigTitle()
                .item(RegisterBlocks.DIAMOND_STORAGE_BLOCK.asItem())
                .screen(1)
                .withCraft(List.of(RegisterItems.CAPSULE))
            .build()
        );
    }

    private void r0(TutorialListWidget list) {
        list.addEntry(new TutorialListWidget.TextEntry(
            textRenderer,
            Text.literal("Основное"),
            Text.literal("RadikCore - многофункциональный проект, который связывает Minecraft сервер, мод, телеграм-бот, а также чат. " +
                "Это проект для тех, кто любит творчество, хочет найти новых друзей или просто с комфортом провести время. " +
                "Это не просто мод, это - огромная база из идей игроков сервера. " +
                "В этом меню вы можете ознакомиться с проектом. " +
                "Этот туториал подойдет как новичку, который хочет познакомиться с сервером, так и олду, который может узнать для себя что-то новое. " +
                "Чтобы открыть вкладку, дважды кликните по ней."),
            list.getRowWidth()
        ));

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Индустриализация"), list.getRowWidth())
            .description(Text.literal("Часть комьюнити проекта - любители чего-то промышленного. " +
                "В проекте представлено несколько различных сфер индустриального развития на сервере. " +
                "В этом разделе можно познакомиться о некоторых из них."))
            .item(RegisterBlocks.DIAMOND_STORAGE_BLOCK.asItem())
            .screen(1)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Города и селения"), list.getRowWidth())
            .description(Text.literal("Игроки объединяются в группы, образуются целые города. " +
                "На сервере развивается торговля и международное сотрудничество. " +
                "Здесь вы сможете узнать о некоторых поселениях, что особенно полезно при выборе места жительства."))
            .item(RegisterBlocks.HOUSE.asItem())
            .screen(2)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Транспорт"), list.getRowWidth())
            .description(Text.literal("Особое положение на сервере занимает транспорт. " +
                "Он оказался нужен, так как некоторые селения находятся на больших расстояниях. " +
                "В этом разделе вы узнаете про транспорт на сервере и его особенности."))
            .item(RegisterBlocks.HOUSE.asItem())
            .screen(3)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Строительство"), list.getRowWidth())
            .description(Text.literal("Любители строительства - вам тут рады! " +
                "На сервере вы сможете построить уникальные постройки благодаря множеству новых декоративных блоков. " +
                "Этот раздел вам поможет узнать о том, какие здесь есть блоки, как их получить, а также некоторые уникальные постройки на сервере."))
            .item(RegisterBlocks.BRICK7.asItem())
            .screen(4)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Приключения"), list.getRowWidth())
            .description(Text.literal("Некоторые игроки очень любят приключения. " +
                "Для них тоже есть своя ниша - новые данжи, биомы, фичи и просто классные места для посещения. " +
                "В этом разделе вы познакомитесь о многих из них."))
            .item(RegisterItems.DISC_PURE_VESSEL)
            .screen(5)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Радиация"), list.getRowWidth())
            .description(Text.literal("Радиация - новая фишка сервера для любителей экстрима и новых эмоций. " +
                "Обновление с радиацией было выпущено на юбилей важного события - 40 лет с аварии на ЧАЭС. " +
                "В этом разделе вы узнаете для чего нужна радиация в проекте, научитесь не заражаться ей, а также узнаете как она работает и как с ней бороться."))
            .icon(Radik.id("textures/mob_effect/radiation.png"))
            .screen(6)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("События"), list.getRowWidth())
            .description(Text.literal("Событие - уникальная и одна из самых сложных вещей в нашем проекте. " +
                "События - многодневные ивенты, в которых нужно выполнять задания, покупать различные вещи. " +
                "В этом разделе можно узнать о том, как, где и когда проходят события и что в них нужно делать."))
            .item(RegisterBlocks.EVENT_BLOCK.asItem())
            .screen(7)
            .build()
        );

        list.addEntry(new TutorialListWidget.IconEntry.Builder(textRenderer, list, Text.literal("Ивенты"), list.getRowWidth())
            .description(Text.literal("Ивенты на нашем проекте (кроме События) - небольшой отрезок времени, когда игроки могут испытать яркие эмоции и получить награды. " +
                "Ивенты обычно проходят на отдельном сервере, а после ивента все возвращается на свои места." +
                "В этом разделе вы узнаете: какие ивенты бывают, кто их создает и какие награды можно получить."))
            .item(RegisterBlocks.TROPHY_NOSTALGIC_GOLD.asItem())
            .screen(8)
            .build()
        );

        list.addEntry(new TutorialListWidget.AttributedEntry(
            textRenderer,
            List.of("SkyGlue555"),
            Text.literal("29.04.2026"),
            Text.literal("29.04.2026"),
            list.getRowWidth()
        ));
    }
}