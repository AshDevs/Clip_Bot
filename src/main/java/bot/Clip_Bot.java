package bot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import config.Config;
import config.PropertiesBasedLoader;
import event.ClipsEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Clip_Bot {

  public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public static void main(String[] args) throws LoginException {

    Config config = PropertiesBasedLoader.load_from_properties("config.properties");
    CommandClientBuilder client = new CommandClientBuilder();
    client.setStatus(OnlineStatus.ONLINE);
    client.setActivity(Activity.streaming("Rowey Daddy.", Constants.TWITCH));
    client.setOwnerId(Constants.OWNERID);
    client.setEmojis(Constants.SUCCESS, Constants.WARNING, Constants.ERROR);
    client.setPrefix(Constants.PREFIX);

    new JDABuilder(AccountType.BOT)
        .setToken(config.bot_token)
        .addEventListeners(
            new ClipsEvent(),
            client.build())
        .build();
  }

}
