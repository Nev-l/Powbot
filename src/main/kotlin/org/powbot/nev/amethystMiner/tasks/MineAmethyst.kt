            import org.powbot.api.Condition
                    import org.powbot.api.rt4.Inventory
                    import org.powbot.api.rt4.Movement
                    import org.powbot.api.rt4.Players
                    import org.powbot.api.rt4.Objects
                    import org.powbot.api.rt4.GameObject
                    import org.powbot.api.Tile
                    import org.powbot.nev.amethystMiner.utils.Task
                    import java.time.Instant
                    import java.time.Duration

            class MineAmethyst(private val bankArea: org.powbot.api.Area, private val toMinePath: Array<Tile>) : Task() {

                private val amethystRockID = 11374
                private var lastLocation: Tile? = null
                private var lastCheckTime: Instant = Instant.now()

                override fun activate(): Boolean {
                    return !Inventory.isFull() && bankArea.contains(Players.local().tile())
                }

                override fun execute(): Boolean {
                    if (!Inventory.isFull() && bankArea.contains(Players.local().tile())) {
                        Movement.newTilePath(*toMinePath).traverse()
                    }

                    while (!Inventory.isFull()) {
                        val amethystRock = Objects.stream().id(amethystRockID).nearest().first()
                        if (amethystRock.valid()) {
                            mineAmethyst(amethystRock)
                        }
                    }
                    return true
                }

                private fun mineAmethyst(rock: GameObject) {
                    rock.interact("Mine")
                    lastLocation = null
                    Condition.wait({ !playerIsMoving() }, 200, 30)
                }

                private fun playerIsMoving(): Boolean {
                    val currentLocation = Players.local().tile()

                    if (lastLocation != null && lastLocation == currentLocation) {
                        val elapsedMillis = Duration.between(lastCheckTime, Instant.now()).toMillis()
                        if (elapsedMillis >= 500) {
                            return false
                        }
                    } else {
                        lastCheckTime = Instant.now()
                        lastLocation = currentLocation
                    }
                    return true
                }
            }
