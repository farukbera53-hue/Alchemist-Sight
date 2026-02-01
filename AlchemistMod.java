package com.egitim.alchemist;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AlchemistMod implements ClientModInitializer {
    // Tuş ataması: M harfi
    private static KeyBinding keyBinding;
    private static boolean isActive = false;

    @Override
    public void onInitializeClient() {
        // Tuşu kaydediyoruz
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.alchemist.toggle", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_M, 
                "category.alchemist"
        ));

        // Her tick sonunda çalışacak döngü
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // M tuşuna basılıp basılmadığını kontrol et
            while (keyBinding.wasPressed()) {
                isActive = !isActive;
                client.player.sendMessage(Text.of("§6Simyacı Görüşü: " + (isActive ? "§aAÇIK" : "§cKAPALI")), true);
            }

            // Eğer hile aktifse çevredeki varlıkları parlat
            if (isActive) {
                for (Entity entity : client.world.getEntities()) {
                    if (entity != client.player && entity.distanceTo(client.player) < 20) {
                        entity.setGlowing(true); // Glow efekti verir
                    }
                }
            }
        });
    }
}
