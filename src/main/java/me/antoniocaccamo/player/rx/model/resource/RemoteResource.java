package me.antoniocaccamo.player.rx.model.resource;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.time.Duration;

/**
 * @author antoniocaccamo on 18/02/2020
 */

@Getter
@Setter
public abstract class RemoteResource extends AbstractResource {


    public static RemoteResourceBuilder builder() {
        return new RemoteResourceBuilder();
    }


    public static final class RemoteResourceBuilder {
        private LocalResource localResource;

        private RemoteResourceBuilder() {
            localResource = new LocalResource();
        }

        public static RemoteResourceBuilder aLocalResource() {
            return new RemoteResourceBuilder();
        }

        public RemoteResourceBuilder withType(TYPE type) {
            localResource.setType(type);
            return this;
        }

        public RemoteResourceBuilder withLocation(LOCATION location) {
            localResource.setLocation(location);
            return this;
        }

        public RemoteResourceBuilder withPath(String path) {
            localResource.setPath(path);
            return this;
        }

        public RemoteResourceBuilder withDuration(Duration duration) {
            localResource.setDuration(duration);
            return this;
        }

        public LocalResource build() {
            return localResource;
        }
    }
}
