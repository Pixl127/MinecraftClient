package de.pixl.pixlclient.cosmetics.cape;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.json.JsonParser;
import de.pixl.pixlclient.cosmetics.CosmeticLoader;
import de.pixl.pixlclient.cosmetics.cape.capes.JsonCape;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CapeLoader implements CosmeticLoader<String, Cape> {

    @Override
    public Cape load(String name) {
        String data = null;
        try (InputStream stream = Objects.requireNonNull(CapeLoader.class.getClassLoader().getResourceAsStream("assets/minecraft/pixl/cosmetics.json"))) {
            data = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (data == null) return null;
        Json json = JsonParser.parse(data);
        Json cape = null;
        Gson gson = new GsonBuilder().create();
        for (JsonElement element : json.get("capes").getAsJsonArray()) {
            cape = JsonParser.parse(gson.toJson(element));
            if (cape.getString("name").equalsIgnoreCase(name)) break;
            cape = null;
        }
        if (cape == null) return null;
        return new JsonCape(
                cape.contains("display") ? cape.getString("display") : cape.getString("name"),
                cape.getString("name"),
                cape.contains("delay") ? cape.getInt("delay") : 0,
                new ConcurrentLinkedQueue<>(cape.getStringList("frames").stream().map(ResourceLocation::new).toList())
        );
    }
}
