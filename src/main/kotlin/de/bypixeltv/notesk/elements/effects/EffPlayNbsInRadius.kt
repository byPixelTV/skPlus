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
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.jetbrains.annotations.Nullable
import java.io.File


class EffPlayNbsInRadius : Effect() {

    private val miniMessages = MiniMessage.miniMessage()

    companion object{
        init {
            Skript.registerEffect(EffPlayNbsInRadius::class.java, "[(skmusic|nbs|notesk)] play (song|music) %string% to [all] players in radius %integer% arround [location] %location%")
        }
    }

    private var song: Expression<String>? = null
    private var loc: Expression<Location>? = null
    private var rad: Expression<Int>? = null

    @Suppress("UNCHECKED_CAST")
    override fun init(
        expressions: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parser: SkriptParser.ParseResult
    ): Boolean {
        this.song = expressions[0] as Expression<String>
        this.loc = expressions[1] as Expression<Location>
        this.rad = expressions[2] as Expression<Int>
        return true
    }

    override fun toString(@Nullable e: Event?, b: Boolean): String {
        return "[(skmusic|nbs|notesk)] play (song|music) %string% to [all] players in radius %integer% arround [location] %location%"
    }
    public override fun execute(e: Event?) {
        val location: Location? = loc?.getSingle(e)
        val radius: Int? = rad?.getSingle(e)
        var fileName: String = song?.getSingle(e).toString()
        if (!fileName.contains(".nbs")) {
            fileName = "$fileName.nbs"
        }
        val music: File = File(Main.INSTANCE.dataFolder, "songs/$fileName")
        if (!music.exists()) {
            Main.INSTANCE.server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#ff0000>Error while trying to load the song <yellow>$fileName</yellow>! Does the file exist?"))
        } else {
            for (p in Bukkit.getOnlinePlayers()) {
                if (p.location.distance(location!!) <= radius!!) {
                    val s: Song = NBSDecoder.parse(music)
                    val sp: SongPlayer = RadioSongPlayer(s)
                    if (Main.songPlayers.containsKey(p)) {
                        Main.songPlayers[p]?.destroy()
                        Main.songPlayers.replace(p, sp)
                    } else {
                        Main.songPlayers[p] = sp
                    }
                    sp.autoDestroy = true
                    sp.addPlayer(p)
                    sp.isPlaying = true
                }
            }
        }
    }
}