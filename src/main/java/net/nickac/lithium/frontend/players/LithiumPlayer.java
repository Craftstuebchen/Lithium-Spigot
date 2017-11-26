/*
 * MIT License
 *
 * Copyright (c) 2017 NickAc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.nickac.lithium.frontend.players;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.nickac.lithium.backend.controls.LContainerViewable;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LOverlay;
import net.nickac.lithium.backend.controls.impl.LWindow;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.LithiumPlugin;
import net.nickac.lithium.frontend.LithiumUtils;
import net.nickac.lithium.frontend.container.ContainerManager;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by NickAc for Lithium!
 */
public class LithiumPlayer {

    private ContainerManager containerManager;

    @Getter
    @Setter
    private LContainerViewable currentContainer;

    @Getter
    private Player handle;
    @Getter
    @Setter(value = AccessLevel.PACKAGE)
    private boolean usingLithium;

    LithiumPlayer(ContainerManager containerManager, Player handle, boolean usingLithium) {
        this.containerManager = containerManager;
        this.handle = handle;
        this.usingLithium = usingLithium;
    }

    public void sendLithiumMessage(String msg) {
        handle.sendPluginMessage(
                LithiumPlugin.getInstance(),
                LithiumPlugin.LITHIUM_CHANNEL,
                LithiumUtils.writeUTF8String(msg)
        );
    }

    public void refreshControl(UUID uuid) {
        LControl c = getControlById(uuid);
        if (usingLithium && c != null && uuid != null) {
            sendLithiumMessage(LithiumConstants.TO_CLIENT.CONTROL_CHANGED + SerializationUtils.objectToString(c));
        }
    }

    public UUID getUniqueId(){
        return handle.getUniqueId();
    }

    public void openInterface(LWindow w) {
        containerManager.openContainer(this, w);
    }

    public void openOverlay(LOverlay o) {
        containerManager.openContainer(this, o);
    }

    public void closeInterface() {
        //TODO: Force Close!
        if (usingLithium) {
            sendLithiumMessage(LithiumConstants.TO_CLIENT.CLOSE_WINDOW);
            setCurrentContainer(null);
        }
    }


    public void removeViewable(UUID u){
        containerManager.removeContainer(u);
    }

    public LContainerViewable geViewableById(UUID u) {
        return containerManager.getViewableById(u);
    }

    public LControl getControlById(UUID u) {
        return containerManager.getControlById(u);
    }


}