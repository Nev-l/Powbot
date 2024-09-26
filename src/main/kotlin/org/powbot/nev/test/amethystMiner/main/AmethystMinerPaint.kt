package org.powbot.nev.amethystMiner.main

import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.paint.Paint
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.krulvis.api.extensions.items.Ore


class AmethystMinerPaint(script:AmethystMiner) {
}

    fun buildPaint(paintBuilder: PaintBuilder): Paint {
        paintBuilder
            .trackSkill(Skill.Mining)
            .trackInventoryItems(
                11374,
               *Ore.values().filter { it != Ore.PAY_DIRT }.flatMap { it.ids.toList() }.toIntArray()
            )
            .withTotalLoot(true)
        return paintBuilder.build()
    }
