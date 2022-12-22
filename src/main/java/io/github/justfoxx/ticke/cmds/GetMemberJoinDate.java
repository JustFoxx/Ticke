package io.github.justfoxx.ticke.cmds;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.justfoxx.ticke.Handler;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

public class GetMemberJoinDate implements Handler.Command {
    @Override @NonNull
    public String getName() {
        return "getmemberjoindate";
    }

    @Override @NonNull
    public String getDescription() {
        return "Gets the join date of a member";
    }

    @Override @NonNull
    public String getUsage() {
        return String.format("%s%s (user)", Handler.prefix, getName());
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {
        if (event.getMessage().getAuthor().get().isBot()) throw new Exception("You cannot use this command as a bot");
        if (!event.getMessage().getAuthor().isPresent()) throw new Exception("Required author is not present");
    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
}
