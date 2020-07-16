From openjdk:8

ARG jarToCopy

ENV jarToCopy $jarToCopy

copy ./target/$jarToCopy $jarToCopy

CMD java -jar ${jarToCopy}