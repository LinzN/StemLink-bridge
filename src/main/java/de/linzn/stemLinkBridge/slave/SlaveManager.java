/*
 * Copyright (C) 2019. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.stemLinkBridge.slave;

import de.linzn.stemLink.components.encryption.CryptContainer;
import de.linzn.stemLink.connections.ClientType;
import de.linzn.stemLink.connections.client.ClientConnection;
import de.linzn.stemLinkBridge.StemLinkBridgePlugin;
import de.stem.stemSystem.STEMSystemApp;

import java.util.UUID;


public class SlaveManager {
    public ClientConnection clientConnection;
    private String host;
    private int port;
    private String cryptAESKey;
    private byte[] vector16B;
    private CryptContainer cryptContainer;
    private UUID clientUUID;

    public SlaveManager() {
        STEMSystemApp.LOGGER.CONFIG("Starting StemLink bridge in SlaveMode");
        this.clientUUID = UUID.fromString(StemLinkBridgePlugin.stemLinkBridgePlugin.getDefaultConfig().getString("connector.uuid"));
        this.host = StemLinkBridgePlugin.stemLinkBridgePlugin.getDefaultConfig().getString("connector.masterAddress");
        this.port = StemLinkBridgePlugin.stemLinkBridgePlugin.getDefaultConfig().getInt("connector.masterPort");
        this.cryptAESKey = StemLinkBridgePlugin.stemLinkBridgePlugin.getDefaultConfig().getString("connector.crypt.cryptAESKey");
        this.vector16B = toByteArray(StemLinkBridgePlugin.stemLinkBridgePlugin.getDefaultConfig().getString("connector.crypt.vector16B"));
        this.cryptContainer = new CryptContainer(cryptAESKey, vector16B);
        this.clientConnection = new ClientConnection(this.host, this.port, clientUUID, ClientType.SLAVE, new StemLinkBridgeWrapper(), this.cryptContainer);
    }

    private static byte[] toByteArray(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        byte[] result = new byte[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }
        return result;
    }

    public void register() {
        this.clientConnection.registerEvents(new StemLinkSlaveListener());
        STEMSystemApp.LOGGER.CONFIG("StemLinkSlaveListener registered");
        this.clientConnection.setEnable();
        STEMSystemApp.LOGGER.CONFIG("Slave stemLink client started");
    }

    public void unregister() {
        this.clientConnection.setDisable();
        STEMSystemApp.LOGGER.CONFIG("Slave stemLink client stopped");
    }
}
