package de.pixl.pixlclient.cosmetics;

import java.util.UUID;

public interface CosmeticManager<T> {

    void setActive(T t);

    T getCurrent();

     void tick();

     void render();

     void setForPlayer(UUID uuid, T t);
}
