package org.cloudfoundry.samples.music.repositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.Artist;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class AlbumRepositoryPopulator implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;

    private ApplicationContext applicationContext;

    public AlbumRepositoryPopulator() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("albums.json");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals(applicationContext)) {
            AlbumRepository albumRepository =
                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, AlbumRepository.class);
            ArtistRepository artistRepository =
                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, ArtistRepository.class);

            if (albumRepository != null && albumRepository.count() == 0) {
                populate(albumRepository, artistRepository);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public void populate(AlbumRepository repository, ArtistRepository artistRepository) {
        Object entity = getEntityFromResource(sourceData);
        Map<String, Artist> artistMap = new HashMap<>();

        if (entity instanceof Collection) {
            for (Album album : (Collection<Album>) entity) {
                if (album != null) {
                    save(repository, artistRepository, album, artistMap);
                }
            }
        } else {
            save(repository, artistRepository, (Album) entity, artistMap);
        }
    }

    private Album save(AlbumRepository repository, ArtistRepository artistRepository, Album album, Map<String, Artist> artistMap) {
        String artistName = album.getArtist().getName();
        Artist artist = artistMap.get(artistName);
        if (artist == null) {
            artist = artistRepository.save(album.getArtist());
            album.setArtist(artist);
            artistMap.put(artistName, artist);
        }
        Album savedAlbum = repository.save(album);
        return savedAlbum;
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
