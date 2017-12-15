package org.cloudfoundry.samples.music.web;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.Artist;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.cloudfoundry.samples.music.repositories.ArtistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/albums")
public class AlbumController {
    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;

    @Autowired
    public AlbumController(AlbumRepository repository, ArtistRepository artistRepository) {
        this.albumRepository = repository;
        this.artistRepository =artistRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Album> albums() {
        return albumRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Album add(@RequestBody @Valid Album album) {
        logger.info("Adding album " + album.getId());
        String artistName = album.getArtist().getName();
        Artist artist = artistRepository.findOne(artistName);
        if (artist == null) {
            artist = artistRepository.save(album.getArtist());
            album.setArtist(artist);
        }
        return albumRepository.save(album);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Album update(@RequestBody @Valid Album album) {
        logger.info("Updating album " + album.getId());
        return albumRepository.save(album);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Album getById(@PathVariable String id) {
        logger.info("Getting album " + id);
        return albumRepository.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String id) {
        logger.info("Deleting album " + id);
        albumRepository.delete(id);
    }
}