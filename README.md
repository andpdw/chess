# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoARiqfJCIK-P8gK0eh8LlEB-rgeWKkwes+h-DsXzQo8wEVG+XYwAgQnitOlCCcJ162ZSw78gyTJTspXI3jS+73sKMBihKboynKZbvEqmAqsGGpukaaAQMwABmvhNsYPpefSMA9n2gW8neSa+s6dmFf2HllaBroDAAcmlIpZQxoE+qJ9QNOmTidFmyw5qoebzNBRYlvUejriihJqGASx0dlOi5be3kbu6W55QKtUVfVm7yNuhjiVQSLwPEKAgGqTSmRpGLqTsayRdopIJeqMAAJJoCA0AouAMBXVsOwHT6dUwCMA38sNBZjGN0D1D40yXtASAAF4oLsMBXO1ZWdY06YAIx9aM4O5vmo3FrDCoI3qSOo+jC1tRVgqbWOE4oM+8Tnpe16bSFK5rgGXNbvFqoajDsDSCg3AEA4AvwYh6CaEtZXMzAYvFQuW02Ttqvk1AQNlR2NQvGMg2Q4sYv1BiYsBmu4qOOzVHoMSDZYxUOMNAArHhhPZhDpOFrrlvW4+duy5R8toM79OmDVFR6XUrnihkqgAZg8cgdU+ljBRjvISRqEu5n8LJsgqaNLhvUETnEc0Wh9GMd4fj+F4KDoDEcSJC3beub4WCiYKIMNNIEb8RG7QRt0PRyaoCnDNXSHaZS8f1PPCvp1rnb1A59i95zctIaStkVCrjIsmyq+R+rwVLqF4Xro98jarq+oX8LiUwE1a1ha1ivLUFI7dl7P2Hm21N5VX1hvI2YMTZ+xGgHcalMKI0zRvNRe2NS7YVxk4AmBFTb+2hoHRBiMoAoxQdHHKysVr0gfsAK+pUjpIhofrcoKsOCSyZGzS8uc97hwPnQh0N96gSylmoMOucwreBmGtD0L1Ra6xkOw6W9suER1-pQ-+q01YgMgSdNWsdyoSTAjAkmI0CEIKtvI4RTJQ4OwjlHNBbsMFgC6l7SuoxjFDXwRbGAFiSwKKljYlRB8Gz130cvASQle7J1TuvI6Wc8GmJTvuQuhiOpOK6hXboIxo6eEbgEFE65-DYHFBqfiaIYAAHElQaH7nE0sDQKlj0nvYJUc8gkKyxrpKy-pX7r3KLZLeaIqk5h4bnQ+FVj5UNHDAU+U4L5zg0YuSoy4wrinvjObQT89TGnaYUWRH8IBfxavDBiFDJmLOmeA7R-SdrgP0XVaBCSobePhkgkhtNUGu3KO7fGPsPFm3gRTV5xDSG7HIUrc5JVvJMOuYbRhGz9o1RYVM+oZScjDLUBiBZUKllCnqBUpkAY+yVOqW-V61sCVgCUSSnMajIUa3qFoqZwNtZ6MZgbEGvsTF7G8b46AlSmTUoxaoexXz3auL+U8sm5iKWCtDsK+x9cznIouai8pEB0o0rUPwxcOiXREoQMKiBXTYTPEDIa0lsSbmGJeMsFpOYCwNHGPalAb1pAFjxuEYIgQQSbHiLqFAyVIKLG+MkUAaog1GW+C6hqSoQ0XBgJ0TGRc0klGceXb2-UxgutUI651So3Ueq9T65YfqA2RtMaGhA4aK17GjUqWNcx42JpyUxJuHAADsbgnAoCcDECMwQ4BcQAGzwFZlqmARQnEDyLl1VoHRmmtKprw9AWYY1KmTakuO3TSwXzWOuuYacd2UgGTAQ8cgUAYtGRHfdDalTjM7PS-cPkz6YnmTqrayzb5rOkZsnU2zX77M-m6b+Jy6UqpxZcoBH6WVgOg-c2djzYHPMIcC6m7yUEYwcd89JWCcHuKlYC0saH4jILBY2U5EKIMMphcyjlO0mFIoGXAVmV6D0BX2QAIRDB-OKCGbWg3+V4whk0YDTSJHNTdiZHFpq6j1SVyHpUU1E+J2aISmxIpVixo8l6lQYnY9ijWvMx06YFBirZ+p2NkrkX4ylQrSXKpVkyi5sHjr6rZZ2DOAmuWeNMby622m8gywxaKlN6DZONAlbgxTRGg7yMC-ZuYiqNPsu3aauoGK3VHvS6554+a5iFsWJ671KTpM4dkw0TJWaXWFZgMVwIWH665OYv4SwksHKbHbkgBIYA2t9ggJ1gAUhAcUE7-BhoulOtNM6BPNGZDJHoLq2n71XaMbA1a2tQDgBAByUBb0FekFJjCaWvwr12WsdbwBNvbd2-t110hstfhPdrAAViNtAV690wEu9dnb0A7tuofW5lAT6AEzN8ufXZhnr5fvqHfX9j9-0v12dZg5Ryf6OZRQVaD1yBl3NS9aqBxNfMoYQSRsjnywsybLt1bBCnuVKeI8uin4K-6QdqLRlz9HN5MP2R9KgJokDrlfATh5Qm4FmOUzx1TORSvHep9hWnbifMAsl6WFTKAZqy+jpprHlKr3Q9KrDgVYAzNxX2bKql8qHNUac7rGDoDgeMvt-ow28TFNq7i34u+1ukty+LuVmnUWCMxc9z462Pvgv3vU5RtnDKJ0elx9rDFMiRbkGwGlGAYYF78aNoJwjYeTRmhrOGZCR2A8-IzPTknjOV5VnNDAWs9Ydfsuo8+mAw3xRXq+rdnZepIB8JAcbx8oiKID7bpAGZFZMqwB79AY1y8yA+H3I96ykD3cM7GEkkc-vU008yaDVteTm5QCu11nrXgz+IGDLAYA2B1uEDyAUSdtSDF56HiPMeE9ejGDQSa07RgiiKA0gnGq+x2hOJ0bCIiIBWKB0oOq0UBHCMBhuAixuVi6IMgoBFuliiidspgSem8HmwOuWdQSGDOsW4eOBASDgzsWGYquGnsmaIe5BYefK4suBNBMe+BdGDC+qS+K+YSO69Q-BI4YBAenK5ee+mClW3sh+FGmAQAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
