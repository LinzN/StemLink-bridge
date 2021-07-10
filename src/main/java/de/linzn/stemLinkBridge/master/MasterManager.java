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

package de.linzn.stemLinkBridge.master;

import de.stem.stemSystem.STEMSystemApp;

public class MasterManager {

    public MasterManager() {
        STEMSystemApp.LOGGER.CONFIG("Starting StemLink bridge in MasterMode");
    }

    public void register() {
        STEMSystemApp.getInstance().getStemLinkModule().getStemLinkServer().registerEvents(new StemLinkMasterListener());
        STEMSystemApp.LOGGER.CONFIG("StemLinkMasterListener registered");
    }

    public void unregister() {

    }
}
