package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/")
public class GreetingResource {
    @Path("/script1")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getScript() {
        return new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/migration/script1.txt"))
        )).lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Path("/scripts")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getScripts() throws URISyntaxException {
        URL directoryUrl = getClass().getResource("/migration");
        System.out.println(directoryUrl);
        File directoryFile = new File(directoryUrl.toURI());
        if (!directoryFile.isDirectory()) {
            throw new IllegalArgumentException(directoryUrl + " is not a directory!");
        }

        return Arrays.stream(directoryFile.listFiles())
                .filter(File::isFile)
                .map(file -> {
                    try {
                        return new BufferedReader(
                                new FileReader(file))
                                .lines()
                                .collect(
                                        Collectors.joining(System.lineSeparator()));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.joining("\n---\n"));
    }
}