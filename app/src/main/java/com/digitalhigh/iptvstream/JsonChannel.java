package com.digitalhigh.iptvstream;

import android.content.ComponentName;
import android.support.annotation.NonNull;

import com.felkertech.cumulustv.plugins.CumulusChannel;

/**
 * <p>It is an extension of the {@link CumulusChannel} class which provides custom methods specifically
 * for interfacing with the Tv Input Database.</p>
 *
 * @author Nick
 * @version 2016.09.04
 */
class JsonChannel extends CumulusChannel implements Comparable {

    public JsonChannel() {
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return getNumber().compareTo(((JsonChannel) o).getNumber());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JsonChannel && getMediaUrl().equals(((JsonChannel) o).getMediaUrl());
    }

    static class Builder extends CumulusChannel.Builder {
        private JsonChannel jsonChannel;

        Builder() {
            super();
        }

        Builder(CumulusChannel channel) {
            super(channel);
        }

        @Override
        public JsonChannel.Builder setAudioOnly(boolean audioOnly) {
            super.setAudioOnly(audioOnly);
            return this;
        }

        @Override
        public JsonChannel.Builder setEpgUrl(String epgUrl) {
            super.setEpgUrl(epgUrl);
            return this;
        }

        @Override
        public JsonChannel.Builder setGenres(String genres) {
            super.setGenres(genres);
            return this;
        }

        @Override
        public JsonChannel.Builder setLogo(String logo) {
            super.setLogo(logo);
            return this;
        }

        @Override
        public JsonChannel.Builder setMediaUrl(String mediaUrl) {
            super.setMediaUrl(mediaUrl);
            return this;
        }

        @Override
        public JsonChannel.Builder setName(String name) {
            super.setName(name);
            return this;
        }

        @Override
        public JsonChannel.Builder setNumber(String number) {
            super.setNumber(number);
            return this;
        }

        @Override
        public JsonChannel.Builder setPluginSource(@NonNull ComponentName pluginComponent) {
            super.setPluginSource(pluginComponent);
            return this;
        }

        @Override
        public JsonChannel.Builder setPluginSource(String pluginComponentName) {
            super.setPluginSource(pluginComponentName);
            return this;
        }

        @Override
        public JsonChannel.Builder setSplashscreen(String splashscreen) {
            super.setSplashscreen(splashscreen);
            return this;
        }

        @Override
        public JsonChannel build() {
            CumulusChannel cumulusChannel = super.build();
            jsonChannel = new JsonChannel();
            Builder builder = new Builder(cumulusChannel);
            builder.cloneInto(jsonChannel);
            return jsonChannel;
        }
    }
}
