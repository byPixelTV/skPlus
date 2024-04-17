package de.bypixeltv.notesk.elements.effects

import ch.njol.skript.Skript
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import de.bypixeltv.notesk.Main
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.jetbrains.annotations.Nullable


class EffStopNbsBroadcast : Effect() {

    private val miniMessages = MiniMessage.miniMessage()

    companion object{
        init {
            Skript.registerEffect(EffStopNbsBroadcast::class.java, "[(skmusic|nbs|notesk)] stop broadcast[ing] (song|music)")
        }
    }

    override fun init(
        expressions: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parser: SkriptParser.ParseResult
    ): Boolean {
        return true
    }

    override fun toString(@Nullable e: Event?, b: Boolean): String {
        return "(skmusic|nbs) stop broadcast[ing] (song|music)"
    }
    public override fun execute(e: Event?) {
        for (p in Bukkit.getOnlinePlayers()) {
            if (Main.songPlayers.containsKey(p)) {
                Main.songPlayers[p]?.isPlaying = false
                Main.songPlayers[p]?.destroy()
            }
        }
    }
}