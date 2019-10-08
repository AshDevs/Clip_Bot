package commands;

import bot.Constants;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.jagrosh.jdautilities.examples.doc.Author;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

@Author("Ashley Tonkin (HardlyAPro)")
public class FeedbackCommand extends Command {

  public FeedbackCommand() {
    this.name = "feedback";
    this.guildOnly = false;
    this.help = "Give your feedback to Corvidae anonymously";
    this.arguments = "[feedback]";
  }

  @Override
  protected void execute(CommandEvent event) {
    List<TextChannel> list = event.getGuild().getTextChannelsByName("feedback", true);
    if (!list.isEmpty()) {
      if (!event.getArgs().isEmpty()) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setDescription(event.getArgs());
        builder.setAuthor("Corvidae Feedback", null, event.getSelfUser().getAvatarUrl());
        builder.setTimestamp(event.getEvent().getMessage().getTimeCreated());
        TextChannel tc = list.get(0);
        tc.sendMessage(builder.build()).queue(
            m -> {
              event.getMessage().delete().queue();
              m.addReaction(Constants.THUMBSUP).queue();
              m.addReaction(Constants.THUMBSDOWN).queue();
            }
        );
      } else {
        event.reply("Usage: " + Constants.PREFIX + "feedback " + this.arguments);
      }
    } else {
      event.getGuild().createTextChannel("feedback").queue(
          textChannel -> event.replyError("I have created the correct channel for you! Enjoy")
      );
    }
  }
}
