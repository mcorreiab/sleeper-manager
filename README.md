# Sleeper Manager

[![CircleCI](https://circleci.com/gh/mcorreiab/sleeper-manager.svg?style=svg)](https://app.circleci.com/pipelines/github/mcorreiab/sleeper-manager)
[![codecov](https://codecov.io/gh/mcorreiab/sleeper-manager/branch/main/graph/badge.svg?token=HV1DK6OF8A)](https://codecov.io/gh/mcorreiab/sleeper-manager)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![kotlin](https://img.shields.io/badge/kotlin-1.6.10-blueviolet?logo=kotlin)](https://kotlinlang.org/)
[![micronaut](https://img.shields.io/badge/micronaut-3.2.6-lightgrey)](https://micronaut.io/)

## What is Sleeper Manager
Sleeper manager is a tool that intends to easy the life of people that play fantasy football in [Sleeper](https://sleeper.app/)

It provides funcionalities using the [public sleeper api ](https://docs.sleeper.app/)

### Funcionalities

Currently, the Sleeper Manager have funcionalities that:
- List all players that have a injury status across all leagues of an user

## Running locally
To run the application locally you will need
- [Docker](https://docs.docker.com/desktop/)
- [Docker compose](https://docs.docker.com/compose/install/)

To get the system up and running, use

``` shell
docker-compose up -d
```

Then, go to http://localhost:8080/swagger-ui for documentation
