/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.GameTickEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.Module
import kotlin.math.max
import kotlin.random.Random

object Reach : Module("Reach", Category.PLAYER) {

    val combatReach by float("CombatReach", 3.5f, 3f..7f)
    val buildReach by float("BuildReach", 5f, 4.5f..7f)

    val maxRange
        get() = max(combatReach, buildReach)

    // Randomness: each tick, pick a probability from within the configured range,
    // then use that probability to decide whether reach is active this tick.
    // This makes reach activation unpredictable and harder to detect.
    private val randomnessEnabled by boolean("Randomness", false)
    val randomnessChance by floatRange("RandomnessChance", 50f..80f, 0f..100f)

    @Volatile
    private var reachActive = true

    fun shouldApplyReach() = handleEvents() && reachActive

    @Suppress("unused")
    private val tickHandler = handler<GameTickEvent> {
        reachActive = if (randomnessEnabled) {
            val chosenChance = Random.nextFloat() *
                (randomnessChance.endInclusive - randomnessChance.start) + randomnessChance.start
            Random.nextFloat() * 100f <= chosenChance
        } else {
            true
        }
    }
}
