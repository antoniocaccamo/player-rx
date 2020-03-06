package me.antoniocaccamo.player.rx.model.resource;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.nio.file.Path;
import java.time.Duration;

/**
 * @author antoniocaccamo on 18/02/2020
 */

@Getter
@Setter
@Entity
@DiscriminatorValue(Resource.LOCATION.REMOTE)
public class RemoteResource extends Resource {


    public static RemoteResourceBuilder builder() {
        return new RemoteResourceBuilder();
    }

    @Override @Transient
    public Path getLocalPath() {
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public static final class RemoteResourceBuilder {
        private RemoteResource remoteResource;

        private RemoteResourceBuilder() {
            remoteResource = new RemoteResource();
        }

        public static RemoteResourceBuilder aLocalResource() {
            return new RemoteResourceBuilder();
        }

        public RemoteResourceBuilder withType(TYPE type) {
            remoteResource.setType(type);
            return this;
        }

//        public RemoteResourceBuilder withLocation(LOCATION location) {
//            localResource.setLocation(location);
//            return this;
//        }

        public RemoteResourceBuilder withPath(String path) {
            remoteResource.setPath(path);
            return this;
        }

        public RemoteResourceBuilder withDuration(Duration duration) {
            remoteResource.setDuration(duration);
            return this;
        }

        public RemoteResource build() {
            return remoteResource;
        }
    }
}
