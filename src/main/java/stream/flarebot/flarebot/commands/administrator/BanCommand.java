package stream.flarebot.flarebot.commands.administrator;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.MessageUtils;
import stream.flarebot.flarebot.commands.Command;
import stream.flarebot.flarebot.commands.CommandType;
import stream.flarebot.flarebot.commands.FlareBotManager;
import stream.flarebot.flarebot.mod.Punishment;

import java.awt.*;

public class BanCommand implements Command {

    @Override
    public void onCommand(User sender, TextChannel channel, Message message, String[] args, Member member) {
        if (args.length >= 2) {
            if (channel.getGuild().getSelfMember().hasPermission(channel, Permission.BAN_MEMBERS)) {
                User user = MessageUtils.getUser(args[0]);
                if (user == null) {
                    channel.sendMessage(new EmbedBuilder()
                            .setDescription("We cannot find that user! Try their ID if you didn't already.")
                            .setColor(Color.red).build()).queue();
                    return;
                }
                //TODO: When reasons are out for JDA add them here!!!
                String reason = FlareBot.getMessage(args, 1);
                FlareBotManager.getInstance().getAutoModConfig(channel.getGuild().getId())
                               .postToModLog(channel, user, sender, Punishment.EPunishment.BAN, reason);
                channel.sendMessage(new EmbedBuilder()
                        .setDescription("The ban hammer has been struck on " + user.getName() + " \uD83D\uDD28")
                        .setImage(channel.getGuild().getId().equals(FlareBot.OFFICIAL_GUILD) ?
                                "https://cdn.discordapp.com/attachments/226785954537406464/309414200344707084/logo-no-background.png" : null)
                        .build()).queue();
                channel.getGuild().getController().ban(channel.getGuild().getMember(user), 7 /*, reason*/).queue();
            } else {
                channel.sendMessage(new EmbedBuilder()
                        .setDescription("We can't ban users! Make sure we have the `Ban Members` permission!")
                        .setColor(Color.red).build()).queue();
            }
        } else {
            MessageUtils.sendUsage(this, channel);
        }
    }

    @Override
    public String getCommand() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Ban a user";
    }

    @Override
    public String getUsage() {
        return "{%}ban <user> <reason>";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
