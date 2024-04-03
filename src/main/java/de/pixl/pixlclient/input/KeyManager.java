package de.pixl.pixlclient.input;

import com.mojang.blaze3d.platform.InputConstants;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.utils.Manager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyManager extends Manager<KeyMapping, IKey> {

    public void tick() {
        values().forEach(IKey::tick);
    }

    public void inject() {
        KeyMapping[] mappings = keys().toArray(KeyMapping[]::new);
        Options options = Client.minecraft.options;
        options.keyMappings = ArrayUtils.addAll(options.keyMappings, mappings);
        preventConficts(options.keyMappings, mappings);
    }

    private void preventConficts(KeyMapping[] existing, KeyMapping[] added) {
        Set<KeyMapping> mappings = new HashSet<>(List.of(existing));
        Arrays.asList(added).forEach(mappings::remove);
        Set<String> check = new HashSet<>(Arrays.stream(added).map(KeyMapping::saveString).toList());
        for (KeyMapping mapping : mappings)
            if (check.contains(mapping.saveString())) mapping.setKey(InputConstants.Type.KEYSYM.getOrCreate(-1));
    }
}
