FROM bellsoft/liberica-openjdk-alpine-musl:11 AS server-build
WORKDIR /project1
COPY ./server src/server
RUN find . -name "*.java" -print | xargs javac

FROM bellsoft/liberica-openjdk-alpine-musl:11
WORKDIR /
COPY --from=server-build /project1 /project1

EXPOSE 3000/tcp

CMD ["java", "-cp", "/project1", "src.server.Server", "TCP", "3000"]