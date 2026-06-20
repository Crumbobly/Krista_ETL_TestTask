FROM ubuntu:latest
LABEL authors="crumbobly"

ENTRYPOINT ["top", "-b"]
