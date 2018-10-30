package com.lucidworks.fusion.connector.plugin;

import com.lucidworks.fusion.connector.plugin.api.fetcher.Fetcher;
import com.lucidworks.fusion.connector.plugin.api.fetcher.context.FetchContext;
import com.lucidworks.fusion.connector.plugin.api.fetcher.context.PreFetchContext;
import com.lucidworks.fusion.connector.plugin.api.fetcher.context.StartContext;
import com.lucidworks.fusion.connector.plugin.api.message.fetcher.FetchInput;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PnTConnectorFetcher implements Fetcher {

  private final Logger logger;
  private final PnTConnectorConfig pntConnectorConfig;

  private String username;
  private String password;
  private String startFolder;
  private Session session;
  private PnTContentCrawler pnTContentCrawler;
  private List<String> documentIdList;

  @Inject
  public PnTConnectorFetcher(
      Logger logger,
      PnTConnectorConfig pntConnectorConfig
  ) {
    this.logger = logger;
    this.pntConnectorConfig = pntConnectorConfig;
  }

  @Override
  public StartResult start(StartContext startContext) {

    username = pntConnectorConfig.getProperties().getUsername();
    password = pntConnectorConfig.getProperties().getPassword();
    startFolder = pntConnectorConfig.getProperties().getStartFolder();

    session = PnTConnectorClient.getConnectorClient(username,password).getSession();

    pnTContentCrawler = new PnTContentCrawler(startFolder);

    documentIdList = pnTContentCrawler.getAllDocuments(session);

    //List<Phase> phaseList = new ArrayList<>();
    //phaseList.add(Phase.builder("test-phase1").build());
    //phaseList.add(Phase.builder("test-phase2").build());

    return StartResult.DEFAULT;//StartResult.builder().withPhases(phaseList).build();
  }

  @Override
  public PreFetchResult preFetch(PreFetchContext preFetchContext) {

    IntStream.range(0, documentIdList.size()).asLongStream().forEach(i -> {
      logger.info("Emitting candidate -> number {}", i);
      Map<String, Object> data = Collections.singletonMap("number", (int)i);
      preFetchContext.emitCandidate(
          String.valueOf(i), Collections.emptyMap(), data
      );
    });
    return preFetchContext.newResult();
  }

  @Override
  public FetchResult fetch(FetchContext fetchContext) {

    Map<String,Object> contentMap = new HashMap<>();

    FetchInput input = fetchContext.getFetchInput();
    logger.info("Received FetchInput -> {}", input);

    long num = getNumber(input);

    Document document = (Document)session.getObject(documentIdList.get((int)num));
    logger.info("Emitting Document -> number {}", num);

    for(Property p : document.getProperties()){
      contentMap.put(p.getId()+"_s",p.getValueAsString());
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
