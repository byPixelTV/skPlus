package de.bypixeltv.notesk.elements.effects

import ch.njol.skript.Skript
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import com.xxmicloxx.NoteBlockAPI.model.FadeType
import de.bypixeltv.notesk.Main
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.jetbrains.annotations.Nullable


class EffSetFadeDuration : Effect() {

    private val miniMessages = MiniMessage.miniMessage()

    companion object{
        init {
            Skript.registerEffect(EffSetFadeDuration::class.java, "(skmusic|nbs) set fade duration of [player] %player% to %integer%")
        }
    }

    private var dur: Expression<Int>? = null
    private var player: Expression<Player>? = null

    @Suppress("UNCHECKED_CAST")
    override fun init(
        expressions: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parser: SkriptParser.ParseResult
    ): Boolean {
        this.player = expressions[0] as Expression<Player>
        this.dur = expressions[1] as Expression<Int>
        return true
    }

    override fun toString(@Nullable e: Event?, b: Boolean): String {
        return "(skmusic|nbs) set fade duration of [player] %player% to %integer%"
    }

    @Suppress("DEPRECATION")
    public override fun execute(e: Event?) {
        val p = player!!.getSingle(e)
        val duration = dur!!.getSingle(e)!!
        if (Main.songPlayers.containsKey(p)) {
            Main.songPlayers[p]?.fadeType = FadeType.LINEAR
            Main.songPlayers[p]?.fadeDuration = duration
        }
    }
}