package com.lucidworks.fusion.connector.plugin;

import com.lucidworks.fusion.connector.plugin.api.fetcher.result.FetchResult;
import com.lucidworks.fusion.connector.plugin.api.fetcher.result.PreFetchResult;
import com.lucidworks.fusion.connector.plugin.api.fetcher.result.StartResult;
import com.lucidworks.fusion.connector.plugin.api.fetcher.type.content.ContentFetcher;
import com.lucidworks.fusion.connector.plugin.api.fetcher.type.content.FetchInput;
import com.lucidworks.fusion.connector.plugin.api.fetcher.type.content.MessageHelper;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.IntStream;

public class PnTConnectorFetcher implements ContentFetcher {

  private static final Logger logger = LogManager.getLogger(PnTConnectorFetcher.class);

  private final PnTConnectorConfig pntConnectorConfig;

  private String username;
  private String password;
  private String startFolder;
  private String url;

  private Session session;
  private PnTContentCrawler pnTContentCrawler;
  private List<String> documentIdList;

  @Inject
  public PnTConnectorFetcher(PnTConnectorConfig pntConnectorConfig) {
    this.pntConnectorConfig = pntConnectorConfig;
  }

  @Override
  public StartResult start(StartContext startContext) {
    username = pntConnectorConfig.properties().username();
    password = pntConnectorConfig.properties().password();
    url = pntConnectorConfig.properties().url();
    startFolder = pntConnectorConfig.properties().startFolder();

    session = PnTConnectorClient.getConnectorClient(username, password, url).getSession();
    pnTContentCrawler = new PnTContentCrawler(startFolder);


    return startContext.newResult();
  }

  @Override
  public PreFetchResult preFetch(PreFetchContext preFetchContext) {

    documentIdList = pnTContentCrawler.getAllDocuments(session);

    IntStream.range(0, documentIdList.size())
        .asLongStream()
        .forEach(
            i -> {
              logger.info("Emitting candidate -> number {}", i);
              Map<String, Object> data = Collections.singletonMap("number", (int) i);
              preFetchContext.emitCandidate(
                  MessageHelper.candidate(String.valueOf(i), Collections.emptyMap(), data).build());
            });
    return preFetchContext.newResult();
  }

  @Override
  public FetchResult fetch(FetchContext fetchContext) {

    Map<String, Object> contentMap = new HashMap<>();

    FetchInput input = fetchContext.getFetchInput();
    logger.info("Received FetchInput -> {}", input);

    long num = getNumber(input);

    Document document = (Document) session.getObject(documentIdList.get((int) num));
    logger.info("Emitting Document -> number {}", num);

    for (Property p : document.getProperties()) {
      contentMap.put(p.getId() + "_s", p.getValueAsString());
    }

    fetchContext.emitDocument(contentMap);

    return fetchContext.newResult();
  }

  private long getNumber(FetchInput input) {
    Object num = input.getMetadata().get("number");
    if (num instanceof Long) {
      return (Long) num;
    } else if (num instanceof Integer) {
      return (Integer) num;
    } else {
      throw new RuntimeException(String.format("Invalid value for number: %s", num));
    }
  }
}
