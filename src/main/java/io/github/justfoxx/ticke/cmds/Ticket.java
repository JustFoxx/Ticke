package io.github.justfoxx.ticke.cmds;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import io.github.justfoxx.ticke.Handler;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Arrays;

public class Ticket implements Handler.Command {
    @Override @NonNull
    public String getName() {
        return null;
    }

    @Override @NonNull
    public String getDescription() {
        return null;
    }

    @Override @NonNull
    public String getUsage() {
        return String.format("%s%s (cmd)", Handler.prefix, getName());
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {
        if (event.getMessage().getAuthor().isEmpty()) throw  new Exception("Required author is not present");
        if (event.getMessage().getAuthor().get().isBot()) throw  new Exception("You cannot use this command as a bot");
    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        Message message = event.getMessage();
        String cmd = args[0].toLowerCase();
        String[] arr = Arrays.copyOfRange(args,1,args.length);

        return switch (cmd) {
            case "create" -> create(arr, event);
            case "close" -> close(arr, event);
            case "open" -> open(arr, event);
            case "remove" -> remove(arr, event);
            default -> help(arr, event);
        };

    }
    @NonNull
    private Mono<?> help(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
    @NonNull
    private Mono<?> remove(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
    @NonNull
    private Mono<?> open(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
    @NonNull
    private Mono<?> close(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
    @NonNull
    private Mono<?> create(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
}
