package org.cloudfoundry.samples.music.repositories.jpa;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.Artist;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.cloudfoundry.samples.music.repositories.ArtistRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"in-memory", "mysql", "postgres", "oracle", "sqlserver"})
public interface JpaArtistRepository extends JpaRepository<Artist, String>, ArtistRepository{
}
