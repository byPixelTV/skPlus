package de.bypixeltv.notesk.elements.expressions

import ch.njol.skript.Skript
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ExpressionType
import ch.njol.skript.lang.SkriptParser
import ch.njol.skript.lang.util.SimpleExpression
import ch.njol.util.Kleenean
import de.bypixeltv.notesk.Main
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.jetbrains.annotations.Nullable


class ExprGetFadeDuration : SimpleExpression<Number>() {

    companion object {
        init {
            Skript.registerExpression(
                ExprGetFadeDuration::class.java, Number::class.java,
                ExpressionType.SIMPLE, "[(skmusic|nbs|notesk)] fade duration of [player] %player%"
            )
        }
    }

    private var player: Expression<Player>? = null

    override fun isSingle(): Boolean {
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean?,
        parseResult: SkriptParser.ParseResult?
    ): Boolean {
        this.player = exprs[0] as Expression<Player>?
        return true
    }

    @Suppress("DEPRECATION")
    @Nullable
    override fun get(e: Event?): Array<out Number?> {
        val p = player!!.getSingle(e)
        var duration = 0
        if (Main.songPlayers.containsKey(p)) {
            duration = Main.songPlayers[p]?.fadeDuration ?: 0
        }
        return arrayOf(duration)
    }

    override fun getReturnType(): Class<out Number> {
        return Number::class.java
    }

    override fun toString(e: Event?, debug: Boolean): String {
        return "[(skmusic|nbs|notesk)] fade duration of [player] %player%"
    }

}