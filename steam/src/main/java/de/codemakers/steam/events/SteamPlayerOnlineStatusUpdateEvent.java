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

package de.codemakers.steam.events;

import de.codemakers.steam.Steam;
import de.codemakers.steam.entities.SteamPlayer;

public class SteamPlayerOnlineStatusUpdateEvent extends GenericSteamPlayerEvent {

    final SteamPlayer.OnlineStatus oldOnlineStatus;

    public SteamPlayerOnlineStatusUpdateEvent(Steam steam, long steamID, SteamPlayer.OnlineStatus oldOnlineStatus) {
        super(steam, steamID);
        this.oldOnlineStatus = oldOnlineStatus;
    }

    public SteamPlayerOnlineStatusUpdateEvent(long id, Steam steam, long steamID, SteamPlayer.OnlineStatus oldOnlineStatus) {
        super(id, steam, steamID);
        this.oldOnlineStatus = oldOnlineStatus;
    }

    public final SteamPlayer.OnlineStatus getOldOnlineStatus() {
        return oldOnlineStatus;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": id=" + id + ", steam=" + steam + ", steamID=" + steamID + ", oldOnlineStatus=" + oldOnlineStatus;
    }

}
