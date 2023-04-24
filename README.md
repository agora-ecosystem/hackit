# Hack-it The Debugger for Big Data

[![Build](https://github.com/agora-ecosystem/hackit/actions/workflows/build.yaml/badge.svg)](https://github.com/agora-ecosystem/hackit/actions/workflows/build.yaml)


The debugger for Big Data 

## Introduction

Hackit is a debugger for big data. It is a tool that allows developers to focus
into the code that is causing the error. The debugger is based on the idea of
using the data that is being processed to find the error. The system use the 
paradigm of Tag-snif, the hackit contains all the elements that you most require
to undestand the behaivor of your data.

## requirement
- Java 11
- Maven 3.6.3
- Docker 20.10.7

## Build


to build the project you require to have maven installed and run the following command

```bash
mvn clean package
```

to build the docker image you require to have docker installed and run the following command

```bash
cd hackit-server
docker build -t hackit-server .
```
## Run

To run the project you require to have docker installed and run the following command

```bash
docker run -p 8080:8080 hackit-server
```

## Usage

To use the debugger you require to have a wayang application.

### Hackit Application

you can see the example of a hackit application in the folder hackit-example. To run the examples you will 
require to run the following command

```bash
mvn clean install 
```

```bash
mvn clean \
    compile \
    exec:java \
    -Dexec.mainClass="de.tuberlin.dima.hackit.example.HackitExample[NAME_OF_THE_EXAMPLE]" \
    -Dexec.cleanupDaemonThreads=false \
    -pl hackit-example
```

#### Wayang Application
```bash
mvn clean \
    compile \
    exec:java \
    -Dexec.mainClass="de.tuberlin.dima.hackit.example.HackitExampleWayang" \
    -Dexec.cleanupDaemonThreads=false \
    -pl hackit-example
```
#### Spark Application
```bash
mvn clean \
    compile \
    exec:java \
    -Dexec.mainClass="de.tuberlin.dima.hackit.example.HackitExampleSpark" \
    -Dexec.cleanupDaemonThreads=false \
    -pl hackit-example
```


## Licence

Copyright 2022 DIMA/TU-Berlin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
