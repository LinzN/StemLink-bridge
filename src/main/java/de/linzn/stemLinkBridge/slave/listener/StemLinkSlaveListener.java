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

package de.linzn.stemLinkBridge.slave.listener;


import de.linzn.stem.STEMApp;
import de.linzn.stem.utils.JavaUtils;
import de.linzn.stemLink.components.events.ConnectEvent;
import de.linzn.stemLink.components.events.DisconnectEvent;
import de.linzn.stemLink.components.events.ReceiveDataEvent;
import de.linzn.stemLink.components.events.handler.EventHandler;
import de.linzn.stemLinkBridge.connector.OutputWriter;

import java.io.*;

public class StemLinkSlaveListener {


    @EventHandler(channel = "stemLink_bridge")
    public void onReceiveEvent(ReceiveDataEvent event) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getDataInBytes()));
        try {
            String configName = in.readUTF();

            String action = in.readUTF();

        } catch (IOException e) {
            STEMApp.LOGGER.ERROR(e);
        }

    }


    @EventHandler()
    public void onConnect(ConnectEvent event) {
        STEMApp.LOGGER.SUPER("StemLink slave connected to master server!");
        String headerChannel = "stemLink_bridge";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("slave_status");
            dataOutputStream.writeUTF(JavaUtils.getVersion());
            dataOutputStream.writeLong(STEMApp.getInstance().getUptimeDate().getTime());
            dataOutputStream.writeInt(STEMApp.getInstance().getPluginModule().getLoadedPlugins().size());
        } catch (IOException e) {
            STEMApp.LOGGER.ERROR(e);
        }
        OutputWriter.writeOutputToLink(headerChannel, byteArrayOutputStream.toByteArray());

    }

    @EventHandler()
    public void onDisconnect(DisconnectEvent event) {
        STEMApp.LOGGER.SUPER("StemLink slave disconnected from master server!");
    }

}
