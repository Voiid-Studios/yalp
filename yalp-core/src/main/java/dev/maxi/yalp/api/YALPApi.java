package dev.maxi.yalp.api;

import dev.maxi.yalp.api.component.ComponentRegistry;
import dev.maxi.yalp.api.component.YALPComponent;
import dev.maxi.yalp.api.compat.CompatibilityComponent;
import dev.maxi.yalp.api.config.ConfigComponent;
import dev.maxi.yalp.api.cooldown.CooldownComponent;
import dev.maxi.yalp.api.gui.GuiComponent;
import dev.maxi.yalp.api.hooks.HooksComponent;
import dev.maxi.yalp.api.item.ItemBuilderComponent;
import dev.maxi.yalp.api.logger.LoggerComponent;
import dev.maxi.yalp.api.message.MessagesComponent;
import dev.maxi.yalp.api.scheduler.SchedulerComponent;
import dev.maxi.yalp.api.util.YALPLogger;

import java.util.Optional;

public interface YALPApi {
    String getVersion();

    ComponentRegistry getComponentRegistry();

    <T extends YALPComponent> Optional<T> getComponent(Class<T> componentType);

    Optional<YALPComponent> getComponent(String id);

    YALPLogger getLogger();

    LoggerComponent logger();

    CompatibilityComponent compatibility();

    MessagesComponent messages();

    ConfigComponent configs();

    SchedulerComponent scheduler();

    CooldownComponent cooldowns();

    ItemBuilderComponent itemBuilder();

    ItemBuilderComponent items();

    HooksComponent hooks();

    GuiComponent guis();
}
