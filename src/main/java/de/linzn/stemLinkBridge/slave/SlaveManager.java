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

package de.linzn.stemLinkBridge.slave;

import de.linzn.stem.STEMApp;
import de.linzn.stemLink.components.encryption.CryptContainer;
import de.linzn.stemLink.connections.ClientType;
import de.linzn.stemLink.connections.client.ClientConnection;
import de.linzn.stemLinkBridge.StemLinkBridgePlugin;
import de.linzn.stemLinkBridge.slave.listener.StemEventListener;
import de.linzn.stemLinkBridge.slave.listener.StemLinkSlaveListener;

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
        STEMApp.LOGGER.CONFIG("Starting StemLink bridge in SlaveMode");
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
        STEMApp.getInstance().getEventModule().getStemEventBus().register(new StemEventListener());

        this.clientConnection.registerEvents(new StemLinkSlaveListener());
        STEMApp.LOGGER.CONFIG("StemLinkSlaveListener registered");
        this.clientConnection.setEnable();
        STEMApp.LOGGER.CONFIG("Slave stemLink client started");
    }

    public void unregister() {
        this.clientConnection.setDisable();
        STEMApp.LOGGER.CONFIG("Slave stemLink client stopped");
    }
}
