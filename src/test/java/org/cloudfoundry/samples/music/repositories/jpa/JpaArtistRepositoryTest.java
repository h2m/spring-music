package org.cloudfoundry.samples.music.repositories.jpa;


import org.cloudfoundry.samples.music.domain.Artist;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("in-memory")
public class JpaArtistRepositoryTest {

    @Autowired
    JpaArtistRepository underTest;

    @Test
    public void test() {
        Artist artist1 = underTest.save(new Artist("Die Doofen"));
        Artist artist2 = underTest.save(new Artist("Die Doofen"));
        assertEquals("name should be set", "Die Doofen", artist1.getName());
        assertEquals("name should be set", "Die Doofen", artist2.getName());

        assertSame("artist should be same", artist1, artist2);
        assertEquals("One instance expected", 1, underTest.findAll().size());
    }
}
