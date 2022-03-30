package com.github.tahmid_23.zombiesautosplits.splitter;

import java.util.concurrent.CompletableFuture;

public interface LiveSplitSplitter {

    CompletableFuture<Void> startOrSplit();

}
