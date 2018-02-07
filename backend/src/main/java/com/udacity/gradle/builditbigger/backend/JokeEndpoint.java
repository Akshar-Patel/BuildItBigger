package com.udacity.gradle.builditbigger.backend;

import com.example.joke_lib.JokeTeller;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/** An endpoint class we are exposing */
@Api(
        name = "jokeApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class JokeEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "tellMeAJoke")
    public JokeBean tell() {
        JokeBean jokeBean = new JokeBean();
        jokeBean.setJoke(JokeTeller.tell());
        return jokeBean;
    }

}
