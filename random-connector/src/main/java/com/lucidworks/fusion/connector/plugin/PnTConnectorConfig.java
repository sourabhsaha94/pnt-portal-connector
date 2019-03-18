package com.lucidworks.fusion.connector.plugin;

import com.lucidworks.fusion.connector.plugin.api.config.ConnectorConfig;
import com.lucidworks.fusion.connector.plugin.api.config.ConnectorPluginProperties;
import com.lucidworks.fusion.schema.SchemaAnnotations.Property;
import com.lucidworks.fusion.schema.SchemaAnnotations.RootSchema;
import com.lucidworks.fusion.schema.SchemaAnnotations.StringSchema;

@RootSchema(
    name = "redhat.pnt.connector",
    title = "PnT Connector", // TODO What does Pnt mean?
    description = "A connector that retrieves documents from alfresco instance",
    category = "CMIS"
)
public interface PnTConnectorConfig extends ConnectorConfig<PnTConnectorConfig.Properties> {

  @Property(title = "Properties", required = true)
  Properties properties();

  interface Properties extends ConnectorPluginProperties {

    @Property(title = "Username", description = "User who is accessing the alfresco instance")
    String username();

    @Property(title = "Password")
    @StringSchema(encrypted = true)
    String password();

    @Property(
        title = "Alfesco Url",
        description = "")
    String url();

    @Property(
        title = "Start folder",
        description = "Start location of the crawl (location must start with / and not end with /)"
    )
    @StringSchema(defaultValue = "/")
    String startFolder();
  }
}
