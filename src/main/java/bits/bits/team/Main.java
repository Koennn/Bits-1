package bits.bits.team;

import bits.bits.team.command.*;
import bits.bits.team.data.Data;
import bits.bits.team.data.DataDiscord;
import bits.bits.team.data.DataUser;
import bits.bits.team.data.DataWarp;
import bits.bits.team.event.*;
import bits.bits.team.runnable.RunnableWorldEvent;
import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

/**
 * The type Main.
 *
 * @author freddedotme
 */
public class Main extends JavaPlugin {
  private Data data;

  @Override
  public void onEnable() {
    super.onEnable();

    data = new Data();
    DataDiscord dataDiscord = new DataDiscord(this);
    DataWarp dataWarp = new DataWarp(this);
    DataUser dataUser = new DataUser(this);

    new RunnableWorldEvent(this).runTaskTimerAsynchronously(this, 400, 288000);
    Discord discord = new Discord(this, dataDiscord);

    File database = new File(data.ROOT_PATH, "GeoLite2-Country.mmdb");
    Reader reader = null;
    try {
      reader = new Reader(database, new CHMCache());
    } catch (IOException e) {
      e.printStackTrace();
    }

    getServer().getPluginManager().registerEvents(new EventPlayerJoinQuit(this, dataUser, discord, reader), this);
    getServer().getPluginManager().registerEvents(new EventBedEnterLeave(this), this);
    getServer().getPluginManager().registerEvents(new EventCancelChunkUnload(dataWarp), this);
    getServer().getPluginManager().registerEvents(new EventSignColorize(), this);
    getServer().getPluginManager().registerEvents(new EventVote(this), this);
    getServer().getPluginManager().registerEvents(new EventFishSlap(), this);
    getServer().getPluginManager().registerEvents(new EventDisablePvP(), this);
    getServer().getPluginManager().registerEvents(new EventDiscord(discord), this);

    getCommand("bed").setExecutor(new CommandBed(this));

    getCommand("warp").setExecutor(new CommandWarp(this, dataWarp));
    getCommand("warps").setExecutor(new CommandWarps(dataWarp));
    getCommand("setwarp").setExecutor(new CommandSetWarp(this, dataWarp));
    getCommand("delwarp").setExecutor(new CommandDelWarp(this, dataWarp));

    getCommand("guards").setExecutor(new CommandGuards(dataUser));
    getCommand("donors").setExecutor(new CommandDonors(dataUser));
    getCommand("setguard").setExecutor(new CommandSetGuard(this, dataUser));
    getCommand("delguard").setExecutor(new CommandDelGuard(this, dataUser));
    getCommand("setdonor").setExecutor(new CommandSetDonor(this, dataUser));
    getCommand("deldonor").setExecutor(new CommandDelDonor(this, dataUser));

    getCommand("info").setExecutor(new CommandInfo(this));
    getCommand("colorname").setExecutor(new CommandColorName(this, dataUser));
    getCommand("vote").setExecutor(new CommandVote(this));
    getCommand("hat").setExecutor(new CommandHat());
    getCommand("donate").setExecutor(new CommandDonate(this));
    getCommand("seen").setExecutor(new CommandSeen(this));
    getCommand("joined").setExecutor(new CommandJoined(this));
    getCommand("broadcast").setExecutor(new CommandBroadcast(this, dataUser));
    getCommand("randomteleport").setExecutor(new CommandRandomTeleport(this, dataUser));
    getCommand("playerhead").setExecutor(new CommandPlayerHead(this));
    getCommand("beam").setExecutor(new CommandBeam(this, dataUser));
    getCommand("shop").setExecutor(new CommandShop(this, dataUser));
  }

  /**
   * D data.
   *
   * @return the data
   */
  public Data d() {
    return data;
  }

  /**
   * Invalid action boolean.
   *
   * @param player  the player
   * @param message the message
   * @return the boolean
   */
  public boolean invalidAction(Player player, String message) {
    player.sendMessage(message);
    return false;
  }

  /**
   * Teleport.
   *
   * @param player   the player
   * @param location the location
   */
  public void teleport(final Player player, final Location location) {
    location.getChunk().load(true);
    player.sendMessage(data.NEUTRAL_TELEPORTING);
    player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 250, 0.5, 0.5, 0.5);
    player.getWorld().spawnParticle(Particle.PORTAL, location, 250, 0.5, 0.5, 0.5);
    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 0.2F, 1.5F);
    new BukkitRunnable() {
      @Override
      public void run() {
        player.teleport(location);
        player.sendMessage(data.POSITIVE_TELEPORTED);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 250, 0.5, 0.5, 0.5);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 0.1, 0.1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.2F, 0.8F);
      }
    }.runTaskLater(this, (player.hasPermission(data.PERM_BYPASSCOOLDOWN) ? data.TELEPORT_WARMUP_DONOR : data
      .TELEPORT_WARMUP));
  }
}
