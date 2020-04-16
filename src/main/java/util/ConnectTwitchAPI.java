package util;

import kong.unirest.Unirest;

public class ConnectTwitchAPI {

  private static final String twitchAPI_ClientID = "20dspkkdh2nwlet07s9vm9zt3f8vg1";
  private static String V5APILink = "https://api.twitch.tv/kraken/";
  private static String NewAPILink = "https://api.twitch.tv/helix/";

  public static String twitchConnection(String url) {
    return Unirest.get(V5APILink + url)
        .header("Client-ID", twitchAPI_ClientID)
        .header("Accept", "application/vnd.twitchtv.v5+json")
        .asString()
        .getBody();
  }

  public static String twitchClipsData(String url, String channel) {
    return Unirest.get(V5APILink + url)
        .header("Client-ID", twitchAPI_ClientID)
        .header("Accept", "application/vnd.twitchtv.v5+json")
        .queryString("channel", channel)
        .queryString("period", "day")
        .asString()
        .getBody();
  }

}
