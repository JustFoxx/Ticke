package io.github.justfoxx.ticke;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import io.github.justfoxx.ticke.cmds.GetMemberJoinDate;
import io.github.justfoxx.ticke.cmds.GetUserServer;
import io.github.justfoxx.ticke.cmds.Ticket;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;

public class Handler {
    public static final HashSet<Command> cmds = new HashSet<>();
    public static final String prefix = "&'";
    public interface Command {
        @NonNull String getName();
        @NonNull String getDescription();
        @NonNull String getUsage();
        void canExecute(MessageCreateEvent event) throws Exception;
        @NonNull Mono<?> run(String[] args, MessageCreateEvent event) throws Exception;
    }

    public static Command GETUSERSERVER = new GetUserServer();
    public static Command GETMEMBERJOINDATE = new GetMemberJoinDate();
    public static Ticket TICKET = new Ticket();

    public static void register() {
        TICKET.register();
        cmds.add(GETUSERSERVER);
        cmds.add(GETMEMBERJOINDATE);
        cmds.add(TICKET);
    }

    public static Mono<?> handle(MessageCreateEvent event) {
        if(event.getMessage().getChannel().block().getType() != Channel.Type.GUILD_TEXT) return Mono.empty();
        String[] args = event.getMessage().getContent().split(" ");
        if (!args[0].startsWith(prefix)) {
            return Mono.empty();
        }

        for (Command cmd : cmds) {
            if (!cmd.getName().equalsIgnoreCase(args[0].substring(prefix.length()))) continue;
            try {
                cmd.canExecute(event);
                return cmd.run(Arrays.copyOfRange(args,1,args.length), event);
            } catch (Exception e) {
                return errorMessage(e, event);
            }

        }
        return Mono.empty();
    }

    private static Mono<?> errorMessage(Exception ex, MessageCreateEvent event) {
        User user = event.getClient().getSelf().block();
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title("Error")
                .description("```"+ex.getMessage()+"```")
                .color(Color.of(0xFF0000))
                .author(user.getUsername(), null, user.getAvatarUrl());
        return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(embedBuilder.build()));
    }
}
