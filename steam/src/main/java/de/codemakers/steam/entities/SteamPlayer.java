/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.steam.entities;

import java.util.Objects;

public class SteamPlayer {

    //Public Data
    private final long steamID;
    private String displayName = null;
    private String profileURL = null;
    private String avatarURL = null;
    private OnlineStatus onlineStatus = OnlineStatus.UNKNOWN;
    private CommunityVisibilityState communityVisibilityState = CommunityVisibilityState.UNKNOWN;
    private boolean isCommunityProfile = false;
    private long lastLogOff = 0L;
    private boolean isAllowingPublicComments = false;
    //Private Data
    private String realName = null;
    private long primaryClanID = 0L;
    private long timeCreated = 0L;
    private String gameID;
    private String gameServerIP;
    private String gameExtraInfo;
    private String locCountryCode;
    private String locStateCode;

    public SteamPlayer(long steamID) {
        this.steamID = steamID;
    }

    public SteamPlayer(long steamID, String displayName, String profileURL, String avatarURL, OnlineStatus onlineStatus, CommunityVisibilityState communityVisibilityState, boolean isCommunityProfile, long lastLogOff, boolean isAllowingPublicComments) {
        this.steamID = steamID;
        this.displayName = displayName;
        this.profileURL = profileURL;
        this.avatarURL = avatarURL;
        this.onlineStatus = onlineStatus;
        this.communityVisibilityState = communityVisibilityState;
        this.isCommunityProfile = isCommunityProfile;
        this.lastLogOff = lastLogOff;
        this.isAllowingPublicComments = isAllowingPublicComments;
    }

    public final long getSteamID() {
        return steamID;
    }

    public final String getSteamIDString() {
        return "" + steamID;
    }

    public final String getDisplayName() {
        return displayName;
    }

    public final SteamPlayer setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public final String getProfileURL() {
        return profileURL;
    }

    public final SteamPlayer setProfileURL(String profileURL) {
        this.profileURL = profileURL;
        return this;
    }

    public final String getAvatarURL() {
        return avatarURL;
    }

    public final SteamPlayer setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
        return this;
    }

    public final OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public final SteamPlayer setOnlineStatus(OnlineStatus onlineStatus) {
        Objects.requireNonNull(onlineStatus);
        this.onlineStatus = onlineStatus;
        return this;
    }

    public final CommunityVisibilityState getCommunityVisibilityState() {
        return communityVisibilityState;
    }

    public final SteamPlayer setCommunityVisibilityState(CommunityVisibilityState communityVisibilityState) {
        Objects.requireNonNull(communityVisibilityState);
        this.communityVisibilityState = communityVisibilityState;
        return this;
    }

    public final boolean isCommunityProfile() {
        return isCommunityProfile;
    }

    public final SteamPlayer setCommunityProfile(boolean communityProfile) {
        isCommunityProfile = communityProfile;
        return this;
    }

    public final long getLastLogOff() {
        return lastLogOff;
    }

    public final SteamPlayer setLastLogOff(long lastLogOff) {
        this.lastLogOff = lastLogOff;
        return this;
    }

    public final boolean isAllowingPublicComments() {
        return isAllowingPublicComments;
    }

    public final SteamPlayer setAllowingPublicComments(boolean allowingPublicComments) {
        isAllowingPublicComments = allowingPublicComments;
        return this;
    }

    public final String getRealName() {
        return realName;
    }

    public final SteamPlayer setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public final long getPrimaryClanID() {
        return primaryClanID;
    }

    public final SteamPlayer setPrimaryClanID(long primaryClanID) {
        this.primaryClanID = primaryClanID;
        return this;
    }

    public final long getTimeCreated() {
        return timeCreated;
    }

    public final SteamPlayer setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public final String getGameID() {
        return gameID;
    }

    public final SteamPlayer setGameID(String gameID) {
        this.gameID = gameID;
        return this;
    }

    public final String getGameServerIP() {
        return gameServerIP;
    }

    public final SteamPlayer setGameServerIP(String gameServerIP) {
        this.gameServerIP = gameServerIP;
        return this;
    }

    public final String getGameExtraInfo() {
        return gameExtraInfo;
    }

    public final SteamPlayer setGameExtraInfo(String gameExtraInfo) {
        this.gameExtraInfo = gameExtraInfo;
        return this;
    }

    public final String getLocCountryCode() {
        return locCountryCode;
    }

    public final SteamPlayer setLocCountryCode(String locCountryCode) {
        this.locCountryCode = locCountryCode;
        return this;
    }

    public final String getLocStateCode() {
        return locStateCode;
    }

    public final SteamPlayer setLocStateCode(String locStateCode) {
        this.locStateCode = locStateCode;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof SteamPlayer) {
            return steamID == ((SteamPlayer) object).steamID;
        } else if (object instanceof Long) {
            return steamID == (Long) object;
        }
        return false;
    }

    @Override
    public final String toString() {
        return "SteamPlayer{" + "steamID=" + steamID + ", displayName='" + displayName + '\'' + ", profileURL='" + profileURL + '\'' + ", avatarURL='" + avatarURL + '\'' + ", onlineStatus=" + onlineStatus + ", communityVisibilityState=" + communityVisibilityState + ", isCommunityProfile=" + isCommunityProfile + ", lastLogOff=" + lastLogOff + ", isAllowingPublicComments=" + isAllowingPublicComments + '}';
    }

    public enum OnlineStatus {
        UNKNOWN(-1), OFFLINE(0), ONLINE(1), BUSY(2), AWAY(3), SNOOZE(4), LOOKING_TO_TRADE(5), LOOKING_TO_PLAY(6);

        private final int key;

        OnlineStatus(int key) {
            this.key = key;
        }

        public static final OnlineStatus fromKey(int key) {
            for (OnlineStatus onlineStatus : values()) {
                if (onlineStatus.key == key) {
                    return onlineStatus;
                }
            }
            return null;
        }

        public final int getKey() {
            return key;
        }

    }

    public enum CommunityVisibilityState {
        UNKNOWN(-1), INVISIBLE(0), VISIBLE(3);

        private final int key;

        CommunityVisibilityState(int key) {
            this.key = key;
        }

        public static final CommunityVisibilityState fromKey(int key) {
            for (CommunityVisibilityState communityVisibilityState : values()) {
                if (communityVisibilityState.key == key) {
                    return communityVisibilityState;
                }
            }
            return null;
        }

        public final int getKey() {
            return key;
        }

    }

}
