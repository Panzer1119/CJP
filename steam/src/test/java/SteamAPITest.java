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

import de.codemakers.base.CJP;
import de.codemakers.base.events.EventListener;
import de.codemakers.steam.Steam;
import de.codemakers.steam.entities.SteamPlayer;
import de.codemakers.steam.events.SteamPlayerDisplayNameUpdateEvent;
import de.codemakers.steam.events.SteamPlayerOnlineStatusUpdateEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SteamAPITest {

    private static String API_KEY_STEAM = "";

    public static final void main(String[] args) throws Exception {
        CJP.shutdown().queueAfter(10, TimeUnit.SECONDS);
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("api_key_steam.txt")));
        API_KEY_STEAM = bufferedReader.lines().collect(Collectors.joining());
        bufferedReader.close();
        final Steam steam = new Steam(API_KEY_STEAM);
        steam.addSteamPlayers(new SteamPlayer(76561198107770160L), new SteamPlayer(76561198112789232L));
        steam.addEventListener(SteamPlayerDisplayNameUpdateEvent.class, new EventListener<>() {
            @Override
            public <T1 extends SteamPlayerDisplayNameUpdateEvent> void onEvent(T1 event) {
                System.out.println(String.format("[%s] \"%s\": oldName: %s", event.getClass().getSimpleName(), event.getSteam().getSteamPlayer(event.getSteamID()).getDisplayName(), event.getOldName()));
            }
        });
        steam.addEventListener(SteamPlayerOnlineStatusUpdateEvent.class, new EventListener<>() {
            @Override
            public <T1 extends SteamPlayerOnlineStatusUpdateEvent> void onEvent(T1 event) {
                System.out.println(String.format("[%s] \"%s\": oldOnlineStatus: %s", event.getClass().getSimpleName(), event.getSteam().getSteamPlayer(event.getSteamID()).getDisplayName(), event.getOldOnlineStatus()));
            }
        });
        steam.update().queue(System.out::println, System.err::println);
    }

}
