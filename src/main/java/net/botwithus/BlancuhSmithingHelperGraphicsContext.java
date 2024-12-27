package net.botwithus;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class BlancuhSmithingHelperGraphicsContext extends ScriptGraphicsContext {

    private BlancuhSmithingHelper script;

    public BlancuhSmithingHelperGraphicsContext(ScriptConsole scriptConsole, BlancuhSmithingHelper script) {
        super(scriptConsole);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        if (ImGui.Begin("Blancuh Smithing Helper", ImGuiWindowFlag.None.getValue())) {
            if (ImGui.BeginTabBar("My bar", ImGuiWindowFlag.None.getValue())) {
                if (ImGui.BeginTabItem("Settings", ImGuiWindowFlag.None.getValue())) {
                    script.setBurial(ImGui.Checkbox("Use burial anvil/forge?", script.isBurial()));
                    ImGui.Text("Currently: " + script.getBotState());
                    ImGui.EndTabItem();
                }
                ImGui.EndTabBar();
            }
            ImGui.End();
        }

    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
