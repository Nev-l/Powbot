import org.powbot.api.Tile
import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.Camera
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Objects
import org.powbot.nev.amethystMiner.utils.Task

class Bank(private val cutAmethysts: Boolean) : Task() {

    private val bankTile = Tile(2129, 210)
    private val bankArea = org.powbot.api.Area(Tile(2124, 205), Tile(2134, 216))

    override fun activate(): Boolean {
        return !cutAmethysts && Inventory.isFull()
    }

    override fun execute(): Boolean {
        val location = Players.local().tile()

        if (!bankArea.contains(location)) {
            walkToBank()
            return true
        }

        handleBanking()
        return true
    }

    private fun walkToBank() {
        if (!Movement.step(bankTile)) {
            Camera.turnTo(bankTile)
        }
        Condition.wait({ bankArea.contains(Players.local().tile()) }, 200, 10)
    }

    private fun handleBanking() {
        if (!Bank.opened()) {
            val nearestBank: GameObject = Objects.stream().name("Bank booth").nearest().first()
            if (nearestBank.valid()) {
                nearestBank.interact("Bank")
                Condition.wait({ Bank.opened() }, 50, 10)
            }
        }

        if (Bank.opened()) {
            if (!Bank.depositAllExcept("Pickaxe")) {
                println("Depositing ores.")
            }
            Condition.wait({ !Inventory.isFull() }, 200, 10)
            Bank.close()
        }
    }
}
