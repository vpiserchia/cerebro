# Build web assets (frontend)
FROM node as builder-grunt

RUN mkdir /app
COPY . /app
WORKDIR /app

RUN npm install -g grunt
RUN npm install
RUN grunt build

# Build scala (backend)
FROM sbtscala/scala-sbt:eclipse-temurin-jammy-11.0.17_8_1.8.2_2.13.10 as builder-scala

ARG CEREBRO_VERSION

RUN mkdir /app /opt/cerebro
WORKDIR /app
COPY --from=builder-grunt /app /app
RUN sbt packageZipTarball
RUN tar --strip-components=1 -C /opt/cerebro -xf /app/target/universal/cerebro-${CEREBRO_VERSION}.tgz
RUN sed -i '/<appender-ref ref="FILE"\/>/d' /opt/cerebro/conf/logback.xml

# Package docker image
FROM eclipse-temurin:8-jre-jammy

COPY --from=builder-scala /opt/cerebro /opt/cerebro

RUN mkdir -p /opt/cerebro/logs \
    && addgroup -gid 1000 cerebro \
    && adduser -q --system --no-create-home --disabled-login -gid 1000 -uid 1000 cerebro \
    && chown -R root:root /opt/cerebro \
    && chown -R cerebro:cerebro /opt/cerebro/logs \
    && chown cerebro:cerebro /opt/cerebro

WORKDIR /opt/cerebro
USER cerebro

ENTRYPOINT [ "/opt/cerebro/bin/cerebro" ]