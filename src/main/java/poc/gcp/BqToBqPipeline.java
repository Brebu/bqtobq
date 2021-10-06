package poc.gcp;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import poc.gcp.options.OptionsBqToBq;

public class BqToBqPipeline {

    public static void main(String[] args) {

        final OptionsBqToBq options = PipelineOptionsFactory.fromArgs(args).withValidation().as(OptionsBqToBq.class);

        Pipeline pipeline = Pipeline.create(options);

        pipeline.apply("Read from BigQuery", BigQueryIO.readTableRows()
                        .from(options.getInputTable())
                        .withMethod(BigQueryIO.TypedRead.Method.DIRECT_READ))
                .apply("Write to BigQuery", BigQueryIO.writeTableRows()
                        .withoutValidation()
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withCustomGcsTempLocation(options.getBigQueryLoadingTemporaryDirectory())
                        .to(options.getOutputTable()));

        pipeline.run();
    }
}
