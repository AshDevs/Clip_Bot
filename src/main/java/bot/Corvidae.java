package bot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.FeedbackCommand;
import config.Config;
import config.PropertiesBasedLoader;
import event.getClipsEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Corvidae {

  public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public static void main(String[] args) throws LoginException {

    Config config = PropertiesBasedLoader.load_from_properties("config.properties");
    EventWaiter waiter = new EventWaiter();
    CommandClientBuilder client = new CommandClientBuilder();
    client.setStatus(OnlineStatus.ONLINE);
    client.setActivity(Activity.streaming("Corvidae Inc.", Constants.TWITCH));
    client.setOwnerId(Constants.OWNERID);
    client.setEmojis(Constants.SUCCESS, Constants.WARNING, Constants.ERROR);
    client.setPrefix(Constants.PREFIX);
    client.addCommands(
        new FeedbackCommand()
    );

    new JDABuilder(AccountType.BOT)
        .setToken(config.bot_token)
        .addEventListeners(waiter)
        .addEventListeners(new getClipsEvent())
        .addEventListeners(client.build())
        .build();
    System.out.println("Bot up and running!");

  }

}
