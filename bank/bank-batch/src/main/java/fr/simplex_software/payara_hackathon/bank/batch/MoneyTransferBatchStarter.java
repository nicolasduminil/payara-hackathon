package fr.simplex_software.payara_hackathon.bank.batch;

import jakarta.annotation.*;
import jakarta.batch.runtime.*;
import jakarta.ejb.*;
import jakarta.ejb.Singleton;
import jakarta.inject.*;
import org.eclipse.microprofile.config.inject.*;
import org.slf4j.*;

@Singleton
@Startup
public class MoneyTransferBatchStarter
{
  private static final Logger slf4jLogger = LoggerFactory.getLogger(MoneyTransferBatchStarter.class);
  @Inject
  @ConfigProperty(name = "money-transfer.batch.starter.jobID")
  private String jobID;

  @PostConstruct
  public void onStartup()
  {
    slf4jLogger.info("*** BankBatchStarter.onStartup(): starting job {}", jobID);
    BatchRuntime.getJobOperator().start(jobID, null);
    slf4jLogger.info("*** BankBatchStarter.onStartup(): The job {} have been started and executed", jobID);
  }
}
