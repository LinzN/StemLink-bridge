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

import de.linzn.homeDevices.devices.interfaces.MqttSwitch;
import de.linzn.homeDevices.events.DeviceUpdateEvent;
import de.linzn.stemLinkBridge.connector.OutputWriter;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.eventModule.handler.StemEventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StemEventListener {

    @StemEventHandler
    public void onDeviceSwitch(DeviceUpdateEvent deviceUpdateEvent) {
        MqttSwitch switchableMQTTDevice = deviceUpdateEvent.getSwitchableMQTTDevice();

        String headerChannel = "stemLink_bridge_device_update";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("update_device");
            dataOutputStream.writeUTF(switchableMQTTDevice.getConfigName());
            dataOutputStream.writeBoolean(deviceUpdateEvent.getNewStatus());
        } catch (IOException e) {
            STEMSystemApp.LOGGER.ERROR(e);
        }

        OutputWriter.writeOutputToLink(headerChannel, byteArrayOutputStream.toByteArray());
    }
}
