package org.powbot.nev.test

import org.powbot.api.Area
import org.powbot.api.Condition
import org.powbot.api.Tile
import org.powbot.api.rt4.*
import org.powbot.api.rt4.Inventory.stream
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.AbstractScript
import org.powbot.api.script.OptionType
import org.powbot.api.script.ScriptConfiguration
import org.powbot.api.script.ScriptManifest
import org.powbot.api.script.paint.PaintBuilder
import java.lang.Boolean.getBoolean
import java.util.concurrent.Callable
import kotlin.random.Random

@ScriptManifest(
	name = "BonesLooter",
	description = "Loots and buries bones, then hops to another random F2P world",
	version = "3.1.5",
	author = "Nev"
)
@ScriptConfiguration(
	name = "enableWorldHopping",
	description = "Enable or disable world hopping if no bones are available",
	optionType = OptionType.BOOLEAN,
	defaultValue = "false"
)
class BonesLooter : AbstractScript() {

	private val boneId = 526

	private var boneAREA: Area = Area(
		Tile(3237, 3614, 0),
		Tile(3234, 3611, 0),
		Tile(3234, 3606, 0),
		Tile(3237, 3603, 0),
		Tile(3242, 3603, 0),
		Tile(3245, 3606, 0),
		Tile(3245, 3611, 0)
	)

	private var lastPosition: Tile? = null

	override fun poll() {
		if (!bonesAvailable() && getBoolean("enableWorldHopping")) {
			println("No bones available, hopping to a random F2P world.")
			hopToRandomF2PWorld()
			lastPosition = Players.local().tile()
			if (!waitForMovement()) {
				println("Bot did not move after hopping, restarting loop.")
				return
			}
		} else {
			lootBones()
			buryBones()
			val randomEnergyThreshold = Random.nextInt(35, 76) // Random number between 35 and 75
			if (Movement.energyLevel() > randomEnergyThreshold && !Movement.running()) {
				println("Enabling running as energy level is above $randomEnergyThreshold.")
				Movement.running(true) // Enable running
			}
		}
	}

	override fun onStart() {
		val paint = PaintBuilder.newBuilder()
			.x(40)
			.y(80)
			.trackSkill(Skill.Prayer)
			.build()
		addPaint(paint)
		println("Script started.")
	}

	private fun bonesAvailable(): Boolean {
		val available = GroundItems.stream()
			.id(boneId)
			.filter { boneAREA.contains(it.tile()) }
			.firstOrNull() != null
		println("Bones available: $available")
		return available
	}

	private fun lootBones() {
		val bones = GroundItems.stream()
			.id(boneId)
			.nearest()
			.filter { boneAREA.contains(it.tile()) }
			.sortedBy { it.tile().distanceTo(Players.local().tile()) }
			.firstOrNull()
		bones?.let {
			if (it.interact("Take")) {
				println("Looting bones.")
				Condition.sleep(1000) // Wait for the bones to be picked up
				Condition.wait({ Inventory.isFull() }, 10, 50)
			}
		}
	}

	private fun buryBones() {
		val bones: Item? = Inventory.stream().id(526).firstOrNull()
		bones?.let {
			if (it.interact("Bury")) {
				println("Burying bones.")
				Condition.sleep(500) // Wait for the bones to be buried
				Condition.wait({ stream().id(526).isNotEmpty() }, 10, 50)
			}
		}
	}

	private fun hopToRandomF2PWorld() {
		val random = Worlds.stream()
			.filter { it.type() == World.Type.FREE && it.population >= 15 && it.specialty() != World.Specialty.BOUNTY_HUNTER
					&& it.specialty() != World.Specialty.PVP
					&& it.specialty() != World.Specialty.TARGET_WORLD
					&& it.specialty() != World.Specialty.PVP_ARENA
					&& it.specialty() != World.Specialty.DEAD_MAN
					&& it.specialty() != World.Specialty.BETA
					&& it.specialty() != World.Specialty.HIGH_RISK
					&& it.specialty() != World.Specialty.LEAGUE
					&& it.specialty() != World.Specialty.SKILL_REQUIREMENT
					&& it.specialty() != World.Specialty.TWISTED_LEAGUE
					&& it.specialty() != World.Specialty.SPEEDRUNNING
					&& it.specialty() != World.Specialty.FRESH_START
					&& it.specialty() != World.Specialty.TRADE }
			.toList().random()
		println("Hopping to world ${random.number}.")
		random.hop()
	}

	private fun waitForMovement(): Boolean {
		val moved = Condition.wait(Callable {
			Players.local().tile() != lastPosition
		}, 500, 10)
		if (moved) {
			logger.info(/* p0 = */ "Bot has moved to a new position.")
		} else {
			println("Bot did not move after hopping.")
		}
		return moved
	}
}

fun main() {
	val script = BonesLooter()
	script.startScript("localhost", "", true)
}