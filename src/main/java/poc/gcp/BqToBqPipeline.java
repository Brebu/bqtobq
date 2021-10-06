package poc.gcp;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import poc.gcp.options.OptionsBqToBq;

public class BqToBqPipeline {

    public static void main(String[] args) {

        final OptionsBqToBq options = PipelineOptionsFactory.fromArgs(args).withValidation().as(OptionsBqToBq.class);

        Pipeline pipeline = Pipeline.create(options);

        pipeline.apply("Read from BigQuery", readFromBigQuery(options))
                .apply("Write to BigQuery", writeToBigQuery(options));

        pipeline.run();
    }

    private static BigQueryIO.TypedRead<@UnknownKeyFor @NonNull @Initialized TableRow> readFromBigQuery(OptionsBqToBq options) {
        return BigQueryIO.readTableRows()
                .from(options.getInputTable())
                .withMethod(BigQueryIO.TypedRead.Method.DIRECT_READ);
    }

    private static BigQueryIO.Write<@UnknownKeyFor @NonNull @Initialized TableRow> writeToBigQuery(OptionsBqToBq options) {
        return BigQueryIO.writeTableRows()
                .withoutValidation()
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                .withCustomGcsTempLocation(options.getBigQueryLoadingTemporaryDirectory())
                .to(options.getOutputTable());
    }

}
