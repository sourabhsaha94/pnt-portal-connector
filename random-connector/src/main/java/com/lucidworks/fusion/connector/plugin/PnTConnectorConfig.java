package com.lucidworks.fusion.connector.plugin;

import com.lucidworks.fusion.connector.plugin.api.config.ConnectorConfig;
import com.lucidworks.fusion.connector.plugin.api.config.FetcherProperties;
import com.lucidworks.fusion.schema.SchemaAnnotations.Property;
import com.lucidworks.fusion.schema.SchemaAnnotations.RootSchema;

@RootSchema(
    name = "redhat.pnt.connector",
    title = "PnT Connector",
    description = "A connector that retrieves documents from alfresco instance",
    category = "CMIS"
)
public interface PnTConnectorConfig extends ConnectorConfig<PnTConnectorConfig.Properties> {

  @Property(
      title = "Properties",
      required = true
  )
  public Properties getProperties();

  /**
   * Connector specific settings
   */
  interface Properties extends FetcherProperties {

    @Property(
            title = "Username",
            description = "User who is accessing the alfresco instance"
    )
    public String getUsername();

    @Property(
            title = "Password"
    )
    public String getPassword();

    @Property(
            title = "Start folder",
            description = "Start location of the crawl (location must start with / and not end with /)"
    )
    public String getStartFolder();

  }

}
