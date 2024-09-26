package org.powbot.nev.amethystMiner.main

import org.powbot.api.Condition
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Camera
import org.powbot.api.script.AbstractScript
import org.powbot.api.script.OptionType
import org.powbot.api.script.ScriptManifest
import org.powbot.api.script.ScriptConfiguration


@ScriptManifest(
    name = "Amethyst Miner",
    description = "Mines amethyst in the Mining Guild",
    version = "1.0"
)
@ScriptConfiguration.List(
    [
        ScriptConfiguration(
            name = "Cut the amethyst",
            description = "Cut the amethyst instead of banking them",
            optionType = OptionType.BOOLEAN,
            defaultValue = "false"
        ),
        ScriptConfiguration(
            name = "Which craft option?",
            description = "If cutting, which craft option would you like to do?",
            optionType = OptionType.STRING,
            defaultValue = "Amethyst bolt tips",
            allowedValues = arrayOf(
                "Amethyst bolt tips",
                "Amethyst arrowtips",
                "Amethyst javelin heads",
                "Amethyst dart tip"
            )
        ),
        ScriptConfiguration(
            name = "Use world hopper?",
            description = "Would you like to hop worlds based on your hop profile settings?",
            optionType = OptionType.BOOLEAN
        )
    ]
)
class AmethystMiner : AbstractScript() {

    override fun poll() {
        // Check if the player is mining or inventory is full
        if (Players.local().animation() == -1 && !Inventory.isFull()) mineAmethyst() else if (Inventory.isFull()) {
        }
    }

    private fun mineAmethyst() {
        val amethystRock = Objects.stream().name("Amethyst").nearest().first()

        if (amethystRock.valid()) {
            if (amethystRock.inViewport()) {
                amethystRock.interact("Mine")
                Condition.wait({ Players.local().animation() != -1 }, 200, 10)
            } else {
                Camera.turnTo(amethystRock)
                Movement.step(amethystRock)
            }
        }
    }


    override fun onStop() {
        println("Script Stopped.")
    }
}

fun main() {
    AmethystMiner().startScript("127.0.0.1", "GIM", false)
}
