package bits.bits.team.data;

import bits.bits.team.Main;
import bits.bits.team.file.FileManager;

/**
 * The type Data discord.
 */
public class DataDiscord {
  private String name;
  private String token;
  private long id;

  /**
   * Instantiates a new Data discord.
   *
   * @param main the main
   */
  public DataDiscord(Main main) {
    String DEFAULT_NAME = "Bot";
    String DEFAULT_TOKEN = "42";
    long DEFAULT_ID = 42L;

    FileManager file = new FileManager(main, "discord.yml");
    file.write("version", main.getDescription().getVersion());

    if (file.read("name") == null) file.write("name", DEFAULT_NAME);
    if (file.read("token") == null) file.write("token", DEFAULT_TOKEN);
    if (file.read("id") == null) file.write("id", DEFAULT_ID);

    name = (String) file.read("name");
    token = (String) file.read("token");
    id = (long) file.read("id");
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets token.
   *
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets token.
   *
   * @param token the token
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(long id) {
    this.id = id;
  }
}
