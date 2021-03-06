package bits.bits.team;

import bits.bits.team.file.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Date;
import java.util.UUID;

/**
 * The type User.
 */
public class User {
  private FileManager file;
  private Main main;
  private UUID uuid;
  private boolean donor, guard;
  private String prefix;
  private String nickname;
  private Date randomTeleport;
  private Date beam;
  private Date nick;
  private UUID beamedFrom, beamedTo;
  private PermissionAttachment permissions;
  private boolean shopMode;
  private String root;

  /**
   * Instantiates a new User.
   *
   * @param file        the file
   * @param main        the main
   * @param uuid        the uuid
   * @param donor       the donor
   * @param guard       the guard
   * @param prefix      the prefix
   * @param nickname    the nickname
   * @param permissions the permissions
   */
  public User(FileManager file, Main main, UUID uuid, boolean donor, boolean guard, String prefix, String nickname,
              PermissionAttachment permissions) {
    this.file = file;
    this.main = main;
    this.uuid = uuid;
    this.donor = donor;
    this.guard = guard;
    this.prefix = prefix;
    this.nickname = nickname;
    this.permissions = permissions;

    root = "users." + uuid.toString();
  }

  /**
   * Gets uuid.
   *
   * @return the uuid
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Sets uuid.
   *
   * @param uuid the uuid
   */
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Is donor boolean.
   *
   * @return the boolean
   */
  public boolean isDonor() {
    return donor;
  }

  /**
   * Sets donor.
   *
   * @param donor the donor
   */
  public void setDonor(boolean donor) {
    this.donor = donor;
    file.write(root + ".donor", donor);

    if (donor) addDonorPermissions();
  }

  /**
   * Is guard boolean.
   *
   * @return the boolean
   */
  public boolean isGuard() {
    return guard;
  }

  /**
   * Sets guard.
   *
   * @param guard the guard
   */
  public void setGuard(boolean guard) {
    this.guard = guard;
    file.write(root + ".guard", guard);

    if (guard) addGuardPermissions();
  }

  /**
   * Gets prefix.
   *
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Sets prefix.
   *
   * @param prefix the prefix
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
    file.write(root + ".prefix", prefix);

    updateName();
  }

  /**
   * Gets nickname.
   *
   * @return the nickname
   */
  public String getNickname() {
    return nickname;
  }

  /**
   * Sets nickname.
   *
   * @param nickname the nickname
   */
  public void setNickname(String nickname) {
    this.nickname = nickname;
    file.write(root + ".nickname", nickname);

    updateName();
  }

  private void updateName() {
    Player player = main.getServer().getPlayer(uuid);
    if (player == null) return;

    String name = (nickname != null) ? "~" + nickname : player.getName();

    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', prefix + name + "&r"));
    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + name + "&r"));
  }

  /**
   * Gets random teleport.
   *
   * @return the random teleport
   */
  public Date getRandomTeleport() {
    return randomTeleport;
  }

  /**
   * Sets random teleport.
   *
   * @param randomTeleport the random teleport
   */
  public void setRandomTeleport(Date randomTeleport) {
    this.randomTeleport = randomTeleport;
  }

  /**
   * Gets beam.
   *
   * @return the beam
   */
  public Date getBeam() {
    return beam;
  }

  /**
   * Sets beam.
   *
   * @param beam the beam
   */
  public void setBeam(Date beam) {
    this.beam = beam;
  }

  /**
   * Gets nick.
   *
   * @return the nick
   */
  public Date getNick() {
    return nick;
  }

  /**
   * Sets nick.
   *
   * @param nick the nick
   */
  public void setNick(Date nick) {
    this.nick = nick;
  }

  /**
   * Gets beamed from.
   *
   * @return the beamed from
   */
  public UUID getBeamedFrom() {
    return beamedFrom;
  }

  /**
   * Sets beamed from.
   *
   * @param beamedFrom the beamed from
   */
  public void setBeamedFrom(UUID beamedFrom) {
    this.beamedFrom = beamedFrom;
  }

  /**
   * Gets beamed to.
   *
   * @return the beamed to
   */
  public UUID getBeamedTo() {
    return beamedTo;
  }

  /**
   * Sets beamed to.
   *
   * @param beamedTo the beamed to
   */
  public void setBeamedTo(UUID beamedTo) {
    this.beamedTo = beamedTo;
  }

  /**
   * Is shop mode boolean.
   *
   * @return the boolean
   */
  public boolean isShopMode() {
    return shopMode;
  }

  /**
   * Sets shop mode.
   *
   * @param shopMode the shop mode
   */
  public void setShopMode(boolean shopMode) {
    this.shopMode = shopMode;
  }

  private void addGuardPermissions() {
    Player player = main.getServer().getPlayer(uuid);
    if (player == null) return;

    permissions = player.addAttachment(main);
    permissions.setPermission(main.d().PERM_BYPASSCOOLDOWN, true);
    permissions.setPermission(main.d().PERM_COLOREDNAME, true);
    permissions.setPermission("minecraft.command.ban", true);
    permissions.setPermission("minecraft.command.kick", true);
    permissions.setPermission("minecraft.command.pardon", true);
    permissions.setPermission("coreprotect.lookup", true);
    permissions.setPermission("coreprotect.inspect", true);
    permissions.setPermission("griefprevention.softmute", true);
    permissions.setPermission("griefprevention.separate", true);
    permissions.setPermission("griefprevention.claimslistother", true);
  }

  private void addDonorPermissions() {
    Player player = main.getServer().getPlayer(uuid);
    if (player == null) return;

    permissions = player.addAttachment(main);
    permissions.setPermission(main.d().PERM_BYPASSCOOLDOWN, true);
    permissions.setPermission(main.d().PERM_COLOREDNAME, true);
  }

  /**
   * Join.
   */
  public void join() {
    updateName();
    if (donor) addDonorPermissions();
    if (guard) addGuardPermissions();
  }

  /**
   * Quit.
   */
  public void quit() {
    Player player = main.getServer().getPlayer(uuid);
    if (player == null) return;

    if (permissions == null) return;
    player.removeAttachment(permissions);
    permissions = null;
  }
}
