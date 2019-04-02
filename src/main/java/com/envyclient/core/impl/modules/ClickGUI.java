package com.envyclient.core.impl.modules;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.module.data.Data;
import com.envyclient.core.api.module.type.Category;
import org.lwjgl.input.Keyboard;

import static com.envyclient.core.Envy.Managers.CLICK_GUI;

@Data(name = "ClickGUI", category = Category.RENDER, keyCode = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(CLICK_GUI.getClickGUI());
    }

}