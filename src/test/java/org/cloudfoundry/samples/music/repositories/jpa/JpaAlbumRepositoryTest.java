package org.cloudfoundry.samples.music.repositories.jpa;


import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.Artist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("in-memory")
public class JpaAlbumRepositoryTest {

    @Autowired
    JpaAlbumRepository underTest;

    @Autowired
    JpaArtistRepository artistRepo;

    @Test
    public void testOne() {
        Artist artist1 = artistRepo.save(new Artist("Die Doofen"));
        Album album = underTest.save(new Album("asdf", artist1, "1979", "electronica"));
        String id = album.getId();
        Assert.notNull(id, "id should be set");
    }

    @Test
    public void testTwo() {
        Artist asdf = new Artist("asdf");
        Artist artist1 = artistRepo.save(asdf);

        Album album1 = underTest.save(new Album("asdf1", artist1, "1980", "electronica"));
        assertSame("artist should be same", artist1, album1.getArtist());

        Album album2 = underTest.save(new Album("asdf2", artist1, "1979", "electronica"));
        String id = album1.getId();
        Assert.notNull(id, "id should be set");

        assertEquals("One instance expected", 1, artistRepo.findAll().size());
        assertEquals("One instance expected", 2, underTest.findAll().size());
    }
}
