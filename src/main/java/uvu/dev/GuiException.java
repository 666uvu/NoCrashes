package uvu.dev;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

public class GuiException extends GuiScreen
{
    private final RuntimeException exception;

    public GuiException(RuntimeException exception)
    {
        this.exception = exception;
    }

    @Override
    public void updateScreen()
    {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 132, "Back to title screen."));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled && button.id == 0) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "An exception has occurred!", this.width / 2, this.height / 4 - 40, 0xFFFFFF);
        this.drawString(this.fontRenderer, "Minecraft ran into an unexpected problem:", this.width / 2 - 140, this.height / 4 + 60, 0xA0A0A0);
        this.drawCenteredString(this.fontRenderer, this.exception.toString(), this.width / 2, this.height / 4 + 87, 0xFFFF00);

        if (this.exception.getStackTrace().length > 0)
        {
            this.drawCenteredString(this.fontRenderer, "   at " + this.exception.getStackTrace()[0].toString(), this.width / 2, this.height / 4 + 95, 0xFFFF00);
        }

        this.drawString(this.fontRenderer, "To prevent more problems, the current game has quit.", this.width / 2 - 140, this.height / 4 + 120, 0xA0A0A0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}