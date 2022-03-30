package com.github.tahmid_23.zombiesautosplits.splitter.socket;

import com.github.tahmid_23.zombiesautosplits.splitter.LiveSplitSplitter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LiveSplitSocketSplitter implements LiveSplitSplitter {

    private final Executor executor;

    private final String host;

    private final int port;

    public LiveSplitSocketSplitter(Executor executor, String host, int port) {
        this.executor = Objects.requireNonNull(executor, "executor");
        this.host = Objects.requireNonNull(host, "host");
        this.port = port;
    }

    @Override
    public CompletableFuture<Void> startOrSplit() {
        return sendCommand("startorsplit");
    }

    @SuppressWarnings("SameParameterValue")
    private CompletableFuture<Void> sendCommand(String command) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        executor.execute(() -> {
            try (Socket socket = new Socket(host, port);
                 Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                writer.write(command + "\r\n");

                future.complete(null);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

}
