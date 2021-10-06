package poc.gcp.options;


import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.ValueProvider;

@Description("Configures the BqToBq export")
public interface OptionsBqToBq extends DataflowPipelineOptions {

    @Description("The path for input table.")
    @Validation.Required
    ValueProvider<String> getInputTable();

    void setInputTable(ValueProvider<String> value);

    @Description("The path for output table.")
    @Validation.Required
    ValueProvider<String> getOutputTable();

    void setOutputTable(ValueProvider<String> value);

    @Description("Temporary directory for BigQuery loading process.")
    @Validation.Required
    ValueProvider<String> getBigQueryLoadingTemporaryDirectory();

    void setBigQueryLoadingTemporaryDirectory(ValueProvider<String> value);

}