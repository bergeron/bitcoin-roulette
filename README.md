# Bitcoin Roulette

![Bitcoin Roulette Gif](BitcoinRoulette.gif)


###To Run

(0) Configure DB

```
BitcoinRouletteServer/src/main/java/pw/bitcoinroulette/server/hibernate.cfg.xml
```



(1) Build

The client and server communicate through RMI, and share classes through BitcoinRouletteLibrary.  Maven has two build profiles:
```
mvn package -Pclient
mvn package -Pserver
```

Which build:

```
BitcoinRouletteClient/target/bitcoinrouletteclient-1.0.jar
BitcoinRouletteServer/target/bitcoinrouletteserver-1.0.jar
```

(2) Run bitcoind

```
bitcoind -blocknotify="echo '%s' | nc 127.0.0.1 4001" -walletnotify="echo '%s' | nc 127.0.0.1 4002" -alertnotify="echo '%s' | nc 127.0.0.1 4003" -daemon
```

(Ports used by [BitcoindClient4J](https://github.com/johannbarbie/BitcoindClient4J#blockchain-events))

(3) Run

```
 java -jar BitcoinRouletteServer/target/bitcoinrouletteserver-1.0.jar
 java -jar BitcoinRouletteClient/target/bitcoinrouletteclient-1.0.jar
```


