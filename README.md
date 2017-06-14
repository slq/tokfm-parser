# TokFM Parser [![Build Status](https://travis-ci.org/slq/tokfm-parser.svg?branch=master)](https://travis-ci.org/slq/tokfm-parser)
Parser / downloader for tokfm audition archives.

### Usage

```java
git clone https://github.com/slq/tokfm-parser
cd tokfm-parser
./gradlew fatJar
java -jar build/libs/tokfm-all-1.0-SNAPSHOT.jar
```

Download will start immediately with the following console output:

```
~/dev/java/tokfm-parser (master)
λ java -jar build\libs\tokfm-all-1.0-SNAPSHOT.jar
2017-06-14 0620 - Pierwsze Śniadanie w TOK-u - Portoryko stanie się 51. stanem USA. Mówi Lech Miodek.mp3                                               : 5,19
2017-06-14 0600 - Pierwsze Śniadanie w TOK-u - WWF Rżnięcie puszczy - bezzasadne. O wynikach raportu mówi Dariusz Gatkowski.mp3                        : 7,04
2017-06-14 0500 - Pierwsze Śniadanie w TOK-u - Poranny przegląd prasy.mp3                                                                              : 16,58
2017-06-13 2300 - Wieczór - Tomasz Stawiszyński - Czy wolno jeść franciszkanów, czy wolno jeść zwierzęta. Spór między kreacjonizmem i ewolucjonizmem.m : 23,98
2017-06-13 2200 - Godzina Filozofów - O wynajmowaniu, a nie posiadaniu - jak zmienia się kapitalizm.mp3                                                : 22,27
2017-06-13 2120 - Wieczór - Tomasz Stawiszyński - Napięcie polityczne wokół Korei.mp3                                                                  : 13,76
2017-06-13 2000 - Salon - Goście Passenta (Daniel Passent) - Goście 'Salonu' Anna Materska-Sosnowska, Jacek Kucharczyk, Jarosław Gugała.mp3            : 23,57
2017-06-13 1940 - Codzienny Magazyn Motoryzacyjny - Test Ford Kuga po faceliftingu.mp3                                                                 : 5,91
2017-06-13 1920 - Jeszcze Więcej Sportu - Rozgrywki tenisowe w nadchodzącym sezonie.mp3                                                                : 7,71
2017-06-13 1900 - Post Factum - Paweł Sulik - Po wyborach Theresa May negocjuje z opozycją i spotyka się z Emmanuelem Macron.mp3                       : 4,58
2017-06-13 1840 - Post Factum - Paweł Sulik - Historia uwięzionego w Rosji Murada Amrieva.mp3                                                          : 7,27
2017-06-13 1820 - Post Factum - Paweł Sulik - Raport NIK pokazuje fatalny stan polskiego górnictwa.mp3                                                 : 5,77
2017-06-13 1800 - Post Factum - Paweł Sulik - Greenpeace blokuje wycinkę drzew w Puszczy Białowieskiej. Relacja naszego reportera Jakuba Medka..mp3    : 4,75
2017-06-13 1800 - Przewodnik Technologiczny TOK FM - Unikajcie logowania się na swoje konta na publicznych komputerach.mp3                             : 0,39
2017-06-13 1740 - Kultura Osobista - Filmowa uczta we Wrocławiu - co na Nowych Horyzontach.mp3                                                         : 6,71
2017-06-13 1700 - Godziny Szczytu - magazyn reporterów - Baśka miała fajne... wodowanie.mp3                                                            : 10,57
2017-06-13 1640 - Co się stało - Komisja Europejska wszczyna procedurę przeciwko Polsce.mp3                                                            : 7,14
2017-06-13 1600 - Popołudnie Radia TOK FM - Kulisy Kremla - o książce Michaiła Zygara opowiadała Anna Łabuszewska.mp3                                  : 10,46
2017-06-13 1540 - Popołudnie Radia TOK FM - Lokatorzy jeszcze poczekają, aż minie spektakl polityczny - o działaniach Komisji Weryfikacyjnej.mp3       : 6,47
2017-06-13 1520 - Popołudnie Radia TOK FM - Porażka populistycznego Ruchu Pięciu Gwiazd w wyborach samorządowych we Włoszech.mp3                       : 5,48...
```

Make sure your console is wide enough (at least 150 characters), so that download status is updated smoothly.

`TokFM-Parser` will update downloading status showing the amout of MB downloaded.

When file already exists, parser will exit. This default behaviour can be overriden by using `-s` or `--skip` flag, which will allow parser to continue downloading even if file already exists (existing file will be skipped, not overwritten).

###### Available options:
```java
-m Mask to use, for example: Ziobro|Zandberg|Sikorski
-s Skip existing file
```

### Filename matching
When `mask` is provided (using `-m` or `--mask` flag), it will be used for `REGEX` matching as `matchPattern` shown below:
```java
Pattern.compile(matchPattern, Pattern.CASE_INSENSITIVE)
```

For example:
```java
-m "Ziobro|Zandberg|Sikorski"
```
will match every podcasrt with either `Ziobro`, `Zandberg` or `Sikorski` mentioned in title.
```java
λ java -jar build\libs\tokfm-all-1.0-SNAPSHOT.jar -m "Ziobro|Zandberg|Sikorski"
Found matched text: (Zandberg)
2017-06-06 0720 - Poranek - Jan Wróbel - Powinniśmy handel niedzielnym ograniczać - Adrian Zandberg z Partii Razem.mp3                                 : 5,49
Found matched text: (Ziobro)
2017-05-24 1520 - Światopodgląd - Błaszczak i Ziobro przed Sejmem w sprawie Igora Stachowiaka.mp3                                                      : 5,43
Found matched text: (Ziobro)
2017-05-21 0900 - Świat się chwieje - Czy Ziobro jest karą za grzechy sędziów.mp3                                                                      : 24,15
Found matched text: (Zandberg)
2017-05-12 1200 - A teraz na poważnie - Zandberg Stanowisko PO ws. przyjmowania uchodźców jest nieprzyzwoite. Grzegorz Schetyna jest cynikiem..mp3     : 10,27
```

