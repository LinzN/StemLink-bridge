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

package de.linzn.stemLinkBridge.connector;

import de.linzn.stemLinkBridge.StemLinkBridgePlugin;
import de.stem.stemSystem.STEMSystemApp;

public class OutputWriter {

    public static void writeOutputToLink(String headerChannel, byte[] bytes) {

        if (StemLinkBridgePlugin.stemLinkBridgePlugin.isMasterMode()) {
            STEMSystemApp.getInstance().getStemLinkModule().getStemLinkServer().getClients().values().forEach(serverConnection -> serverConnection.writeOutput(headerChannel, bytes));
        } else {
            if(StemLinkBridgePlugin.stemLinkBridgePlugin.getSlaveManager().clientConnection.isValidConnection()){
                StemLinkBridgePlugin.stemLinkBridgePlugin.getSlaveManager().clientConnection.writeOutput(headerChannel, bytes);
            } else {
                STEMSystemApp.LOGGER.WARNING("Slave is not connected to master. Abort update!");
            }
        }
    }
}
