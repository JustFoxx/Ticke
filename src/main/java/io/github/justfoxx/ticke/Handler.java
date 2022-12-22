package io.github.justfoxx.ticke;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.justfoxx.ticke.cmds.GetUserServer;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;

public class Handler {
    public static final HashSet<Command> cmds = new HashSet<>();
    public static String prefix = "&'";
    public interface Command {
        String getName();
        String getDescription();
        String getUsage();
        Exception canExecute(MessageCreateEvent event);
        Mono<?> run(String[] args, MessageCreateEvent event);
    }
    public static void register() {
        cmds.add(new GetUserServer());
    }

    public static Mono<?> handle(MessageCreateEvent event) {
        String[] args = event.getMessage().getContent().split(" ");
        if (!args[0].startsWith(prefix)) {
            return Mono.empty();
        }

        for (Command cmd : cmds) {
            if (!cmd.getName().equalsIgnoreCase(args[0].substring(prefix.length()))) return Mono.empty();
            var ex = cmd.canExecute(event);
            if(ex != null) return errorMessage(ex, event);
            return cmd.run(Arrays.copyOfRange(args,1,args.length), event);
        }
        return Mono.empty();
    }

    private static Mono<?> errorMessage(Exception ex, MessageCreateEvent event) {
        return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(ex.getMessage()));
    }
}
