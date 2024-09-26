import org.powbot.api.Condition
import org.powbot.api.rt4.Chat
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Game.Tab
import org.powbot.api.Random
import org.powbot.mobile.script.ScriptManager
import org.powbot.nev.amethystMiner.utils.Task

class CutTask(private val cutAmethysts: Boolean, private val craftOptionSelected: String) : Task() {

    private val random = Random
    private var makeOption = 100
    private var checkedForChisel = false
    private val chiselID = 1755
    private val amethystID = 21347

    override fun activate(): Boolean {
        return cutAmethysts && Inventory.isFull()
    }

    override fun execute(): Boolean {
        if (makeOption == 100) {
            setMakeOption()
        }

        if (Game.tab() != Tab.INVENTORY) {
            Game.tab(Tab.INVENTORY)
            Condition.wait({ Game.tab() == Tab.INVENTORY }, 100, 5)
        }

        // Check if chisel is present in inventory
        if (!checkedForChisel && Inventory.stream().id(chiselID).isEmpty()) {
            println("You don't have a chisel in your inventory to cut amethysts!")
            ScriptManager.stop()
            return false
        }

        checkedForChisel = true

        // Use chisel on amethyst
        if (!Inventory.stream().id(chiselID).first().interact("Use")) {
            return false // If interaction fails, exit
        }

        Condition.sleep(100 + random.nextInt(200, 5)) // Small delay

        if (!Inventory.stream().id(amethystID).first().interact("Use")) {
            return false // If interaction fails, exit
        }

        // Wait for crafting dialog
        if (!Condition.wait({ Chat.chatting() }, 200, 25)) {
            println("Failed to open crafting dialog")
            return false
        }

        // Select crafting option (e.g., bolt tips, arrowtips)
        if (!Chat.completeChat(makeOption.toString())) {
            println("Failed to select crafting option")
            return false
        }

        // Wait until the amethyst is fully processed
        return Condition.wait({ Inventory.stream().id(amethystID).isEmpty() }, 200, 100)
    }

    private fun setMakeOption() {
        makeOption = when (craftOptionSelected) {
            "Amethyst bolt tips" -> 1
            "Amethyst arrowtips" -> 2
            "Amethyst javelin heads" -> 3
            "Amethyst dart tip" -> 4
            else -> {
                println("Incorrect script setup, please read the script guide!")
                100
            }
        }
    }
}
