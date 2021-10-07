FROM gcr.io/dataflow-templates-base/java11-template-launcher-base:latest

ENV FLEX_TEMPLATE_JAVA_CLASSPATH=/template/*
ENV FLEX_TEMPLATE_JAVA_MAIN_CLASS=poc.gcp.BqToBqPipeline

COPY /target/BqToBq-bundled-1.0.jar /template/