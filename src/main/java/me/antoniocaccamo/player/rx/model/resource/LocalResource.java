package me.antoniocaccamo.player.rx.model.resource;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.file.Path;
import java.time.Duration;

/**
 * @author antoniocaccamo on 18/02/2020
 */
@Getter
@Setter
@Entity @DiscriminatorValue(Resource.LOCATION.LOCAL)
public class LocalResource extends Resource {

    @Override
    public Path getLocalPath() {
        return null;
    }

    public static LocalResourceBuilder builder() {
        return new LocalResourceBuilder();
    }


    public static final class LocalResourceBuilder {
        private LocalResource localResource;

        private LocalResourceBuilder() {
            localResource = new LocalResource();
        }

        public static LocalResourceBuilder aLocalResource() {
            return new LocalResourceBuilder();
        }

        public LocalResourceBuilder withType(TYPE type) {
            localResource.setType(type);
            return this;
        }

//        public LocalResourceBuilder withLocation(LOCATION location) {
//            localResource.setLocation(location);
//            return this;
//        }

        public LocalResourceBuilder withPath(String path) {
            localResource.setPath(path);
            return this;
        }

        public LocalResourceBuilder withDuration(Duration duration) {
            localResource.setDuration(duration);
            return this;
        }

        public LocalResource build() {
            return localResource;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
