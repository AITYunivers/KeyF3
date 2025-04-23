package io.github.yunivers.keyf3.client.gui.screen;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class SilentPauseScreen extends GameMenuScreen
{
    private boolean escapeDebounce;

    @Override
    public void init()
    {
        this.saveStep = 0;
        this.buttons.clear();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        boolean failedSave = !this.minecraft.world.attemptSaving(this.saveStep++);
        if (failedSave || this.ticks < 20)
        {
            float anim = MathHelper.sin(((float)(this.ticks % 10) + delta) / 10.0F * (float) Math.PI * 2.0F) * 0.2F + 0.8F;
            int color = (int)(255.0F * anim);
            this.drawTextWithShadow(this.textRenderer, "Saving level..", 8, this.height - 16, color << 16 | color << 8 | color);
        }

        if (!escapeDebounce)
            escapeDebounce = !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);

        this.drawCenteredTextWithShadow(this.textRenderer, "Game paused", this.width / 2, 40, 0xFFFFFF);
    }

    @Override
    protected void keyPressed(char character, int keyCode)
    {
        if (escapeDebounce)
            super.keyPressed(character, keyCode);
    }
}
