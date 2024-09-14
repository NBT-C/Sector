package me.nbtc.devroomsector.manager.flag;

public enum FlagState {
    EVERYONE,
    WHITELIST,
    NONE;

    public static FlagState fromString(String flagString) {
        try {
            return FlagState.valueOf(flagString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
