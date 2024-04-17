package de.bypixeltv.notesk.elements.effects

import ch.njol.skript.Skript
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import com.xxmicloxx.NoteBlockAPI.model.Song
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import de.bypixeltv.notesk.Main
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.jetbrains.annotations.Nullable
import java.io.File


class EffBroadcastNbs : Effect() {

    private val miniMessages = MiniMessage.miniMessage()

    companion object{
        init {
            Skript.registerEffect(EffBroadcastNbs::class.java, "[(skmusic|nbs|notesk)] broadcast (song|music) %string%")
        }
    }

    private var song: Expression<String>? = null

    @Suppress("UNCHECKED_CAST")
    override fun init(
        expressions: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parser: SkriptParser.ParseResult
    ): Boolean {
        this.song = expressions[0] as Expression<String>
        return true
    }

    override fun toString(@Nullable e: Event?, b: Boolean): String {
        return "[(skmusic|nbs)] broadcast (song|music) %string%"
    }
    public override fun execute(e: Event?) {
        var fileName: String = song?.getSingle(e).toString()
        if (!fileName.contains(".nbs")) {
            fileName = "$fileName.nbs"
        }
        val music: File = File(Main.INSTANCE.dataFolder, "songs/$fileName")
        if (!music.exists()) {
            Main.INSTANCE.server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#ff0000>Error while trying to load the song <yellow>$fileName</yellow>! Does the file exist?"))
        } else {
            for (p: Player? in Bukkit.getOnlinePlayers()) {
                val s: Song = NBSDecoder.parse(music)
                val sp: SongPlayer = RadioSongPlayer(s)
                if (Main.songPlayers.containsKey(p)) {
                    Main.songPlayers[p]?.destroy()
                    if (p != null) {
                        Main.songPlayers.replace(p, sp)
                    }
                } else {
                    if (p != null) {
                        Main.songPlayers[p] = sp
                    }
                }
                sp.addPlayer(p)
                sp.autoDestroy = true
                sp.isPlaying = true
            }
        }
    }
}