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

package de.linzn.stemLinkBridge.restApi;


import de.linzn.restfulapi.api.jsonapi.IRequest;
import de.linzn.restfulapi.api.jsonapi.RequestData;
import de.linzn.stemLink.connections.ClientType;
import de.linzn.stemLinkBridge.StemLinkBridgePlugin;
import de.stem.stemSystem.STEMSystemApp;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.UUID;

public class GET_StemLink implements IRequest {
    @Override
    public Object proceedRequestData(RequestData requestData) {
        JSONObject jsonObject = new JSONObject();
        StemLinkBridgePlugin.stemLinkBridgePlugin.getSlaveManager();
        String mode;

        if (!StemLinkBridgePlugin.stemLinkBridgePlugin.isEnabled()) {
            mode = "DISABLED";
        } else if (StemLinkBridgePlugin.stemLinkBridgePlugin.isMasterMode()) {
            mode = "MASTER";
        } else {
            mode = "SLAVE";
        }

        JSONObject clientListWithTypes = new JSONObject();

        for (ClientType clientType : ClientType.values()) {
            JSONObject clientList = new JSONObject();
            Set<UUID> uuids = STEMSystemApp.getInstance().getStemLinkModule().getStemLinkServer().getClientsByType(clientType).keySet();
            JSONArray uuidArray = new JSONArray();
            for (UUID uuid : uuids) {
                uuidArray.put(uuid.toString());
            }

            clientList.put("amount", uuids.size());
            clientList.put("clients", uuidArray);
            clientListWithTypes.put(clientType.name(), clientList);
        }

        jsonObject.put("mode", mode);
        jsonObject.put("clients", clientListWithTypes);
        return jsonObject;
    }

    @Override
    public Object genericData() {
        return proceedRequestData(null);
    }

    @Override
    public String name() {
        return "stemlink";
    }
}
