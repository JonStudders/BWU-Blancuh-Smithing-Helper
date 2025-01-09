package net.botwithus;

import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.events.impl.ChatMessageEvent;

import java.util.Random;

public class BlancuhSmithingHelper extends LoopingScript {

    private BotState botState = BotState.IDLE;
    private boolean useBurial = false;
    private Random random = new Random();

    enum BotState {
        IDLE,
        SMITHING,
        HEATING,
        WORKING
    }

    public BlancuhSmithingHelper(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new BlancuhSmithingHelperGraphicsContext(getConsole(), this);
        subscribe(ChatMessageEvent.class, chatMessageEvent -> {
            if (chatMessageEvent.getMessage().contains("Your item has cooled down slightly")) {
                println("Need to heatup!");
                setBotState(BotState.HEATING);
            } else if (chatMessageEvent.getMessage().contains("Your unfinished item is at full heat:")) {
                println("Need to smith!");
                setBotState(BotState.SMITHING);
            } else if (chatMessageEvent.getMessage().contains("You finished smithing:")) {
                println("Item completed.");
                setBotState(BotState.HEATING);
            }
        });
    }

    @Override
    public void onLoop() {
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN || botState == BotState.IDLE) {
            Execution.delay(random.nextLong(3000,7000));
            return;
        }
        switch (botState) {
            case IDLE -> {
                println("We're idle!");
                Execution.delay(random.nextLong(1000,3000));
            }
            case SMITHING -> {
                // Interact 'Smith' closest anvil
                Execution.delay(handleSmithing(player));
            }
            case HEATING -> {
                // Interact 'Heat' closest furnace
                Execution.delay(handleHeating(player));
            }
            case WORKING -> {
                println("We're working!");
                Execution.delay(random.nextLong(1000,3000));
            }
        }
    }

    private long handleSmithing(LocalPlayer player) {
        String anvilType = isBurial() ? "Burial Anvil" : "Anvil";
        SceneObject closestAnvil = SceneObjectQuery.newQuery().name(anvilType).option("Smith").results().nearest();

        if (closestAnvil != null ) {
            println("Found anvil.");
            println("Interacted anvil: " + closestAnvil.interact("Smith"));
            setBotState(BotState.WORKING);
        } else {
            println("Did not find anvil.");
        }
        return random.nextLong(1500,3000);
    }

    private long handleHeating(LocalPlayer player) {
        String forgeType = isBurial() ? "Burial Forge" : "Forge";
        SceneObject closestForge = SceneObjectQuery.newQuery().name(forgeType).option("Heat").results().nearest();

        if (closestForge != null ) {
            println("Found forge.");
            println("Interacted forge: " + closestForge.interact("Heat"));
            setBotState(BotState.WORKING);
        } else {
            println("Did not find forge.");
        }
        return random.nextLong(1500,3000);
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public boolean isBurial() {
        return useBurial;
    }

    public void setBurial(boolean useBurial) {
        this.useBurial = useBurial;
    }
}
