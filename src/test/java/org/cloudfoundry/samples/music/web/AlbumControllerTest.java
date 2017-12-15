package org.cloudfoundry.samples.music.web;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.Artist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("in-memory")
public class AlbumControllerTest {


    @Autowired
    AlbumController underTest;

    @Test
    public void add() {
        Album album = underTest.add(new Album("asdf", new Artist("asdf"), "1979", "electronica"));
        String id = album.getId();
        Assert.notNull(id, "id should be set");

    }
}
