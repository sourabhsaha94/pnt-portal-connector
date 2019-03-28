package com.lucidworks.fusion.connector.plugin;

import com.lucidworks.fusion.connector.plugin.api.plugin.ConnectorPlugin;
import com.lucidworks.fusion.connector.plugin.api.plugin.ConnectorPluginModule;
import org.pf4j.PluginWrapper;

import javax.inject.Inject;

public class PnTConnectorPlugin extends ConnectorPluginModule {

  @Inject
  public PnTConnectorPlugin(PluginWrapper wrapper) {
    super(wrapper);
  }

  @Override
  public ConnectorPlugin getConnectorPlugin() {

    return builder(PnTConnectorConfig.class)
        .withFetcher("content", PnTConnectorFetcher.class)
        .build();
  }
}
