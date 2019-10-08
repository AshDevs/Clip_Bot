package event;

import static bot.Corvidae.scheduler;
import static util.ConnectTwitchAPI.twitchClipsData;
import static util.ConnectTwitchAPI.twitchConnection;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClipsEvent implements EventListener {

  @Override
  public void onEvent(@Nonnull GenericEvent event) {
    if (event instanceof ReadyEvent) {
      Runnable getClips = () -> {
        String team_data = twitchConnection("teams/corvidaeinc");
        Guild guild = event.getJDA().getGuildById(event.getJDA().getGuilds().get(0).getIdLong());
        List<TextChannel> list = Objects.requireNonNull(guild)
            .getTextChannelsByName("highlights", true);
        try {
          JSONObject team = new JSONObject(team_data);
          JSONArray teamArray = team.getJSONArray("users");

          for (int x = 0; x < teamArray.length(); x++) {
            JSONObject teamInfo = teamArray.getJSONObject(x);
            String clip_data = twitchClipsData("clips/top", teamInfo.getString("name"));
            JSONObject clips = new JSONObject(clip_data);
            JSONArray clipsArray = clips.getJSONArray("clips");

            for (int i = 0; i < clipsArray.length(); i++) {
              JSONObject clip = clipsArray.getJSONObject(i);
              JSONObject broadcaster = clip.getJSONObject("broadcaster");
              JSONObject curator = clip.getJSONObject("curator");
              JSONObject thumbnails = clip.getJSONObject("thumbnails");
              EmbedBuilder builder = new EmbedBuilder();
              builder.setTitle(broadcaster.getString("display_name"),
                  broadcaster.getString("channel_url"));
              builder.setColor(new Color(12390624));
              builder.setTimestamp(OffsetDateTime.parse(clip.getString("created_at")));
              builder.setFooter(curator.getString("display_name"), curator.getString("logo"));
              builder.setThumbnail(broadcaster.getString("logo"));
              builder
                  .setAuthor("Corvidae Inc.", null, event.getJDA().getSelfUser().getAvatarUrl());
              builder.addField("Clip Title", clip.getString("title"), false);
              builder.addField("Game", clip.getString("game"), false);
              builder
                  .addField("Clip", "https://clips.twitch.tv/" + clip.getString("slug"), false);
              builder.setImage(thumbnails.getString("medium"));
              TextChannel tc = list.get(0);
              tc.sendMessage(builder.build()).queue();
            }
          }
        } catch (JSONException ex) {
          ex.printStackTrace();
        }
      };
      Guild guild = event.getJDA().getGuildById(event.getJDA().getGuilds().get(0).getIdLong());
      List<TextChannel> list = Objects.requireNonNull(guild)
          .getTextChannelsByName("highlights", true);
      if (list.isEmpty()) {
        guild.createTextChannel("highlights").queue(
            e -> scheduler.scheduleWithFixedDelay(getClips, 0, 24 * 60, TimeUnit.MINUTES)
        );
      }
    }
  }
}
