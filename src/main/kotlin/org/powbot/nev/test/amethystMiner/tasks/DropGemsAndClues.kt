import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Item
import org.powbot.nev.amethystMiner.utils.Task

class DropGemsAndClues : Task() {

    private val gemIDs = intArrayOf(1625, 1627, 1629, 1623, 1621, 1619, 1617)
    private val clueIDs = intArrayOf(23442, 20358, 20360, 20362, 20364)

    override fun activate(): Boolean {
        return Inventory.isFull() && (Inventory.stream().id(*clueIDs).isNotEmpty() || Inventory.stream().id(*gemIDs).isNotEmpty())
    }

    override fun execute(): Boolean {
        dropAllClues()
        dropAllGems()
        return true
    }

    private fun dropAllGems() {
        gemIDs.forEach { id ->
            Inventory.stream().id(id).forEach { item: Item ->
                item.interact("Drop")
            }
        }
    }

    private fun dropAllClues() {
        clueIDs.forEach { id ->
            Inventory.stream().id(id).forEach { item: Item ->
                item.interact("Drop")
            }
        }
    }
}
