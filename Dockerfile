FROM gcr.io/gcp-runtimes/ubuntu_18_0_4:latest AS build
ADD . /app
WORKDIR /app
USER root
# Update
RUN apt-get -y update

# Install GIT
RUN apt-get -y install git

# Install OpenJDK-11
RUN apt-get install -y openjdk-11-jre-headless && \
    apt-get clean;

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

# Install Maven

ARG MAVEN_VERSION=3.8.3
ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
 && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
 && rm -f /tmp/apache-maven.tar.gz \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Define default command.
CMD ["mvn", "--version"]

# Clone project
RUN git clone https://github.com/Brebu/bqtobq.git

RUN mvn -f /app/bqtobq/pom.xml install -P dataflow-runner

FROM gcr.io/dataflow-templates-base/java11-template-launcher-base:latest

COPY --from=build /app/bqtobq/target/BqToBq-bundled-1.0.jar /template/

ENV FLEX_TEMPLATE_JAVA_CLASSPATH=/template/*
ENV FLEX_TEMPLATE_JAVA_MAIN_CLASS=poc.gcp.BqToBqPipeline