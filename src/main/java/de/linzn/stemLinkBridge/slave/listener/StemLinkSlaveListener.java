/*
 * Copyright (C) 2021. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.stemLinkBridge.slave.listener;


import de.linzn.stemLink.components.events.ConnectEvent;
import de.linzn.stemLink.components.events.DisconnectEvent;
import de.linzn.stemLink.components.events.ReceiveDataEvent;
import de.linzn.stemLink.components.events.handler.EventHandler;
import de.linzn.stemLinkBridge.connector.OutputWriter;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.utils.JavaUtils;

import java.io.*;

public class StemLinkSlaveListener {


    @EventHandler(channel = "stemLink_bridge")
    public void onReceiveEvent(ReceiveDataEvent event) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getDataInBytes()));
        try {
            String configName = in.readUTF();

            String action = in.readUTF();

        } catch (IOException e) {
            STEMSystemApp.LOGGER.ERROR(e);
        }

    }


    @EventHandler()
    public void onConnect(ConnectEvent event) {
        STEMSystemApp.LOGGER.WARNING("StemLink slave connected to master server!");
        String headerChannel = "stemLink_bridge";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("slave_status");
            dataOutputStream.writeUTF(JavaUtils.getVersion());
            dataOutputStream.writeLong(STEMSystemApp.getInstance().getUptimeDate().getTime());
            dataOutputStream.writeInt(STEMSystemApp.getInstance().getPluginModule().getLoadedPlugins().size());
        } catch (IOException e) {
            STEMSystemApp.LOGGER.ERROR(e);
        }
        OutputWriter.writeOutputToLink(headerChannel, byteArrayOutputStream.toByteArray());

    }

    @EventHandler()
    public void onDisconnect(DisconnectEvent event) {
        STEMSystemApp.LOGGER.WARNING("StemLink slave disconnected from master server!");
    }

}
