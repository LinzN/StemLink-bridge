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

package de.linzn.stemLinkBridge.connector;

import de.linzn.stem.STEMApp;
import de.linzn.stemLinkBridge.StemLinkBridgePlugin;

public class OutputWriter {

    public static void writeOutputToLink(String headerChannel, byte[] bytes) {

        if (StemLinkBridgePlugin.stemLinkBridgePlugin.isMasterMode()) {
            STEMApp.getInstance().getStemLinkModule().getStemLinkServer().getClients().values().forEach(serverConnection -> serverConnection.writeOutput(headerChannel, bytes));
        } else {
            if (StemLinkBridgePlugin.stemLinkBridgePlugin.getSlaveManager().clientConnection.isValidConnection()) {
                StemLinkBridgePlugin.stemLinkBridgePlugin.getSlaveManager().clientConnection.writeOutput(headerChannel, bytes);
            } else {
                STEMApp.LOGGER.WARNING("Slave is not connected to master. Abort update!");
            }
        }
    }
}
