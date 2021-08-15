# twm-mkt-eq-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Tech Stack:

1. Java 11
2. Quarkus
3. Lombok

## Requirements

1. Install the latest jar of the project: https://github.com/crackCodeLogn/twm-artifactory, using ```mvn clean install```
2. Generate jar of current project using ```mvn clean install```
3. Before starting the app, define a ```holdings.csv``` file inside ```twm-mkt-eq-service/landing```, which would contain the stocks you are holding in CSV format (below is tabular only for clarity):

symbol | qty | buy_price |
--- | --- | --- 
HDFCBANK | 11 | 1520
SBIN | 15 | 430.75

4. Set a variable in your system for ```TWM_HOME_PARENT```, which defines a project structure defined in the ```twm-mkt-eq-service/bin/twm-mkt-eq-service.sh```
5. Make sure you have java configured.
6. Indian market only. This also considers the EMH - Efficient Market Hypothesis, which assumes that the stock price on NSE and BSE are the same.

## Starting the application locally

1. Start ```./twm-mkt-eq-service.sh <provide-pnL-calculation-interval-here-in-seconds>``` from terminal

## Stopping application running locally

1. Ctrl + C the terminal where the twm-mkt-eq-service jar is running.

## Output:

Based on the sample selected above, below is the output for the run:-

#### // Will add snap on running trading day.  