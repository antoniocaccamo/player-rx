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
public class LocalResource extends AbstractResource {

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

        public LocalResourceBuilder withLocation(LOCATION location) {
            localResource.setLocation(location);
            return this;
        }

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
        final StringBuilder sb = new StringBuilder("LocalResource{");
        sb.append("type=").append(type);
        sb.append(", location=").append(location);
        sb.append(", path='").append(path).append('\'');
        sb.append(", duration=").append(duration);
        sb.append('}');
        return sb.toString();
    }
}
