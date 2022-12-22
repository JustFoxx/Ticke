package io.github.justfoxx.ticke;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.intent.IntentSet;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class Main {
    public static final String token = System.getenv("TOKEN");
    public static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Handler.register();

        Mono<Void> login = DiscordClientBuilder.create(token)
                .build().gateway()
                .setEnabledIntents(IntentSet.all())
                .withGateway(gatewayFunction);
        login.block();
    }

    public static final Function<GatewayDiscordClient, Publisher<?>> gatewayFunction = gateway -> {
        var command = gateway.on(MessageCreateEvent.class, Handler::handle);
        return command.then();
    };
}

