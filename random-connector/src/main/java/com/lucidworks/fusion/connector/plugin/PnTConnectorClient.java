package com.lucidworks.fusion.connector.plugin;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PnTConnectorClient {

  private static final Logger logger = LogManager.getLogger(PnTConnectorClient.class);

  private SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
  private Session session;
  //  private String CMIS_BROWSER_BINDING_URL =
  //
  // "https://alfpnt.intranet.dev.int.devlab.redhat.com/alfresco/api/-default-/public/cmis/versions/1.1/browser";
  File keyStore;

  private static PnTConnectorClient connectorClient = null;

  private PnTConnectorClient(String username, String password, String folder) {

    /*System.out.println("GETTING TRUST STORE");

    try {
       keyStore = stream2file(getClass().getResourceAsStream("/pnt-portal.jks"));
    } catch (IOException e) {
        e.printStackTrace();
    }

    System.setProperty("javax.net.ssl.trustStore",keyStore.getPath());
    System.setProperty("javax.net.ssl.trustStorePassword ","changeit");
    */
    Map<String, String> parameter = new HashMap<String, String>();
    parameter.put(SessionParameter.USER, username);
    parameter.put(SessionParameter.PASSWORD, password);
    parameter.put(SessionParameter.ATOMPUB_URL, folder); // diff binding per type??
    // TODO: binding Type in config?
    // parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
    parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

    logger.info("Session using " + username + " -> " + folder);

    List<Repository> repositories = this.sessionFactory.getRepositories(parameter);

    if (!CollectionUtils.isEmpty(repositories)) {
      Repository repository = repositories.get(0);
      this.session = repository.createSession();
    }
  }

  public static PnTConnectorClient getConnectorClient(
      String username, String password, String startLink) {
    if (connectorClient == null) {
      connectorClient = new PnTConnectorClient(username, password, startLink);
    }

    return connectorClient;
  }

  public Session getSession() {
    return session;
  }

  public File stream2file(InputStream inputStream) throws IOException {
    final File tempFile = File.createTempFile("stream2file", ".tmp");
    tempFile.deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(inputStream, out);
    }
    return tempFile;
  }
}
