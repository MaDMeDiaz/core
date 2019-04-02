package com.envyclient.core.impl.managers;

import com.envyclient.core.api.manager.Manager;
import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.module.ModuleMode;
import com.envyclient.core.api.module.data.Data;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.api.setting.data.Name;
import com.envyclient.core.api.setting.type.ModeSetting;
import com.envyclient.core.impl.events.KeyPressEvent;
import com.envyclient.core.impl.modules.ClickGUI;
import com.envyclient.core.impl.modules.MiddleClickFriend;
import com.envyclient.core.impl.modules.Screenshot;
import com.envyclient.core.impl.modules.hud.Hud;
import me.ihaq.eventmanager.listener.EventListener;
import me.ihaq.eventmanager.listener.EventTarget;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.envyclient.core.Envy.Managers.EVENT;
import static com.envyclient.core.Envy.Managers.SETTING;

public class ModuleManager extends Manager<Module> implements EventListener {

    @Override
    public void enable() {
        EVENT.register(this);
        registerModules(
                new Hud(), new MiddleClickFriend(), new ClickGUI(), new Screenshot()
        );
    }

    @Override
    public void disable() {
        // force disabling clickgui module when we close the game
        Module clickGUI = getModule(ClickGUI.class);
        if (clickGUI.isToggled()) {
            clickGUI.toggle();
        }
    }

    private void registerModules(Module... modules) {
        Arrays.stream(modules)
                .filter(module -> module.getClass().isAnnotationPresent(Data.class))
                .forEach(this::add);
    }

    @Nullable
    public Module getModule(Class<? extends Module> clazz) {
        return getContents().stream()
                .filter(module -> module.getClass() == clazz)
                .findFirst().orElse(null);
    }

    @Nullable
    public Module getModule(String name) {
        return getContents().stream()
                .filter(module -> module.getName().equalsIgnoreCase(name) || Arrays.asList(module.getAlias()).contains(name))
                .findFirst().orElse(null);
    }

    public List<Module> getModules(Category category) {
        return getContents().stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Module> getToggledModules() {
        return getContents().stream()
                .filter(Module::isToggled)
                .collect(Collectors.toList());
    }

    void registerModuleModes() {
        getContents().forEach(module -> Arrays.stream(module.getClass().getSuperclass().getDeclaredFields()).forEach(field -> {

            if (field.isAnnotationPresent(Name.class) && field.getType().isAssignableFrom(String.class) && !module.getModes().isEmpty()) {

                String name = field.getAnnotation(Name.class).value();
                List<String> modes = module.getModes().stream()
                        .map(ModuleMode::getName)
                        .collect(Collectors.toList());

                field.setAccessible(true);

                ModeSetting setting = new ModeSetting(name, module, field, modes);
                setting.setValue(modes.get(0));

                SETTING.add(setting);
            }

        }));
    }

    @EventTarget
    public void onKeyPress(KeyPressEvent e) {
        getContents().stream()
                .filter(module -> e.getKey() == module.getKeyCode())
                .forEach(Module::toggle);
    }

}