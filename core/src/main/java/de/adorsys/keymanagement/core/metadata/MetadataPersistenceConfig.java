package de.adorsys.keymanagement.core.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;
import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class MetadataPersistenceConfig {

    @NonNull
    private final Class<? extends KeyMetadata> metadataClass;

    @NonNull
    @Builder.Default
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantSerde()).create();

    public static class InstantSerde implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Instant.parse(json.getAsString());
        }
    }
}
