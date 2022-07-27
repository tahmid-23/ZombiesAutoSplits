package com.github.tahmid_23.zombiesautosplits.splitter;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface LiveSplitSplitter {

    CompletableFuture<Void> startOrSplit();

}
