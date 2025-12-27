/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.stemLinkBridge.master.listener;


import de.linzn.stem.STEMApp;
import de.linzn.stemLink.components.events.ReceiveDataEvent;
import de.linzn.stemLink.components.events.handler.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StemLinkMasterListener {


    @EventHandler(channel = "stemLink_bridge")
    public void onReceiveEvent(ReceiveDataEvent event) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getDataInBytes()));
        try {
            String subChannel = in.readUTF();

            if (subChannel.equalsIgnoreCase("slave_status")) {
                String version = in.readUTF();
                long uptimeValue = in.readLong();
                int loadedPlugins = in.readInt();


                Date date = new Date(uptimeValue);
                long diff = TimeUnit.MILLISECONDS.toSeconds((new Date()).getTime() - date.getTime());
                String uptime = String.format("%d days, %02d:%02d:%02d", diff / 86400L, diff / 3600L, diff % 3600L / 60L, diff % 60L);

                STEMApp.LOGGER.CORE("Receive slave STEM data");
                STEMApp.LOGGER.CORE("VERSION: " + version + " UPTIME: " + uptime);
                STEMApp.LOGGER.CORE("LOADED PLUGINS: " + loadedPlugins);

            }
        } catch (IOException e) {
            STEMApp.LOGGER.ERROR(e);
        }

    }

    @EventHandler(channel = "stemLink_bridge_device_update")
    public void onDeviceInfoUpdate(ReceiveDataEvent event) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getDataInBytes()));
        try {
            String subChannel = in.readUTF();

            if (subChannel.equalsIgnoreCase("update_device")) {
                String configName = in.readUTF();
                boolean newValue = in.readBoolean();
                STEMApp.LOGGER.CONFIG("Extern deviceUpdate on slave system: " + configName + " " + newValue);
            }

        } catch (IOException e) {
            STEMApp.LOGGER.ERROR(e);
        }
    }

}
