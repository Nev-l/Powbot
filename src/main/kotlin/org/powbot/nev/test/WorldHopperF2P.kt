package org.powbot.nev.test

import org.powbot.api.rt4.Worlds
import org.powbot.api.rt4.World

object WorldHopperF2P {

    fun hopToRandomF2PWorld() {
        val currentWorldId = Worlds.current().id()
        val random = Worlds.stream()
            .filter {
                it.type() == World.Type.FREE &&
                        it.population >= 15 &&
                        it.id() != currentWorldId &&
                        it.specialty() != World.Specialty.BOUNTY_HUNTER &&
                        it.specialty() != World.Specialty.PVP &&
                        it.specialty() != World.Specialty.TARGET_WORLD &&
                        it.specialty() != World.Specialty.PVP_ARENA &&
                        it.specialty() != World.Specialty.DEAD_MAN &&
                        it.specialty() != World.Specialty.BETA &&
                        it.specialty() != World.Specialty.HIGH_RISK &&
                        it.specialty() != World.Specialty.LEAGUE &&
                        it.specialty() != World.Specialty.SKILL_REQUIREMENT &&
                        it.specialty() != World.Specialty.TWISTED_LEAGUE &&
                        it.specialty() != World.Specialty.SPEEDRUNNING &&
                        it.specialty() != World.Specialty.FRESH_START &&
                        it.specialty() != World.Specialty.TRADE
            }
            .toList().randomOrNull()

        random?.hop()
    }
}