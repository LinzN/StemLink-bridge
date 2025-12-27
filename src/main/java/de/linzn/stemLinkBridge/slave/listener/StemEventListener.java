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

import de.linzn.homeDevices.devices.interfaces.MqttSwitch;
import de.linzn.homeDevices.events.records.DeviceUpdateEvent;
import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.eventModule.handler.StemEventHandler;
import de.linzn.stemLinkBridge.connector.OutputWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StemEventListener {

    @StemEventHandler
    public void onDeviceSwitch(DeviceUpdateEvent deviceUpdateEvent) {
        MqttSwitch switchableMQTTDevice = deviceUpdateEvent.mqttSwitch();

        String headerChannel = "stemLink_bridge_device_update";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("update_device");
            dataOutputStream.writeUTF(switchableMQTTDevice.getConfigName());
            dataOutputStream.writeBoolean(deviceUpdateEvent.newStatus());
        } catch (IOException e) {
            STEMApp.LOGGER.ERROR(e);
        }

        OutputWriter.writeOutputToLink(headerChannel, byteArrayOutputStream.toByteArray());
    }
}
