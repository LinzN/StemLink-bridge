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

package de.linzn.stemLinkBridge;


import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.stemLinkBridge.master.MasterManager;
import de.linzn.stemLinkBridge.slave.SlaveManager;

import java.util.Arrays;
import java.util.UUID;

public class StemLinkBridgePlugin extends STEMPlugin {

    public static StemLinkBridgePlugin stemLinkBridgePlugin;
    private SlaveManager slaveManager;
    private MasterManager masterManager;
    private boolean masterMode;
    private boolean enabled;


    public StemLinkBridgePlugin() {
        stemLinkBridgePlugin = this;
    }

    @Override
    public void onEnable() {
        setUpConfig();
        startup();
    }

    @Override
    public void onDisable() {
        if (this.enabled) {
            if (this.masterMode) {
                this.masterManager.unregister();
            } else {
                this.slaveManager.unregister();
            }
        }
    }

    private void startup() {
        if (this.enabled) {
            if (this.masterMode) {
                this.masterManager = new MasterManager();
                this.masterManager.register();
            } else {
                this.slaveManager = new SlaveManager();
                this.slaveManager.register();
            }
        } else {
            STEMApp.LOGGER.CONFIG("StemLink bridge is disabled!");
        }
    }


    private void setUpConfig() {
        this.enabled = this.getDefaultConfig().getBoolean("connector.enable", false);
        this.masterMode = this.getDefaultConfig().getBoolean("connector.isMaster", true);
        this.getDefaultConfig().getString("connector.masterAddress", "10.40.0.10");
        this.getDefaultConfig().getString("connector.uuid", UUID.randomUUID().toString());
        this.getDefaultConfig().getInt("connector.masterPort", 11101);
        this.getDefaultConfig().getString("connector.crypt.cryptAESKey", "3979244226452948404D635166546A576D5A7134743777217A25432A462D4A61");
        this.getDefaultConfig().getString("connector.crypt.vector16B", Arrays.toString(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7}));
        this.getDefaultConfig().save();
    }

    public boolean isMasterMode() {
        return masterMode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SlaveManager getSlaveManager() {
        return slaveManager;
    }

    public MasterManager getMasterManager() {
        return masterManager;
    }
}
