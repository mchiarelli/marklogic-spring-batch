package com.marklogic.spring.batch.item.tasklet;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.datamovement.DeleteListener;
import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.client.query.QueryDefinition;
import com.marklogic.client.datamovement.DataMovementManager;
import com.marklogic.client.datamovement.JobTicket;
import com.marklogic.client.datamovement.QueryBatcher;
import com.marklogic.client.query.StructuredQueryDefinition;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DeleteDocumentsTasklet implements Tasklet {

    private DatabaseClient databaseClient;
    private StructuredQueryDefinition queryDefinition;

    public DeleteDocumentsTasklet(DatabaseClientProvider databaseClientProvider, StructuredQueryDefinition queryDef) {
        this.databaseClient = databaseClientProvider.getDatabaseClient();
        this.queryDefinition = queryDef;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final DataMovementManager dataMovementManager = databaseClient.newDataMovementManager();
        QueryBatcher qb = dataMovementManager.newQueryBatcher(queryDefinition)
                .withBatchSize(5)
                .withThreadCount(2)
                .withConsistentSnapshot()
                .onUrisReady(new DeleteListener())
                .onQueryFailure(throwable -> throwable.printStackTrace());
        JobTicket ticket = dataMovementManager.startJob(qb);
        qb.awaitCompletion();
        dataMovementManager.stopJob(ticket);
        return RepeatStatus.FINISHED;
    }

}
