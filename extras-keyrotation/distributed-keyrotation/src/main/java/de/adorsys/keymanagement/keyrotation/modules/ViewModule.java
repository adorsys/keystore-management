package de.adorsys.keymanagement.keyrotation.modules;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.keyrotation.api.services.KeyView;
import de.adorsys.keymanagement.keyrotation.impl.services.KeyViewImpl;

@Module
public abstract class ViewModule {

    @Binds
    abstract KeyView view(KeyViewImpl view);
}
