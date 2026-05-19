package dev.maxi.yalp.internal;

import dev.maxi.yalp.api.YALPApi;
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

public final class YALPApiImpl implements YALPApi {
    private final String version;
    private final ComponentManager components;
    private final YALPLogger logger;

    public YALPApiImpl(String version, ComponentManager components, YALPLogger logger) {
        this.version = version;
        this.components = components;
        this.logger = logger;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public ComponentRegistry getComponentRegistry() {
        return components;
    }

    @Override
    public <T extends YALPComponent> Optional<T> getComponent(Class<T> componentType) {
        return components.get(componentType);
    }

    @Override
    public Optional<YALPComponent> getComponent(String id) {
        return components.get(id);
    }

    @Override
    public YALPLogger getLogger() {
        return logger;
    }

    @Override
    public LoggerComponent logger() {
        return require(LoggerComponent.class);
    }

    @Override
    public CompatibilityComponent compatibility() {
        return require(CompatibilityComponent.class);
    }

    @Override
    public MessagesComponent messages() {
        return require(MessagesComponent.class);
    }

    @Override
    public ConfigComponent configs() {
        return require(ConfigComponent.class);
    }

    @Override
    public SchedulerComponent scheduler() {
        return require(SchedulerComponent.class);
    }

    @Override
    public CooldownComponent cooldowns() {
        return require(CooldownComponent.class);
    }

    @Override
    public ItemBuilderComponent itemBuilder() {
        return require(ItemBuilderComponent.class);
    }

    @Override
    public ItemBuilderComponent items() {
        return itemBuilder();
    }

    @Override
    public HooksComponent hooks() {
        return require(HooksComponent.class);
    }

    @Override
    public GuiComponent guis() {
        return require(GuiComponent.class);
    }

    private <T extends YALPComponent> T require(Class<T> type) {
        return getComponent(type).orElseThrow(() -> new IllegalStateException("YALP component missing: " + type.getName()));
    }
}
