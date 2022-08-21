package com.github.tahmid_23.zombiesautosplits.config;

import java.util.Objects;

public record ZombiesAutoSplitsConfig(String host, int port, boolean useLiveSplits, boolean useInternal) {

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_PORT = 16834;

    public static final boolean DEFAULT_USE_LIVE_SPLITS = false;

    public static final boolean DEFAULT_USE_INTERNAL = true;

    public Builder toBuilder() {
        return new Builder()
                .setHost(host)
                .setPort(port)
                .setUseLiveSplits(useLiveSplits)
                .setUseInternal(useInternal);
    }

    public static class Builder {

        private String host = DEFAULT_HOST;

        private int port = DEFAULT_PORT;

        private boolean useLiveSplits = DEFAULT_USE_LIVE_SPLITS;

        private boolean useInternal = DEFAULT_USE_INTERNAL;

        public Builder setHost(String host) {
            Objects.requireNonNull(host, "host");
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setUseLiveSplits(boolean useLiveSplits) {
            this.useLiveSplits = useLiveSplits;
            return this;
        }

        public Builder setUseInternal(boolean useInternal) {
            this.useInternal = useInternal;
            return this;
        }

        public ZombiesAutoSplitsConfig build() {
            return new ZombiesAutoSplitsConfig(host, port, useLiveSplits, useInternal);
        }

    }

}
