import org.powbot.api.Condition
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Game.Tab
import org.powbot.api.rt4.Equipment.Slot
import org.powbot.mobile.script.ScriptManager
import org.powbot.nev.amethystMiner.utils.Task

class CheckPickaxe : Task() {

    private val pickaxeIDs = intArrayOf(20014, 13243, 13244, 23680, 23682, 11920, 1275, 23276, 1271, 1273, 12297, 1269, 1267, 1265)
    private var checkedForPickaxe = false

    override fun activate(): Boolean {
        return !checkedForPickaxe
    }

    override fun execute(): Boolean {
        if (Game.tab() != Tab.INVENTORY) {
            Game.tab(Tab.INVENTORY)
            Condition.wait({ Game.tab() == Tab.INVENTORY }, 50, 10)
        }

        if (Inventory.stream().id(*pickaxeIDs).isNotEmpty()) {
            checkedForPickaxe = true
            println("Pickaxe found in inventory, continuing...")
            return true
        }

        if (Game.tab() != Tab.EQUIPMENT) {
            Game.tab(Tab.EQUIPMENT)
            Condition.wait({ Game.tab() == Tab.EQUIPMENT }, 50, 10)
        }

        if (Equipment.itemAt(Slot.MAIN_HAND).id() in pickaxeIDs) {
            checkedForPickaxe = true
            println("Pickaxe equipped, continuing...")
            return true
        }

        println("Pickaxe not found, stopping script.")
        ScriptManager.stop()
        return false
    }
}
