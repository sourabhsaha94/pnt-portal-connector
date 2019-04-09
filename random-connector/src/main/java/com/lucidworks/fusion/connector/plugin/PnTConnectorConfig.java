package com.lucidworks.fusion.connector.plugin;

import com.lucidworks.fusion.connector.plugin.api.config.ConnectorConfig;
import com.lucidworks.fusion.connector.plugin.api.config.ConnectorPluginProperties;
import com.lucidworks.fusion.schema.SchemaAnnotations.Property;
import com.lucidworks.fusion.schema.SchemaAnnotations.RootSchema;
import com.lucidworks.fusion.schema.SchemaAnnotations.StringSchema;

@RootSchema(
    name = "redhat.pnt.connector",
    title = "PnT Connector",
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
    //@StringSchema(encrypted = true) Adding this line leads to configuration errors
    String password();

    //Used this in url https://alfpnt.intranet.dev.int.devlab.redhat.com/alfresco/api/-default-/public/cmis/versions/1.1/atom
    @Property(
        title = "Alfesco Url",
        description = "")
    @StringSchema(defaultValue = "https://pnt.stage.redhat.com/alfresco/api/-default-/public/cmis/versions/1.1/atom")
    String url();

    //Used this in startFolder /Sites/pnt-portal/documentLibrary/Customer success
    @Property(
        title = "Start folder",
        description = "Start location of the crawl (location must start with / and not end with /)"
    )
    @StringSchema(defaultValue = "/Sites/pnt-portal/documentLibrary")
    String startFolder();
  }
}
