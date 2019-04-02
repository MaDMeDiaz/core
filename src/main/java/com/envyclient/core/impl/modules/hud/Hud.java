package com.envyclient.core.impl.modules.hud;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.module.data.Data;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.api.setting.data.Name;
import com.envyclient.core.impl.modules.hud.parts.Armor;
import com.envyclient.core.impl.modules.hud.parts.PotionEffects;
import com.envyclient.core.impl.modules.hud.parts.TabGui;
import com.envyclient.core.impl.modules.hud.parts.ToggledMods;
import me.ihaq.eventmanager.listener.EventListener;

import static com.envyclient.core.Envy.Managers.EVENT;

@Data(name = "Hud", category = Category.RENDER, alias = {"overlay"})
public class Hud extends Module {

    @Name("ArrayList")
    public boolean toggledMods = true;

    @Name("PotionEffects")
    public boolean potionEffects = true;

    @Name("Armor")
    public boolean armor = true;

    @Name("TabGui")
    public boolean tabGui = true;

    private final EventListener[] parts = {new ToggledMods(this), new PotionEffects(this), new Armor(this), new TabGui(this)};

    @Override
    public void onEnable() {
        super.onEnable();
        EVENT.register(parts);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EVENT.unregister(parts);
    }


}
