package org.powbot.nev.test

import org.powbot.api.script.paint.Paint
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.api.rt4.walking.model.Skill

    fun buildPaint(paintBuilder: PaintBuilder): Paint {
        return paintBuilder
            .trackInventoryItem(526, "Bones")
            .trackSkill(Skill.Prayer)
            .build()
    }