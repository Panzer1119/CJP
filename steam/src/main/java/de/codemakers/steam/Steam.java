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

package de.codemakers.steam;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import de.codemakers.base.action.Action;
import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.events.EventHandler;
import de.codemakers.base.events.EventListener;
import de.codemakers.base.events.IEventHandler;
import de.codemakers.steam.entities.SteamPlayer;
import de.codemakers.steam.events.SteamEvent;
import de.codemakers.steam.events.SteamPlayerDisplayNameUpdateEvent;
import de.codemakers.steam.events.SteamPlayerOnlineStatusUpdateEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Steam implements IEventHandler<SteamEvent> {

    public static final String API_FORMAT = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s";

    private final EventHandler<SteamEvent> eventHandler = new EventHandler<>();
    private final CopyOnWriteArrayList<SteamPlayer> steamPlayers = new CopyOnWriteArrayList<>();
    private String apiKey = null;
    private URL url = null;

    public Steam(String apiKey) {
        this.apiKey = apiKey;
    }

    public static final String getAPIURL(Steam steam) {
        Objects.requireNonNull(steam);
        return String.format(API_FORMAT, steam.apiKey, steam.steamPlayers.stream().map(SteamPlayer::getSteamIDString).collect(Collectors.joining(",")));
    }

    public final String getApiKey() {
        return apiKey;
    }

    public final Steam setApiKey(String apiKey) {
        if (apiKey == null) {
            return this;
        }
        if (!apiKey.equals(this.apiKey)) {
            this.url = null;
        }
        this.apiKey = apiKey;
        return this;
    }

    public final Steam addSteamPlayers(SteamPlayer... steamPlayers) {
        this.url = null;
        this.steamPlayers.addAll(Arrays.asList(steamPlayers));
        return this;
    }

    public final List<SteamPlayer> getSteamPlayers() {
        return Collections.unmodifiableList(steamPlayers);
    }

    public final SteamPlayer getSteamPlayer(long steamid) {
        return steamPlayers.stream().filter((steamPlayer) -> steamPlayer.getSteamID() == steamid).findFirst().orElse(null);
    }

    public final String getAPIURL() {
        return getAPIURL(this);
    }

    private final URL getURL() {
        if (url == null) {
            try {
                url = new URL(getAPIURL());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return url;
    }

    public final ReturningAction<List<SteamPlayer>> update() {
        return Action.ofToughSupplier(() -> {
            final HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();
            connection.setRequestMethod("GET");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final String data = bufferedReader.lines().collect(Collectors.joining("\n"));
            bufferedReader.close();
            final Any any = JsonIterator.deserialize(data);
            final Any response = any.get("response");
            final List<Any> players = response.get("players").asList();
            players.stream().parallel().forEach(this::updatePlayer);
            return getSteamPlayers();
        });
    }

    private final Steam updatePlayer(Any player) {
        final long steamID = player.get("steamid").toLong();
        final SteamPlayer steamPlayer = steamPlayers.stream().filter((steamPlayer_) -> steamPlayer_.getSteamID() == steamID).findFirst().orElseGet(() -> {
            final SteamPlayer steamPlayer_ = new SteamPlayer(steamID);
            steamPlayers.add(steamPlayer_);
            return steamPlayer_;
        });
        //------------------------------------------------------------------------------------------------------------//
        //Public Data
        final String personaname = player.get("personaname").toString();
        if (!Objects.equals(steamPlayer.getDisplayName(), personaname)) {
            onEvent(new SteamPlayerDisplayNameUpdateEvent(this, steamID, steamPlayer.getDisplayName()));
        }
        steamPlayer.setDisplayName(personaname);
        //------------------------------------------------------------------------------------------------------------//
        final String profileurl = player.get("profileurl").toString();
        if (!Objects.equals(steamPlayer.getProfileURL(), profileurl)) {

        }
        steamPlayer.setProfileURL(profileurl);
        //------------------------------------------------------------------------------------------------------------//
        final String avatar = player.get("avatar").toString();
        if (!Objects.equals(steamPlayer.getAvatarURL(), avatar)) {

        }
        steamPlayer.setAvatarURL(avatar);
        //------------------------------------------------------------------------------------------------------------//
        final int personastate = player.get("personastate").toInt();
        if (steamPlayer.getOnlineStatus().getKey() != personastate) {
            onEvent(new SteamPlayerOnlineStatusUpdateEvent(this, steamID, steamPlayer.getOnlineStatus()));
        }
        steamPlayer.setOnlineStatus(SteamPlayer.OnlineStatus.fromKey(personastate));
        //------------------------------------------------------------------------------------------------------------//
        final int communityvisibilitystate = player.get("communityvisibilitystate").toInt();
        if (steamPlayer.getCommunityVisibilityState().getKey() != communityvisibilitystate) {

        }
        steamPlayer.setCommunityVisibilityState(SteamPlayer.CommunityVisibilityState.fromKey(communityvisibilitystate));
        //------------------------------------------------------------------------------------------------------------//
        final boolean profilestate = player.get("profilestate").toInt() == 1;
        if (steamPlayer.isCommunityProfile() != profilestate) {

        }
        steamPlayer.setCommunityProfile(profilestate);
        //------------------------------------------------------------------------------------------------------------//
        final long lastlogoff = player.get("lastlogoff").toLong();
        if (steamPlayer.getLastLogOff() != lastlogoff) {

        }
        steamPlayer.setLastLogOff(lastlogoff);
        //------------------------------------------------------------------------------------------------------------//
        final boolean commentpermission = player.get("commentpermission").toInt() == 1;
        if (steamPlayer.isAllowingPublicComments() != commentpermission) {

        }
        steamPlayer.setCommunityProfile(commentpermission);
        //------------------------------------------------------------------------------------------------------------//
        //Private Data
        //------------------------------------------------------------------------------------------------------------//
        steamPlayer.setRealName(player.get("realname").toString());
        steamPlayer.setPrimaryClanID(player.get("primaryclanid").toLong());
        steamPlayer.setTimeCreated(player.get("timecreated").toLong());
        steamPlayer.setGameID(player.get("gameid").toString());
        steamPlayer.setGameServerIP(player.get("gameserverip").toString());
        steamPlayer.setGameExtraInfo(player.get("gameextrainfo").toString());
        steamPlayer.setLocCountryCode(player.get("loccountrycode").toString());
        steamPlayer.setLocStateCode(player.get("locstatecode").toString());
        //------------------------------------------------------------------------------------------------------------//
        return this;
    }


    @Override
    public final <T extends SteamEvent> IEventHandler addEventListener(Class<T> clazz, EventListener<T> eventListener) {
        eventHandler.addEventListeners(clazz, eventListener);
        return this;
    }

    @Override
    public final <T extends SteamEvent> IEventHandler removeEventListener(Class<T> clazz, EventListener<T> eventListener) {
        eventHandler.removeEventListener(clazz, eventListener);
        return this;
    }

    @Override
    public final IEventHandler clearEventListeners() {
        eventHandler.clearEventListeners();
        return this;
    }

    @Override
    public final <T extends SteamEvent> List<EventListener<T>> getEventListeners(Class<T> clazz) {
        return eventHandler.getEventListeners(clazz);
    }

    @Override
    public final <T extends SteamEvent> void onEvent(T event) {
        eventHandler.onEvent(event);
    }

}
